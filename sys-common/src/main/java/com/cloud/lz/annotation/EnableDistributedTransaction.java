package com.cloud.lz.annotation;

import com.cloud.lz.config.DistributedTransactionConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DistributedTransactionConfig.class)
public @interface EnableDistributedTransaction {
}
