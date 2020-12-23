package com.cloud.lz.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    @Autowired
    private ApplicationContext context;

    @ConfigurationProperties(value = "storage",ignoreInvalidFields = true)
    @Data
    public static class StorageProperties{
        private String type;
        @Autowired
        private Minio minioPropertie;
    }
    @ConfigurationProperties(value = "storage.minio",ignoreInvalidFields = true)
    @Data
    public static class Minio{
        private String bucket;
    }

    @ConfigurationProperties("storage.minio")
    @Bean
    public Minio Minio(){
        return new Minio();
    }


    @Bean
    public StorageProperties storageProperties(){
        StorageProperties storageProperties = new StorageProperties();
        //storageProperties.setMinioPropertie(minioProperties);
        return storageProperties;
    }

}
