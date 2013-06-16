package com.modwiz.spoutcommands.annotations;


import sun.reflect.annotation.AnnotationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    /**
     * Returns all the aliases that this command is recognized under
     */
    public String[] aliases();

    /**
     * Returns the proper usage of this command
     */
    public String usage() default "";

    /**
     * Returns a description of the command
     */
    public String desc();

    /**
     * Returns the minimum number of args for this command
     */
    public int min() default 0;

    /**
     * Returns the maximum number of args for this command
     */
    public int max() default -1;
}


