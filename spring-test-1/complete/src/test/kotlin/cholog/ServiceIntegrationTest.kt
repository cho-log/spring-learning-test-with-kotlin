package cholog

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ServiceIntegrationTest {
    @Autowired
    lateinit var memberService: MemberService

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun testWithRealRepository() {
        // When
        val result = memberService.create("John", "john@example.com")

        // Then
        assertThat(result.id).isNotNull()
        assertThat(result.name).isEqualTo("John")
        assertThat(memberRepository.findById(result.id!!)).isNotNull()
    }

    @Test
    fun testDuplicateEmailWithRealRepository() {
        // Given
        memberService.create("First", "test@example.com")

        assertThrows<IllegalArgumentException> {
            memberService.create("Second", "test@example.com")
        }
    }
}
