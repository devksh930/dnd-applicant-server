package ac.dnd.server.review.infrastructure.web

import ac.dnd.server.review.application.service.LinkQueryService
import ac.dnd.server.review.infrastructure.web.dto.response.LinkQueryResponse
import ac.dnd.server.review.infrastructure.web.mapper.LinkWebMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/link/{linkKey}")
class LinkQueryController(
    private val linkQueryService: LinkQueryService,
    private val linkWebMapper: LinkWebMapper
) {

    @GetMapping
    fun getLink(
        @PathVariable linkKey: String
    ): ResponseEntity<LinkQueryResponse> {
        val formLink = linkQueryService.getLinkInfo(linkKey)
        return ResponseEntity.ok(linkWebMapper.toResponse(formLink))
    }
}