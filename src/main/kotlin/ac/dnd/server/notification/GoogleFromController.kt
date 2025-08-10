package ac.dnd.server.notification

import ac.dnd.server.notification.github.service.GitHubService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RestController
@RequestMapping("/google")
class GoogleFromController(
    private val gitHubService: GitHubService
) {

    @PostMapping("/form")
    fun googleForm(
        @RequestBody request: Map<String, String>,
    ): ResponseEntity<Unit> {

        val markdownText = StringBuilder()
        request.forEach { (question, answer) ->
            val title = question.split("\n").first().trim()
            markdownText.append("### $title\n")
            markdownText.append("$answer\n\n")
        }

        val markdownContent = markdownText.toString()
        println(markdownContent)

        // GitHub에 이슈 생성
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val issueTitle = "코드리뷰 제출 - $timestamp"

        val githubIssue = gitHubService.createIssue(issueTitle, markdownContent)


        return ResponseEntity.ok().build();
    }
}