package cholog

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class DataLayerTest {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    lateinit var memberRepository: MemberRepository

    @BeforeEach
    fun setUp() {
        memberRepository = JdbcMemberRepository(jdbcTemplate)
    }
    
    @Test
    fun testFindById() {
        // TODO: Write a test for MemberRepository.findById using @JdbcTest
        // 1. Use jdbcTemplate.update() to insert test data
        // 2. Call memberRepository.findById()
        // 3. Verify the result using assertThat()
    }
    
    @Test
    fun testFindAllWithSql() {
        // TODO: Write a test for MemberRepository.findAll using @Sql annotation
        // 1. Add @Sql("/test-data.sql") annotation to this method
        // 2. Call memberRepository.findAll()
        // 3. Verify the result has 3 members
    }
}
