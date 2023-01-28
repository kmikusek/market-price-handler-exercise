package com.fx.santander

import com.fx.santander.domain.subscriber.PricesSubscriber
import com.fx.santander.repository.PriceRepository
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.io.IOException

@RunWith(SpringRunner::class)
@Import(TestConfig::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SantanderApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var repository: PriceRepository

    @Autowired
    lateinit var subscriber: PricesSubscriber

    lateinit var mockBackEnd: MockWebServer

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @BeforeAll
    @Throws(IOException::class)
    fun setUp() {
        mockBackEnd = MockWebServer()
        mockBackEnd.play(8282)
    }

    @AfterAll
    @Throws(IOException::class)
    fun tearDown() {
        mockBackEnd.shutdown()
    }

    @Test
    fun `should return the third price as the most recent for EUR_USD`() {
        //given
        subscriber.onMessage("106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001")
        subscriber.onMessage("107, EUR/USD, 1.1010,1.2010,01-06-2020 12:01:01:002")
        subscriber.onMessage("108, EUR/USD, 1.1020,1.2020,01-06-2020 12:01:01:003")
        subscriber.onMessage("108, GBP_USD, 1.1020,1.2020,01-06-2020 12:01:01:004")

        //when
        val result = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/v1/price/{instrument}", "EUR_USD")
                .accept(MediaType.APPLICATION_JSON)
        )

        //then
        result.andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", `is`(108)))
            .andExpect(jsonPath("$.instrument", `is`("EUR_USD")))
            .andExpect(jsonPath("$.bid", `is`(1.100898)))
            .andExpect(jsonPath("$.ask", `is`(1.203202)))
            .andExpect(jsonPath("$.timestamp", `is`("2020-06-01T12:01:01.003")))
    }

    @Test
    fun `should return the second price as the most recent`() {
        //given
        subscriber.onMessage("106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001")
        subscriber.onMessage("107, EUR/USD, 1.1010,1.2010,01-06-2020 12:01:01:003")
        subscriber.onMessage("108, EUR/USD, 1.1020,1.2020,01-06-2020 12:01:01:002")
        subscriber.onMessage("108, GBP_USD, 1.1020,1.2020,01-06-2020 12:01:01:004")

        //when
        val result = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/v1/price/{instrument}", "EUR_USD")
                .accept(MediaType.APPLICATION_JSON)
        )

        //then
        result.andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", `is`(107)))
            .andExpect(jsonPath("$.instrument", `is`("EUR_USD")))
            .andExpect(jsonPath("$.bid", `is`(1.099899)))
            .andExpect(jsonPath("$.ask", `is`(1.202201)))
            .andExpect(jsonPath("$.timestamp", `is`("2020-06-01T12:01:01.003")))
    }

    @Test
    fun `should return two value prices for multilines`() {
        //given
        subscriber.onMessage("106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 1.1010,1.2010,01-06-2020 12:01:01:001\n" +
                "108, GBP/USD, 1.1020,1.2020,01-06-2020 12:01:01:001")
        subscriber.onMessage("109, EUR/USD, 1.1010,1.2010,01-06-2020 12:01:01:002\n" +
                "110, EUR/JPY, 1.1015,1.2010,01-06-2020 12:01:01:002\n" +
                "111, GBP/USD, 1.1025,1.2020,01-06-2020 12:01:01:002")
        subscriber.onMessage("108, GBP/USD, 1.1020,1.2020,01-06-2020 12:01:01:004")

        //when
        val result1 = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/v1/price/{instrument}", "EUR_USD")
                .accept(MediaType.APPLICATION_JSON)
        )
        val result2 = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/v1/price/{instrument}", "EUR_JPY")
                .accept(MediaType.APPLICATION_JSON)
        )

        //then
        result1.andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", `is`(109)))
            .andExpect(jsonPath("$.instrument", `is`("EUR_USD")))
            .andExpect(jsonPath("$.bid", `is`(1.099899)))
            .andExpect(jsonPath("$.ask", `is`(1.202201)))
            .andExpect(jsonPath("$.timestamp", `is`("2020-06-01T12:01:01.002")))
        result2.andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", `is`(110)))
            .andExpect(jsonPath("$.instrument", `is`("EUR_JPY")))
            .andExpect(jsonPath("$.bid", `is`(1.1003985)))
            .andExpect(jsonPath("$.ask", `is`(1.202201)))
            .andExpect(jsonPath("$.timestamp", `is`("2020-06-01T12:01:01.002")))
    }
}
