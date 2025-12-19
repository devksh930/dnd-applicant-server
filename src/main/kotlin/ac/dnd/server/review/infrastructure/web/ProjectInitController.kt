package ac.dnd.server.review.infrastructure.web

import ac.dnd.server.review.application.service.ProjectInitService
import ac.dnd.server.review.infrastructure.web.dto.request.ProjectInitRequest
import ac.dnd.server.review.infrastructure.web.dto.response.ProjectInitLink
import ac.dnd.server.review.infrastructure.web.dto.response.ProjectInitResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/project/init")
class ProjectInitController (
    private val projectInitService: ProjectInitService
){

    @PostMapping
    fun initProject(
        @Valid @RequestBody request: ProjectInitRequest
    ): ResponseEntity<ProjectInitResponse> {
        val results = projectInitService.initProjects(request.generation, request.teamCount)
        val response = ProjectInitResponse(
            generation = request.generation,
            teamCount = request.teamCount,
            links = results.map { (teamName, key) -> ProjectInitLink(teamName = teamName, linkKey = key) }
        )
        return ResponseEntity.ok(response)
    }
}