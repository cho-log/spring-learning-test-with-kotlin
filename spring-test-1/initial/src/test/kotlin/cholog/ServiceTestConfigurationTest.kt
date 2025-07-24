package cholog

import org.junit.jupiter.api.Test
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
    fun inMemoryMemberRepository(): MemberRepository = InMemoryMemberRepository()
}

@SpringBootTest
@Import(TestConfig::class)
class ServiceTestConfigurationTest {
    
    @Autowired
    lateinit var memberService: MemberService
    
    @Test
    fun testWithTestConfiguration() {
        // TODO: Write a test using @TestConfiguration with InMemoryMemberRepository
        // 1. Call memberService.create()
        // 2. Verify the result has an id
        // 3. This test uses InMemoryMemberRepository instead of JDBC repository
    }
}
