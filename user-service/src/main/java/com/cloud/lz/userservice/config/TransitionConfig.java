package com.cloud.lz.userservice.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class TransitionConfig {


    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
        redisTemplate.setHashKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource setDruidDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        return dataSource;
    }

    //后台监控:因为springboot内置了servlet容器(tomcat)，使用没有web.xml，可以用ServletRegistrationBean替代
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");

        //后台需要有人登陆，账号密码
        HashMap<String,String> initParameters = new HashMap<>();
        //增加配置
        initParameters.put("loginUsername","admin");  //登陆参数是固定的
        initParameters.put("loginPassword","123456");

        //允许谁可以访问(ip)
        initParameters.put("allow","");
        bean.setInitParameters(initParameters);  //设置初始化参数
        return bean;
    }
}
