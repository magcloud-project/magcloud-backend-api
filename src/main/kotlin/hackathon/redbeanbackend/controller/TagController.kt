package hackathon.redbeanbackend.controller

import hackathon.redbeanbackend.dto.TagCreateDTO
import hackathon.redbeanbackend.dto.TagResponseDTO
import hackathon.redbeanbackend.service.TagService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/tag")
class TagController(private val tagService: TagService) {
    @GetMapping("/{id}")
    fun onRegisterRequested(@PathVariable id: Long): ResponseEntity<TagResponseDTO> {
        val result = tagService.findTagById(id)
        return ResponseEntity.ok(result)
    }

    @PostMapping
    fun createNewTag(@RequestBody dto: TagCreateDTO): ResponseEntity<TagResponseDTO> {
        val result = tagService.createNewTag(dto)
        return ResponseEntity.ok(result)
    }
}
