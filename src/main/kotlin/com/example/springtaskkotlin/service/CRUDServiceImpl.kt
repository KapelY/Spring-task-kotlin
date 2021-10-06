package com.example.springtaskkotlin.service

import com.example.springtaskkotlin.repository.StringRepository
import com.example.springtaskkotlin.service.CRUDServiceImpl.ServiceStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface CRUDService {
    fun addAll(storage: ServiceStorage)

    fun getAll(): List<String>

    fun getAmount(): Int

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

    override fun getAmount(): Int {
        return stringRepository.size()
    }

    override fun addAndReturn(serviceStorage: ServiceStorage): ServiceStorage {
        println(serviceStorage)
        return serviceStorage.apply {
            println(array)
            this.array = getAll() as MutableList<String>
            array.addAll(serviceStorage.array)
            println(array)
        }
    }

    data class ServiceStorage(var array: MutableList<String> = mutableListOf()) {}
}

