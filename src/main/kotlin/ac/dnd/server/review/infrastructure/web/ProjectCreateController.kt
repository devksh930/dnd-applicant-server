package ac.dnd.server.review.infrastructure.web

import ac.dnd.server.review.application.dto.command.ProjectCreateCommand
import ac.dnd.server.review.application.service.ProjectCreateService
import ac.dnd.server.review.infrastructure.web.dto.request.ProjectCreateRequest
import ac.dnd.server.shared.web.toPutResponse
import org.springframework.http.ResponseEntity
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/project")
class ProjectCreateController(
    private val projectCreateService: ProjectCreateService
) {

    @PutMapping("/{linkKey}")
    fun createProject(
        @PathVariable linkKey: String,
        @Valid @RequestBody request: ProjectCreateRequest
    ): ResponseEntity<Unit> {
        val isFirstSubmission = projectCreateService.createProject(
            ProjectCreateCommand(
                linkKey = linkKey,
                name = request.name,
                description = request.description,
                techStacks = request.techStacks,
                fileId = request.fileId,
                urlLinks = request.urlLinks
            )
        )

        val location = UriComponentsBuilder.fromPath("/project/{linkKey}")
            .buildAndExpand(linkKey)
            .toUri()

        return isFirstSubmission.toPutResponse(location)
    }
}