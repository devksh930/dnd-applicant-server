package ac.dnd.server.notification.infrastructure.web

import ac.dnd.server.notification.application.GitHubIssueCreateService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/google")
class GoogleFormController(
    private val gitHubIssueCreateService: GitHubIssueCreateService
) {

    @PostMapping("/form")
    fun googleForm(
        @RequestBody request: Map<String, String>,
    ): ResponseEntity<Unit> {

        gitHubIssueCreateService.createIssueFromForm(request)
        return ResponseEntity.ok().build();
    }
}