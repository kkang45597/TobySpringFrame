package com.mingi.springframe.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Aspect
@Component
public class TransactionAspect {

    @Autowired
    private PlatformTransactionManager transactionManager;

    // @Transactional
//    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    @Around("execution(* com.mingi.springframe.service.UserServiceImpl.*(..))")
    public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            return result;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }
}