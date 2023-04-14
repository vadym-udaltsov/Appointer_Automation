package com.bot.dagger;

import com.bot.model.CommandType;
import dagger.MapKey;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@MapKey
public @interface CommandKey {
    CommandType value();
}
