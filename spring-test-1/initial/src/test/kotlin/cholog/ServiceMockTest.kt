package cholog

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class ServiceMockTest {

    @Mock
    lateinit var memberRepository: MemberRepository

    @InjectMocks
    lateinit var memberService: MemberService

    @Test
    fun testWithMock() {
        // TODO: Write a test for MemberService.create using @MockitoBean
        // 1. Mock memberRepository.findByEmail() to return null (no duplicate)
        // 2. Mock memberRepository.save() using given().willReturn()
        // 3. Call memberService.create()
        // 4. Verify the result and mock interaction using verify()
    }
    
    @Test
    fun testDuplicateEmailThrowsException() {
        // TODO: Write a test for duplicate email validation
        // 1. Mock memberRepository.findByEmail() to return existing member
        // 2. Call memberService.create() with same email
        // 3. Verify IllegalArgumentException is thrown with correct message
    }
}
