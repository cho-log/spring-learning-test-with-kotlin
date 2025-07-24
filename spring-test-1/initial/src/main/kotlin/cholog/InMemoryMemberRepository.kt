package cholog

import java.util.concurrent.atomic.AtomicLong

class InMemoryMemberRepository : MemberRepository {
    private val users = mutableMapOf<Long, Member>()
    private val idGenerator = AtomicLong(1)
    
    override fun findById(id: Long): Member? {
        return users[id]
    }
    
    override fun findByEmail(email: String): Member? {
        return users.values.find { it.email == email }
    }
    
    override fun findAll(): List<Member> {
        return users.values.toList()
    }
    
    override fun save(member: Member): Member {
        val savedUser = if (member.id == null) {
            val id = idGenerator.getAndIncrement()
            member.copy(id = id)
        } else {
            member
        }
        users[savedUser.id!!] = savedUser
        return savedUser
    }
}
