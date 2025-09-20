package com.foodback.demo.exception.general

import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExceptionDestination(
    val code: Int,
    val errorType: ErrorType = ErrorType.DEFAULT,
    vararg val details: ExceptionDetail
)

fun ExceptionDestination.findException(): ExceptionDestination? {
    return this::class.findAnnotation()
}

@Retention(AnnotationRetention.RUNTIME)
annotation class ExceptionDetail(
    val key: String,
    val value: String
)