package com.stacklabs.micronaut.workshop.agency.aop

import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class LoggingAdvice : MethodInterceptor<Any, Any> {

    companion object {
        val LOG = LoggerFactory.getLogger(LoggingAdvice::class.java)
    }

    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {
        val attributes = context.attributes
            .joinToString(prefix = "[", postfix = "]", transform = { "${it.key}=${it.value}" })
        val parameters = context.parameters
            .entries
            .joinToString(prefix = "[", postfix = "]", transform = { "${it.key}=${it.value.value}" })
        LOG.info("In ${context.targetMethod.declaringClass.simpleName}.${context.methodName} with attributes $attributes and params $parameters...")

        return context.proceed()
    }
}