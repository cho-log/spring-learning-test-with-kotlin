package cholog

import org.springframework.stereotype.Service

@Service
class MemberService(private val memberRepository: MemberRepository) {
    
    fun findById(id: Long): Member {
        return memberRepository.findById(id) ?: throw IllegalArgumentException("User not found")
    }
    
    fun create(name: String, email: String): Member {
        checkDuplicateEmail(email)
        val member = Member(name = name, email = email)
        return memberRepository.save(member)
    }
    
    private fun checkDuplicateEmail(email: String) {
        val existingUser = memberRepository.findByEmail(email)
        if (existingUser != null) {
            throw IllegalArgumentException("Email already exists")
        }
    }
}
