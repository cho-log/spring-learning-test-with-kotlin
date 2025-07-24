package cholog

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class JdbcMemberRepository(private val jdbcTemplate: JdbcTemplate) : MemberRepository {
    
    override fun findById(id: Long): Member? {
        return try {
            jdbcTemplate.queryForObject(
                "SELECT id, name, email FROM users WHERE id = ?",
                { rs, _ -> Member(rs.getLong("id"), rs.getString("name"), rs.getString("email")) },
                id
            )
        } catch (e: Exception) {
            null
        }
    }
    
    override fun findByEmail(email: String): Member? {
        return try {
            jdbcTemplate.queryForObject(
                "SELECT id, name, email FROM users WHERE email = ?",
                { rs, _ -> Member(rs.getLong("id"), rs.getString("name"), rs.getString("email")) },
                email
            )
        } catch (e: Exception) {
            null
        }
    }
    
    override fun findAll(): List<Member> {
        return jdbcTemplate.query(
            "SELECT id, name, email FROM users",
            { rs, _ -> Member(rs.getLong("id"), rs.getString("name"), rs.getString("email")) }
        )
    }
    
    override fun save(member: Member): Member {
        if (member.id == null) {
            val keyHolder = GeneratedKeyHolder()
            jdbcTemplate.update({ connection ->
                val ps = connection.prepareStatement(
                    "INSERT INTO users (name, email) VALUES (?, ?)",
                    arrayOf("id")
                )
                ps.setString(1, member.name)
                ps.setString(2, member.email)
                ps
            }, keyHolder)
            
            val id = keyHolder.key!!.toLong()
            return member.copy(id = id)
        } else {
            jdbcTemplate.update(
                "UPDATE users SET name = ?, email = ? WHERE id = ?",
                member.name, member.email, member.id
            )
            return member
        }
    }
}
