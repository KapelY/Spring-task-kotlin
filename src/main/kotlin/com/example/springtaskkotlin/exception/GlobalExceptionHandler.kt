package com.example.springtaskkotlin.exception

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.Getter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import java.time.LocalDateTime

data class CustomException(val msg: String? = null, val status: HttpStatus? = null) : RuntimeException()

@Getter
open class ExceptionResponse(status: HttpStatus, message: String) {
    private var statusCode: Int

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    protected var timestamp: LocalDateTime
    private var message: String

    init {
        statusCode = status.value()
        timestamp = LocalDateTime.now()
        this.message = message
    }
}

@ControllerAdvice(basePackages = ["com.example.springtaskkotlin.controller"])
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    @ResponseBody
    fun handleAccessDeniedException(
        ex: Exception?
    ): ExceptionResponse {
        println(HttpStatus.BAD_REQUEST)
        return ExceptionResponse(HttpStatus.BAD_REQUEST, USE_TEMPLATE_JSON_ARRAY)
    }

    companion object {
        const val USE_TEMPLATE_JSON_ARRAY = "You should use json object like:{" +
                "array: ['some', 'onemore']}"
    }
}

