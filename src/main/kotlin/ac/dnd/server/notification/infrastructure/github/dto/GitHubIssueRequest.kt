package ac.dnd.server.notification.infrastructure.github.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GitHubIssueRequest(
    val title: String,
    val body: String,
    val labels: List<String> = listOf("form-submission")
)