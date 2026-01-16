package ac.dnd.server.review.infrastructure.web

import ac.dnd.server.review.application.service.MemberReviewQueryService
import ac.dnd.server.review.infrastructure.web.dto.response.MemberReviewProjectResponse
import ac.dnd.server.review.infrastructure.web.mapper.MemberReviewWebMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member-review")
class MemberReviewQueryController(
    private val memberReviewQueryService: MemberReviewQueryService,
    private val memberReviewWebMapper: MemberReviewWebMapper
) {

    @GetMapping("/{linkKey}")
    fun getMemberReviewsByLink(
        @PathVariable linkKey: String
    ): ResponseEntity<MemberReviewProjectResponse> {
        val result = memberReviewQueryService.getMemberReviewsByLink(linkKey)
        val response = memberReviewWebMapper.toResponse(
            result.generation,
            result.teamName,
            result.members
        )
        return ResponseEntity.ok(response)
    }
}