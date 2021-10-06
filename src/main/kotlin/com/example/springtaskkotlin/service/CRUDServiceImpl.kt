package com.example.springtaskkotlin.service

import com.example.springtaskkotlin.repository.StringRepository
import com.example.springtaskkotlin.service.CRUDServiceImpl.ServiceStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface CRUDService {
    fun addAll(storage: ServiceStorage)

    fun getAll(): List<String>

    fun getSize(): Int

    fun addAndReturn(serviceStorage: ServiceStorage): ServiceStorage
}

@Service
class CRUDServiceImpl : CRUDService {

    @Autowired
    private lateinit var stringRepository: StringRepository

    override fun addAll(storage: ServiceStorage) {
        stringRepository.addAll(storage)
    }

    override fun getAll(): List<String> {
        return stringRepository.getAll()
    }

    override fun getSize(): Int {
        return stringRepository.size()
    }

    override fun addAndReturn(serviceStorage: ServiceStorage): ServiceStorage {
        val tempStorage = ServiceStorage(getAll())
        tempStorage.array += serviceStorage.array
        return tempStorage
    }

    data class ServiceStorage(var array: List<String> = listOf()) {}
}

