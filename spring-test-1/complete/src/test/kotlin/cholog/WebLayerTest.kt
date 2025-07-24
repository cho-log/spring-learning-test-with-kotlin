package cholog

import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MemberController::class)
class WebLayerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var memberService: MemberService

    @Test
    fun testGetUser() {
        // Given
        given(memberService.findById(1L)).willReturn(Member(1L, "John", "john@example.com"))

        // When & Then
        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun testCreateUser() {
        // Given
        given(memberService.create(any(), any())).willReturn(Member(1L, "Jane", "jane@example.com"))

        // When & Then
        mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Jane","email":"jane@example.com"}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/users/1"))
            .andExpect(jsonPath("$.id").isNotEmpty)
    }
}
