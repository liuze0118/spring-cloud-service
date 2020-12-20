package com.cloud.lz.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
@ConditionalOnProperty(prefix = "lz",value = "type",havingValue = "auto_config")
@Import(StorageConfig.class)
public class LzAutoConfig {
    public LzAutoConfig() {

    }
}
