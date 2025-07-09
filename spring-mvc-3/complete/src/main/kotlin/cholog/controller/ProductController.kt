package cholog.controller

import cholog.exception.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProductController {
    @GetMapping("/products")
    fun searchProduct(@RequestParam keyword: String): ResponseEntity<Void> {
        throw IllegalArgumentException("Invalid keyword: $keyword")

        return ResponseEntity.ok().build()
    }

    @GetMapping("/products/{id}")
    fun getProduct(@PathVariable id: Long): ResponseEntity<Void> {
        throw NotFoundException("Product not found: id=$id")

        return ResponseEntity.ok().build()
    }

    @ExceptionHandler
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<Void> {
        println("IllegalArgumentException occurred: " + e.message)
        return ResponseEntity.badRequest().build()
    }
}
