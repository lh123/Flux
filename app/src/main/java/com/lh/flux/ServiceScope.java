package com.lh.flux;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by liuhui on 2016/5/12.
 * ServiceScope
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceScope
{
}
