package cholog

interface MemberRepository {
    fun findById(id: Long): Member?
    fun findByEmail(email: String): Member?
    fun save(member: Member): Member
    fun findAll(): List<Member>
}
