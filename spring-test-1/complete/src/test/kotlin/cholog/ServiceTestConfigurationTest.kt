package cholog

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestConfig {
    @Bean
    @Primary
    fun inMemoryUserRepository(): MemberRepository = InMemoryMemberRepository()
}

@SpringBootTest
@Import(TestConfig::class)
class ServiceTestConfigurationTest {
    @Autowired
    lateinit var memberService: MemberService

    @Test
    fun testWithTestConfiguration() {
        // When
        val result = memberService.create("John", "john@example.com")

        // Then
        assertThat(result.id).isNotNull()
        assertThat(result.name).isEqualTo("John")
        // This test uses InMemoryUserRepository instead of JDBC repository
    }

    @Test
    fun testDuplicateEmailWithTestConfiguration() {
        // Given
        memberService.create("First", "test@example.com")

        // When & Then
        assertThrows<IllegalArgumentException> {
            memberService.create("Second", "test@example.com")
        }
    }
}
