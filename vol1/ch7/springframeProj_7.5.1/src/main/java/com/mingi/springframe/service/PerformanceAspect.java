package com.mingi.springframe.service;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component  // annotation based configuration metadata
public class PerformanceAspect {

    @Around("execution(* com.mingi.springframe.service.UserServiceImpl.*(..))")
    public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) 
    		throws Throwable {
        long start = System.currentTimeMillis(); // cross cutting concern
        // MethodInvation의 메서드
        Object returnValue = joinPoint.proceed();  // call target method
        long end = System.currentTimeMillis();    // cross cutting concern

        System.out.println(joinPoint.getSignature().getName() + " execution time: " + (end - start) + "ms");
        return returnValue;
    }
}