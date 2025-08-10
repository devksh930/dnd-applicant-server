package ac.dnd.server.notification.github.service

import ac.dnd.server.notification.github.dto.GitHubIssueRequest
import ac.dnd.server.notification.github.dto.GitHubIssueResponse
import ac.dnd.server.shared.config.properties.GitHubProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class GitHubService(
    private val gitHubProperties: GitHubProperties,
    private val githubWebClient: WebClient
) {
    private val logger = LoggerFactory.getLogger(GitHubService::class.java)

    fun createIssue(title: String, body: String): GitHubIssueResponse {
        val request = GitHubIssueRequest(
            title = title,
            body = body
        )

        logger.info("Creating GitHub issue with title: $title")

        return try {
            val response = githubWebClient
                .post()
                .uri("/repos/${gitHubProperties.repository.owner}/${gitHubProperties.repository.name}/issues")
                .header("Authorization", "Bearer ${gitHubProperties.token}")
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GitHubIssueResponse::class.java)
                .block()!!

            logger.info("Successfully created GitHub issue #${response.number}: ${response.htmlUrl}")
            response
        } catch (e: Exception) {
            logger.error("Failed to create GitHub issue", e)
            throw RuntimeException("Failed to create GitHub issue: ${e.message}", e)
        }
    }
}