package cholog

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ServiceIntegrationTest {
    
    @Autowired
    lateinit var memberRepository: MemberRepository
    
    @Autowired
    lateinit var memberService: MemberService
    
    @Test
    fun testWithRealRepository() {
        // TODO: Write an integration test for MemberService.create using real repository
        // 1. Call memberService.create()
        // 2. Verify the result has an id
        // 3. Verify the member exists in repository using memberRepository.findById()
    }
    
    @Test
    fun testDuplicateEmailWithRealRepository() {
        // TODO: Write an integration test for duplicate email validation
        // 1. Create first member with memberService.create()
        // 2. Try to create second member with same email
        // 3. Verify IllegalArgumentException is thrown
    }
}
