package cholog

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql

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
        // Given
        jdbcTemplate.update("INSERT INTO users (id, name, email) VALUES (1, 'John', 'john@example.com')")

        // When
        val user = memberRepository.findById(1L)

        // Then
        assertThat(user).isNotNull
        assertThat(user?.name).isEqualTo("John")
        assertThat(user?.email).isEqualTo("john@example.com")
    }

    @Test
    @Sql("/test-data.sql")
    fun testFindAllWithSql() {
        // When
        val users = memberRepository.findAll()

        // Then
        assertThat(users).hasSize(3)
        assertThat(users.map { it.name }).contains("Alice", "Bob", "Charlie")
    }
}
