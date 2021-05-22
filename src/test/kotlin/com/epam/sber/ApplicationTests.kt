package com.epam.sber

import com.epam.sber.constant.ApiUrl
import com.epam.sber.repository.QuoteRepository
import org.apache.logging.log4j.kotlin.logger
import org.json.JSONObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.StopWatch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

val request = MockMvcRequestBuilders
	.post(ApiUrl.QUOTES)
	.content(ResourceUtil.resourceAsString("json/request.json"))
	.contentType(MediaType.APPLICATION_JSON)

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	val logger = logger()

	@Autowired
	lateinit var mockMvc: MockMvc

	@Autowired
	lateinit var quoteRepository: QuoteRepository

	@Test
	fun e2eTest() {

		val sw = StopWatch()
		sw.start("requests")
		//
		val beginTime = LocalDateTime.now()
		for (i in 1..1000) {
			MyRunnable(mockMvc).run()
		}
		sw.stop()
		logger.info(sw.prettyPrint())
		// WHEN

		val resultAction = mockMvc.perform(request) // Выполняем запрос

		val count = quoteRepository.count()
		val endTime = LocalDateTime.now()
		val time = ChronoUnit.MILLIS.between(beginTime, endTime)
		Assertions.assertEquals(2002, count)
		logger.info("Inserted %s quotes for %d millisecond".format(count, time))
		//2002 quotes inserted for 1645 millisecond
		// без блокировки входного потока на время сохранения в БД
		// THEN
		resultAction
			.andExpect(MockMvcResultMatchers.status().isCreated)

		val resultAction2 = mockMvc.perform(
			MockMvcRequestBuilders.get(ApiUrl.ELVL_BY_ISIN + "?isin=RU000A0JX0J2")
		)
		resultAction2
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		val jsonResponse = JSONObject(resultAction2.andReturn().response.contentAsString)
		val expected2 = ResourceUtil.resourceAsString("json/elvlByIsin.json")
		Assertions.assertEquals(expected2, jsonResponse.toString(2))

		val resultAction3 = mockMvc.perform(MockMvcRequestBuilders.get(ApiUrl.ELVLS))
		resultAction3
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		val jsonResponse3 = JSONObject(resultAction3.andReturn().response.contentAsString)
		val expected = ResourceUtil.resourceAsString("json/elvls.json")
		Assertions.assertEquals(expected, jsonResponse3.toString(2))
	}

	class MyRunnable(private val mockMvc: MockMvc): Runnable {
		override fun run() {
			mockMvc.perform(request)
		}
	}
}
