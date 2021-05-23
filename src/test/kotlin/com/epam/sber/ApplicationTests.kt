package com.epam.sber

import com.epam.sber.constant.ApiUrl
import org.json.JSONObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.FileCopyUtils
import java.io.InputStreamReader

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Test
	fun e2eTest() {
		// GIVEN
		val content = "[{\n" +
				"\"isin\":\"RU000A0JX0J2\",\n" +
				"\"bid\":\"100.4\",\n" +
				"\"ask\":\"100.5\"\n" +
				"}," +
				"{" +
				"\"isin\":\"RU0007661625\",\n" +
				"\"bid\":\"200.4\",\n" +
				"\"ask\":\"200.5\"\n" +
				"}]"
		val request = MockMvcRequestBuilders
			.post(ApiUrl.QUOTES)
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)

		// WHEN
		val resultAction = mockMvc.perform(request) // Выполняем запрос

		Thread.sleep(1000)

		// THEN
		resultAction
			.andExpect(MockMvcResultMatchers.status().isCreated)

		val request2 = MockMvcRequestBuilders
			.get(ApiUrl.ELVL_BY_ISIN+"?isin=RU000A0JX0J2")
		val resultAction2 = mockMvc.perform(request2)
		resultAction2
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		val jsonResponse = JSONObject(resultAction2.andReturn().response.contentAsString)
		val expected2 = resourceAsString("json/elvlByIsin.json")
		Assertions.assertEquals(expected2, jsonResponse.toString(2))

		val request3 = MockMvcRequestBuilders
			.get(ApiUrl.ELVLS)
		val resultAction3 = mockMvc.perform(request3)
		resultAction3
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		val jsonResponse3 = JSONObject(resultAction3.andReturn().response.contentAsString)
		val expected = resourceAsString("json/elvls.json")
		Assertions.assertEquals(expected, jsonResponse3.toString(2))
	}

	fun resourceAsString(path: String): String {
		val res = ClassPathResource(path).inputStream
		return FileCopyUtils.copyToString(InputStreamReader(res))
	}
}
