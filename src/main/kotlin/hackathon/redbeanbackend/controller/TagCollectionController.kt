package hackathon.redbeanbackend.controller

import hackathon.redbeanbackend.dto.APIResponse
import hackathon.redbeanbackend.dto.AuthRegisterDTO
import hackathon.redbeanbackend.dto.TagResponseDTO
import hackathon.redbeanbackend.service.TagService
import hackathon.redbeanbackend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
