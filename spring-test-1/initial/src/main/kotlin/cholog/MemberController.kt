package cholog

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
class MemberController(private val memberService: MemberService) {
    
    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<Member> {
        val user = memberService.findById(id)
        return ResponseEntity.ok(user)
    }
    
    @PostMapping("/users")
    fun createUser(@RequestBody request: CreateMemberRequest): ResponseEntity<Member> {
        val user = memberService.create(request.name, request.email)
        return ResponseEntity.created(URI.create("/users/${user.id}")).body(user)
    }
}
