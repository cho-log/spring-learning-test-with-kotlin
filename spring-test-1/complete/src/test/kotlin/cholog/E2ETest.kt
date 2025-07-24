package cholog

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class E2ETest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun testCreateAndGetUser() {
        // Given
        val createRequest = """{"name":"Test","email":"test@test.com"}"""
        val headers =
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
            }
        val entity = HttpEntity(createRequest, headers)

        // When - Create user
        val createResponse = restTemplate.postForEntity("http://localhost:$port/users", entity, Member::class.java)

        // Then - Verify creation
        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(createResponse.body?.name).isEqualTo("Test")
        assertThat(createResponse.body?.email).isEqualTo("test@test.com")
        assertThat(createResponse.body?.id).isNotNull()

        val userId = createResponse.body?.id!!

        // When - Get user
        val getResponse = restTemplate.getForEntity("http://localhost:$port/users/$userId", Member::class.java)

        // Then - Verify retrieval
        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun testGetNonExistentUser() {
        // When
        val response = restTemplate.getForEntity("http://localhost:$port/users/999", String::class.java)

        // Then
        assertThat(response.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
