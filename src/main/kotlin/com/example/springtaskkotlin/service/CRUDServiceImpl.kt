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
class CRUDServiceImpl(@Autowired private val stringRepository: StringRepository) : CRUDService {

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
        return ServiceStorage(getAll() + serviceStorage.array)
    }

    data class ServiceStorage(val array: List<String> = mutableListOf()) {}
}

