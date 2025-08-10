package ac.dnd.server.notification.github.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GitHubIssueResponse(
    val id: Long,
    val number: Int,
    val title: String,
    val body: String?,
    @JsonProperty("html_url")
    val htmlUrl: String,
    val state: String,
    @JsonProperty("created_at")
    val createdAt: String
)