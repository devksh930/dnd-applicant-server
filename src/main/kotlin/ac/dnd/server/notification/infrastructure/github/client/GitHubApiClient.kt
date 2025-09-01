package ac.dnd.server.notification.infrastructure.github.client

import ac.dnd.server.notification.infrastructure.github.dto.GitHubIssueRequest
import ac.dnd.server.notification.infrastructure.github.dto.GitHubIssueResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange
interface GitHubApiClient {
    @PostExchange("/repos/{owner}/{repo}/issues")
    fun createIssue(
        @PathVariable("owner") owner: String,
        @PathVariable("repo") repo: String,
        @RequestBody request: GitHubIssueRequest
    ): GitHubIssueResponse
}
