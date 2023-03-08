package co.bearus.magcloud.controller

import co.bearus.magcloud.domain.service.TagService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/tag")
class TagController(private val tagService: TagService) {
    @GetMapping("/{id}")
    fun onRegisterRequested(@PathVariable id: Long): ResponseEntity<co.bearus.magcloud.controller.dto.response.TagResponseDTO> {
        val result = tagService.findTagById(id)
        return ResponseEntity.ok(result)
    }

    @PostMapping
    fun createNewTag(@RequestBody @Valid dto: co.bearus.magcloud.controller.dto.request.TagCreateDTO): ResponseEntity<co.bearus.magcloud.controller.dto.response.TagResponseDTO> {
        val result = tagService.createNewTag(dto)
        return ResponseEntity.ok(result)
    }
}
