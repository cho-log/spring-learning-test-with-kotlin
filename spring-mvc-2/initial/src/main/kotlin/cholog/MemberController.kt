package cholog

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import java.util.concurrent.atomic.AtomicLong

@Controller
class MemberController {
    private val members: MutableList<Member> = ArrayList()
    private val index = AtomicLong(1)

    @PostMapping("/members")
    fun create(): ResponseEntity<Void> {
        // TODO: create Member
//        val newMember: Member = Member.toEntity(member, index.getAndIncrement())
//        members.add(newMember)
        return ResponseEntity.badRequest().build()
    }

    @GetMapping("/members")
    fun read(): ResponseEntity<List<Member>> {
        // TODO: Return all stored members.
        return ResponseEntity.badRequest().build()
    }

    @PutMapping("/members/{id}")
    fun update(): ResponseEntity<Void> {
        // TODO: Update member using the request body and the ID from the URL.
//        val member = members.stream()
//            .filter { it: Member -> it.id == id }
//            .findFirst()
//            .orElseThrow { RuntimeException() }
//
//        member.update(newMember)
        return ResponseEntity.badRequest().build()
    }

    @DeleteMapping("/members/{id}")
    fun delete(): ResponseEntity<Void> {
        // TODO: Delete the member using the ID from the URL.
//        val member = members.stream()
//            .filter { it: Member -> it.id == id }
//            .findFirst()
//            .orElseThrow { RuntimeException() }
//
//        members.remove(member)

        return ResponseEntity.badRequest().build()
    }
}
