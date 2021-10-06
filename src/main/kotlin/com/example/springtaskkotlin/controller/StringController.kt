package com.example.springtaskkotlin.controller

import com.example.springtaskkotlin.exception.CustomException
import com.example.springtaskkotlin.service.CRUDService
import com.example.springtaskkotlin.service.CRUDServiceImpl.ServiceStorage
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class StringController @Autowired constructor(private val crudService: CRUDService) {
private val logger = KotlinLogging.logger {}


    @PutMapping(path = [PUT_STORAGE])
    fun putData(@RequestBody serviceStorage: ServiceStorage) {
        if (serviceStorage.array.isEmpty()) {
            logger.error(EMPTY_ARRAY_INCOMING)
            throw CustomException(CANNOT_BE_EMPTY, HttpStatus.BAD_REQUEST)
        }
        crudService.addAll(serviceStorage)
    }

    @PostMapping(path = [ADD_RETURN_STORAGE])
    fun postData(@RequestBody serviceStorage: ServiceStorage): ServiceStorage =
        crudService.addAndReturn(serviceStorage)

    @GetMapping(path = [GET_STORAGE_SIZE])
    fun getAmount(): Int = crudService.getSize()

    companion object {
        const val CANNOT_BE_EMPTY = "array cannot be empty"
        const val EMPTY_ARRAY_INCOMING = "Empty array path=/append"
        const val PUT_STORAGE = "/managed-strings"
        const val ADD_RETURN_STORAGE = "/managed-strings/storage"
        const val GET_STORAGE_SIZE = "/managed-strings/size"
    }
}