package com.example.springtaskkotlin.controller

import com.example.springtaskkotlin.controller.StringController.Companion.ADD_RETURN_STORAGE
import com.example.springtaskkotlin.controller.StringController.Companion.CANNOT_BE_EMPTY
import com.example.springtaskkotlin.controller.StringController.Companion.GET_STORAGE_SIZE
import com.example.springtaskkotlin.controller.StringController.Companion.PUT_STORAGE
import com.example.springtaskkotlin.exception.CustomException
import com.example.springtaskkotlin.repository.StringRepository
import com.example.springtaskkotlin.repository.StringRepositoryImpl
import com.example.springtaskkotlin.service.CRUDService
import com.example.springtaskkotlin.service.CRUDServiceImpl
import com.example.springtaskkotlin.service.CRUDServiceImpl.ServiceStorage
import com.sun.tools.javac.util.List
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(SpringExtension::class)
@WebMvcTest
@ContextConfiguration
internal class StringControllerTestIT {
    companion object {
        private val STORAGE_DEFAULT = List.of("Antony", "Joshua", "Winner", "Champion")
        private const val PARAM_STRING = "{\"array\":[\"Antony\",\"Joshua\",\"Winner\",\"Champion\"]}"
        private const val BAD_PARAM_STRING = "\"array\":[\"A\",\"J\"]"
        private const val BAD_PARAM_STRING2 = "{\"array\": \"A\"}"
        private const val PARAM_STRING3 = "{\"array\":[\"Antony\"]}"
        private const val PARAM_EXPECTED3 = "{\"array\":[\"Antony\",\"Joshua\",\"Winner\",\"Champion\",\"Antony\"]}"
        private const val BAD_PARAM_STRING4 = "{\"array\": []}"
    }

    @Autowired
    lateinit var stringRepository: StringRepository

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var crudService: CRUDService

    @Autowired
    lateinit var stringController: StringController

    @BeforeEach
    fun setUp() {
        mvc = MockMvcBuilders.standaloneSetup(stringController).build()
        stringRepository.clear()
    }

    @Test
    @DisplayName("When call PUT_STORAGE then storage must have correct size & content")
    @Throws(Exception::class)
    fun whenDataAppendOK() {
        crudService.getSize().let { assertThat(it == 0).isTrue }
        mvc.perform(
            put(PUT_STORAGE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(PARAM_STRING)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
        assertTrue(crudService.getSize() == 4)
        assertTrue(crudService.getAll() == STORAGE_DEFAULT)
    }

    @Test
    @DisplayName("When call PUT_STORAGE fails then storage must contain empty List<String>")
    @Throws(java.lang.Exception::class)
    fun whenDataAppendFail() {
        mvc.perform(
            put(PUT_STORAGE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(BAD_PARAM_STRING)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
        assertTrue(crudService.getSize() == 0)
        assertThat(crudService.getAll()).isEmpty()
    }

    @Test
    @DisplayName("When call PUT_STORAGE contains empty array like {'array':[]} then custom exception is thrown")
    fun whenExceptionIsThrown() {
        Assertions.assertThatThrownBy {
            mvc
                .perform(
                    put(PUT_STORAGE).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(BAD_PARAM_STRING4)
                )
        }
            .hasCause(CustomException(CANNOT_BE_EMPTY, HttpStatus.BAD_REQUEST))
    }

    @Test
    @DisplayName(
        "When call ADD_RETURN_STORAGE then storage has correct size(no changes) " +
                "& content(no changes) & response (storage + income)"
    )
    @Throws(java.lang.Exception::class)
    fun whenDataAddOk() {
        crudService.addAll(setUpDefault())
        assertTrue(crudService.getSize() == 4)
        mvc.perform(
            post(ADD_RETURN_STORAGE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(PARAM_STRING3)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andExpect(MockMvcResultMatchers.content().json(PARAM_EXPECTED3))
        assertTrue(crudService.getSize() == 4)
        assertTrue(crudService.getAll() == STORAGE_DEFAULT)
    }

    @Test
    @DisplayName("When call ADD_RETURN_STORAGE fails then storage must contain empty List<String>")
    @Throws(java.lang.Exception::class)
    fun whenDataAddFails() {
        mvc.perform(
            post(ADD_RETURN_STORAGE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(BAD_PARAM_STRING2)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
        assertTrue(crudService.getSize() == 0)
        assertThat(crudService.getAll()).isEmpty()
    }

    @Test
    @DisplayName("When call GET_STORAGE_SIZE then return correct array size of 4")
    @Throws(java.lang.Exception::class)
    fun getAmountOfStrings() {
        crudService.addAll(setUpDefault())
        val result = mvc.perform(get(GET_STORAGE_SIZE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andExpect(MockMvcResultMatchers.content().string("4"))
            .andReturn()
        assertEquals(result.response.contentAsString, "4")
    }

    private fun setUpDefault(): ServiceStorage = ServiceStorage(STORAGE_DEFAULT)

    @Configuration
    class TestConfig {
        @Bean
        fun stringRepository(): StringRepository {
            return StringRepositoryImpl()
        }

        @Bean
        fun crudService(): CRUDService {
            return CRUDServiceImpl(stringRepository())
        }

        @Bean
        fun stringController(): StringController {
            return StringController(crudService())
        }
    }
}