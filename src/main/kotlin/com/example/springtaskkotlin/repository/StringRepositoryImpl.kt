package com.example.springtaskkotlin.repository

import com.example.springtaskkotlin.service.CRUDServiceImpl.*
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.ArrayList

interface StringRepository {
    fun addAll(serviceStorage: ServiceStorage)

    fun getAll(): List<String>

    fun size(): Int

    fun clear()
}

@Repository
class StringRepositoryImpl : StringRepository {
    private val storage: Queue<String>

    init {
        storage = ConcurrentLinkedQueue()
    }

    override fun addAll(serviceStorage: ServiceStorage) {
        this.storage += serviceStorage
    }

    override fun getAll(): List<String> {
        return ArrayList(storage)
    }

    override fun size(): Int {
        return storage.size
    }

    override fun clear() {
        storage.clear()
    }
}

operator fun Queue<String>.plusAssign(serviceStorage: ServiceStorage) {
    this.addAll(serviceStorage.array)
}