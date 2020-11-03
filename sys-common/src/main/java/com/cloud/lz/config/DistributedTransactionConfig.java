package com.cloud.lz.config;

import com.cloud.lz.advisor.DistributedTransactionAdvisor;
import com.cloud.lz.dcstx.DistributedTransactionManager;
import com.cloud.lz.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.*;

@Configuration
@Slf4j
public class DistributedTransactionConfig implements BeanDefinitionRegistryPostProcessor {

    @Bean
    public RedisUtil redisUtil(){
        return new RedisUtil();
    }

    @Bean(name = "distributedExecutor")
    public ThreadPoolTaskExecutor threadPoolExecutor(){
        ThreadPoolTaskExecutor poolExecutor = new ThreadPoolTaskExecutor();
        poolExecutor.setThreadNamePrefix("distributed_thread_");
        poolExecutor.setMaxPoolSize(20);
        poolExecutor.setCorePoolSize(10);
        poolExecutor.setAllowCoreThreadTimeOut(false);
        poolExecutor.setQueueCapacity(20);
        poolExecutor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor exe) -> {
            log.warn("当前任务线程池队列已满.");
        });
        poolExecutor.initialize();
        return poolExecutor;
    }

    @Bean
    public DistributedTransactionManager distributedTransactionManager(PlatformTransactionManager transactionManager){
        return new DistributedTransactionManager(transactionManager);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(DistributedTransactionAdvisor.class);
        beanDefinitionRegistry.registerBeanDefinition("distributedTransactionAdvisor",beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
