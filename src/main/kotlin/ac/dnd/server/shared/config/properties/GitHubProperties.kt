package ac.dnd.server.shared.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "github")
data class GitHubProperties @ConstructorBinding constructor(
    val token: String,
    val repository: Repository
) {
    data class Repository(
        val owner: String,
        val name: String
    )
}