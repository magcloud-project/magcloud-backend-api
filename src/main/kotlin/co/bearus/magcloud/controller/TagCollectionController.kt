package co.bearus.magcloud.controller

import co.bearus.magcloud.dto.response.TagResponseDTO
import co.bearus.magcloud.service.TagService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tags")
class TagCollectionController(private val tagService: TagService) {
    @GetMapping
    fun onRegisterRequested(): ResponseEntity<List<TagResponseDTO>> {
        val result = tagService.getAllTag()
        return ResponseEntity.ok(result)
    }
}
