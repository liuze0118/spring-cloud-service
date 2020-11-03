package com.cloud.lz.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedTransactional {
    String type() default "start";  //provider
}
