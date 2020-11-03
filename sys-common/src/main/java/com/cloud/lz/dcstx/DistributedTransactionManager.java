package com.cloud.lz.dcstx;

import com.cloud.lz.utils.RedisUtil;
import io.micrometer.core.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.*;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class DistributedTransactionManager  {
    protected transient Log logger = LogFactory.getLog(this.getClass());
    @Resource
    private RedisUtil redisUtil;

    @Resource(name = "distributedExecutor")
    private ThreadPoolTaskExecutor executor;

    private PlatformTransactionManager manager;

    public static ThreadLocal<DistributedTransactionStatus> threadLocal = new ThreadLocal<>();

    public DistributedTransactionManager(PlatformTransactionManager manager) {
        this.manager = manager;
    }


    public void doBegin(DistributedTransactionStatus transaction,DistributedTransactionDefinition definition) throws NoSuchFieldException, IllegalAccessException {
        Connection con = null;
        DefaultTransactionStatus real = (DefaultTransactionStatus)manager.getTransaction(definition);
        Class<? extends DefaultTransactionStatus> clazz = real.getClass();
        Field field = clazz.getDeclaredField("transaction");
        field.setAccessible(true);
        if(transaction == null)
            transaction = new DistributedTransactionManager.DistributedTransactionStatus(field.get(real),5,definition.type.equals(definition.TYPE_START)?true:false);
        try {
            transaction.definition = definition;
            threadLocal.set(transaction);
            redisUtil.set(definition.getSignatureStr(),definition.STATE_INIT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void commit(TransactionStatus transactionStatus) throws TransactionException {
        DistributedTransactionStatus disTS = (DistributedTransactionStatus) transactionStatus;
        //分布式事务发起方
        if(disTS.definition.type.equals(DistributedTransactionDefinition.TYPE_START)){
            try {
                manager.commit(disTS);
                redisUtil.set(disTS.definition.signatureStr,disTS.definition.STATE_COMMIT);
            } finally {
                if(!disTS.isNewSynchronization())
                    threadLocal.remove();
            }
        }else{
            executor.execute(()->{
                try{
                    int timeOut = disTS.timeout;
                    long end = System.currentTimeMillis() + timeOut * 6000;
                    int state=0;
                    while (System.currentTimeMillis() <= end && state==0){
                        Object value = redisUtil.get(disTS.definition.signatureStr);
                        if(value != null)
                            state = Integer.parseInt(String.valueOf(value));
                    }
                    if(state == disTS.definition.STATE_COMMIT){
                        manager.commit(disTS);
                        /**如果服务提供方提交失败
                         *
                         * 可以通过binlog记录服务调用方的commit id 然后通过binlog日志进行事务回滚
                         * **/
                        //手动清理缓存
                        //需要把currentThread传入到线程中，获取ThreadLocal里面的transaction对象（满足spring事务嵌套）
                        //或者直接在外层commit方法中执行clear方法，清空缓存。线程里面直接使用（不满足spring事务嵌套）
                        /**spring执行的清理过程**/
//                        if (status.isNewSynchronization()) {
//                            TransactionSynchronizationManager.clear();
//                        }
//
//                        if (status.isNewTransaction()) {
//                            this.doCleanupAfterCompletion(status.getTransaction());
//                        }
                        TransactionSynchronizationManager.clear();

                    }else{
                        manager.rollback(disTS);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(!disTS.isNewSynchronization())
                        TransactionSynchronizationManager.clear();
                    if(disTS.isNewTransaction()){
                        threadLocal.remove();
                    }
                    redisUtil.del(disTS.definition.signatureStr);
                }
            });
        }
    }

    public void rollback(TransactionStatus transactionStatus) throws TransactionException, SQLException {
        DistributedTransactionStatus status = (DistributedTransactionStatus) transactionStatus;
        try {
            manager.rollback(status);
        } finally {
            if(status.definition.type.equals(DistributedTransactionDefinition.TYPE_START)){
                redisUtil.set(status.definition.signatureStr,status.definition.STATE_ROLLBACK);
            }else{
                redisUtil.del(status.definition.signatureStr);
            }
            if(status.isNewTransaction())
                threadLocal.remove();
        }
    }



    public static class DistributedTransactionStatus extends DefaultTransactionStatus {
        private String name;
        private int timeout;
        private boolean autoCommit = false;
        private Connection connection;
        private static DistributedTransactionDefinition definition;
        private static TransactionStatus target;

        public DistributedTransactionStatus(@Nullable Object transaction,@Nullable Integer timeout,boolean sync) {
            super(transaction,true,sync,false,false,null);
            if(timeout != null)
               this.timeout = timeout;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
        public boolean isAutoCommit() {
            return autoCommit;
        }

        public void setAutoCommit(boolean autoCommit) {
            this.autoCommit = autoCommit;
        }
        public Connection getConnection() {
            return connection;
        }

        public void setConnection(Connection connection) {
            this.connection = connection;
        }
    }
    public static class DistributedTransactionDefinition extends DefaultTransactionDefinition {
        public static final String TYPE_START= "start";
        public static final String TYPE_PROVIDER = "provider";
        private static int  STATE_INIT = 0;  //调用方发起事务
        private static int  STATE_COMMIT = 1; //调用方成功提交 提供方提交
        private static int STATE_ROLLBACK = -1; //调用方提交失败 提供方回滚
        private String type;
        private String signatureStr;

        public DistributedTransactionDefinition(@NotNull String type, @NotNull String signatureStr) {
            super();
            if(StringUtils.isNotEmpty(type))
                this.type = type;
            else
                this.type = TYPE_START;
            this.signatureStr = signatureStr;
        }
        public String getType() {
            return type;
        }

        public String getSignatureStr() {
            return signatureStr;
        }
    }

}
