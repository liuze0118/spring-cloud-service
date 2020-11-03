package com.cloud.lz.advisor;

import com.cloud.lz.annotation.DistributedTransactional;
import com.cloud.lz.dcstx.DistributedTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Aspect
@Component
public class DistributedTransactionAdvisor {

    private AmqpTemplate amqpTemplate;
    @Resource
    private DistributedTransactionManager manager;
    @Autowired
    private ApplicationContext applicationContext;


    @PostConstruct
    public void init(){
        DistributedTransactionManager bean = applicationContext.getBean(DistributedTransactionManager.class);
        System.out.println("初始化切面");
    }

    @Pointcut("@annotation(com.cloud.lz.annotation.DistributedTransactional)")
    public void pointcut(){};

    @Around(value = "pointcut()&&@annotation(distributedTransactional)")
    public void doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint,DistributedTransactional distributedTransactional) throws Throwable {
        String type = distributedTransactional.type();
        String signNature;
        Object[] objects;
        DistributedTransactionAdvisor.HandSignNature  handSignNature = new DistributedTransactionAdvisor.HandSignNature(proceedingJoinPoint);
        if(type.equals(DistributedTransactionManager.DistributedTransactionDefinition.TYPE_START)){
            signNature = proceedingJoinPoint.getSignature().hashCode() + "";
            objects = handSignNature.setSignNatureId(signNature);
        }else{
            signNature = handSignNature.getSignNatureId();
            objects = handSignNature.args;
        }
        DistributedTransactionManager.DistributedTransactionDefinition definition = new DistributedTransactionManager.DistributedTransactionDefinition(type,signNature);
        manager.doBegin(manager.threadLocal.get(),definition);
        try {
            proceedingJoinPoint.proceed(objects);
            manager.commit(manager.threadLocal.get());
        }catch (Exception e){
            e.printStackTrace();
            if(e instanceof RuntimeException)
                manager.rollback(manager.threadLocal.get());
            throw new RuntimeException("do transaction failed,because of :" + e.getMessage());
        }
    }

    private class HandSignNature{
        String signNatureId;
        String names[];
        Object[] args;
        MethodSignature signature;
        public HandSignNature(ProceedingJoinPoint proceedingJoinPoint) {
            signature = (MethodSignature) proceedingJoinPoint.getSignature();
            names = signature.getParameterNames();
            args = proceedingJoinPoint.getArgs();
        }

        public String getSignNatureId(){
            for(int i=0;i<names.length;i++){
                if("signNatureId".equals(names[i])){
                    signNatureId =(String)args[i];
                    break;
                }
            }
            return signNatureId;
        }

        public Object[] setSignNatureId(String signNatureId){
            for(int i=0;i<names.length;i++){
                if("signNatureId".equals(names[i])){
                    args[i] = signNatureId;
                    break;
                }
            }
            return args;
        }


    }




}
