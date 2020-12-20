package com.cloud.lz.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    @Autowired
    private ApplicationContext context;

    @Data
    public static class StorageProperties{
        private String type;
        private StorageMinioProperties minioPropertie;
    }
    @Data
    public static class StorageMinioProperties{
        private String bucket;
    }

    @ConfigurationProperties("storage.minio")
    @Bean
    public StorageMinioProperties storageMinioProperties(){
        return new StorageMinioProperties();
    }

    @ConfigurationProperties("storage")
    @Bean
    public StorageProperties storageProperties(StorageMinioProperties minioProperties){
        StorageProperties storageProperties = new StorageProperties();
        storageProperties.setMinioPropertie(minioProperties);
        return storageProperties;
    }

}
