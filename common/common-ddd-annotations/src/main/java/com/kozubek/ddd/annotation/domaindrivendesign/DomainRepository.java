package com.kozubek.ddd.annotation.domaindrivendesign;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainRepository {}

