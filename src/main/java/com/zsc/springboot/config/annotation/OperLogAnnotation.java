package com.zsc.springboot.config.annotation;

import java.lang.annotation.*;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/30 19:41
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface OperLogAnnotation {
    String operModul() default ""; // 操作模块
    String operType() default "";  // 操作类型
    String operDesc() default "";  // 操作说明
}
