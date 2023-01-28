package com.fx.santander.exceptions

import org.springframework.core.convert.ConversionFailedException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class ControllerAdvice {

    @ResponseBody
    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundHandler(e: NotFoundException): String? {
        return e.message
    }

    @ResponseBody
    @ExceptionHandler(ConversionFailedException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConflict(e: RuntimeException): String? {
        return e.message
    }
}