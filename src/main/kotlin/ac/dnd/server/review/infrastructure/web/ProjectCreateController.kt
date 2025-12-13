package ac.dnd.server.review.infrastructure.web

import ac.dnd.server.review.application.dto.command.ProjectCreateCommand
import ac.dnd.server.review.application.service.ProjectCreateService
import ac.dnd.server.review.infrastructure.web.dto.request.ProjectCreateRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/project")
class ProjectCreateController(
    private val projectCreateService: ProjectCreateService
) {

    @PutMapping("/{linkKey}")
    fun createProject(
        @PathVariable linkKey: String,
        @RequestBody request: ProjectCreateRequest
    ) {
        projectCreateService.createProject(
            ProjectCreateCommand(
                linkKey,
                request.name,
                request.description,
                request.techStack,
                request.urlLinks,
            )
        )
    }
}