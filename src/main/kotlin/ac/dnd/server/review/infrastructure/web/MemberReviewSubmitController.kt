package ac.dnd.server.review.infrastructure.web

import ac.dnd.server.review.application.dto.command.MemberReviewSubmitCommand
import ac.dnd.server.review.application.dto.command.UrlLinkCommand
import ac.dnd.server.review.application.service.MemberReviewSubmitService
import ac.dnd.server.review.infrastructure.web.dto.request.MemberReviewSubmitRequest
import ac.dnd.server.shared.web.toPutResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/member-review")
class MemberReviewSubmitController(
    private val memberReviewSubmitService: MemberReviewSubmitService
) {

    @PutMapping("/{linkKey}")
    fun submitMemberReview(
        @PathVariable linkKey: String,
        @Valid @RequestBody request: MemberReviewSubmitRequest
    ): ResponseEntity<Unit> {
        val isFirstSubmission = memberReviewSubmitService.submitMemberReview(
            MemberReviewSubmitCommand(
                linkKey = linkKey,
                name = request.name,
                description = request.description,
                urlLinks = request.urlLinks?.map { urlLink ->
                    UrlLinkCommand(
                        type = urlLink.type,
                        url = urlLink.url,
                        order = urlLink.order
                    )
                }
            )
        )

        val location = UriComponentsBuilder.fromPath("/member-review/{linkKey}")
            .buildAndExpand(linkKey)
            .toUri()

        return isFirstSubmission.toPutResponse(location)
    }
}