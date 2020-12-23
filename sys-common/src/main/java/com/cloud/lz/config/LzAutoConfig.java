package com.cloud.lz.config;

import com.cloud.lz.vo.UserVo;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;

@ConditionalOnProperty(prefix = "lz",value = "type",havingValue = "auto_config")
@Import(StorageConfig.class)
//@ComponentScan("com.cloud.lz.annotation")
@Configuration
public class LzAutoConfig {
    public LzAutoConfig() {

    }

    @Bean
    public UserVo userVo(){
        return new UserVo();
    }

}
