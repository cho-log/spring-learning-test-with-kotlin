package cholog

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class ServiceMockTest {

    @Mock
    lateinit var memberRepository: MemberRepository

    @InjectMocks
    lateinit var memberService: MemberService

    @Test
    fun testWithMock() {
        // Given
        given(memberRepository.findByEmail("john@example.com")).willReturn(null)
        given(memberRepository.save(any())).willReturn(Member(1L, "John", "john@example.com"))

        // When
        val result = memberService.create("John", "john@example.com")

        // Then
        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("John")
        verify(memberRepository).findByEmail("john@example.com")
        verify(memberRepository).save(any())
    }

    @Test
    fun testDuplicateEmailThrowsException() {
        // Given
        given(memberRepository.findByEmail("john@example.com"))
            .willReturn(Member(1L, "Existing", "john@example.com"))

        // When & Then
        assertThrows<IllegalArgumentException> {
            memberService.create("John", "john@example.com")
        }

        verify(memberRepository).findByEmail("john@example.com")
        verify(memberRepository, never()).save(any())
    }
}
