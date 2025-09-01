package ac.dnd.server.notification.application

import ac.dnd.server.notification.infrastructure.github.client.GitHubApiClient
import ac.dnd.server.notification.infrastructure.github.dto.GitHubIssueRequest
import ac.dnd.server.notification.infrastructure.github.dto.GitHubIssueResponse
import ac.dnd.server.shared.config.properties.GitHubProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class GitHubIssueCreateService(
    private val gitHubProperties: GitHubProperties, private val githubApiClient: GitHubApiClient
) {
    companion object {
        private const val ISSUE_TITLE_PREFIX = "코드리뷰 제출 - "
        private const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm"
        private const val MARKDOWN_HEADER = "### "
    }

    private val logger = LoggerFactory.getLogger(GitHubIssueCreateService::class.java)

    fun createIssueFromForm(requestMap: Map<String, String>): GitHubIssueResponse {
        val markdownContent = buildMarkdownBody(requestMap)
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT))
        val issueTitle = "$ISSUE_TITLE_PREFIX$timestamp"
        val request = GitHubIssueRequest(
            title = issueTitle, body = markdownContent
        )

        logger.info("생성될 깃허브 이슈 제목 : $issueTitle")

        val response = githubApiClient.createIssue(
            gitHubProperties.repository.owner, gitHubProperties.repository.name, request
        )
        return response
    }

    private fun buildMarkdownBody(form: Map<String, String>): String {
        return form.entries.joinToString(separator = "\n\n") { (question, answer) ->
            val title = question.lines().first().trim()
            "$MARKDOWN_HEADER$title\n$answer"
        }
    }


}