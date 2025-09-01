package ac.dnd.server.notification.infrastructure.github.config

import ac.dnd.server.notification.infrastructure.github.client.GitHubApiClient
import ac.dnd.server.notification.infrastructure.github.exception.GitHubApiException
import ac.dnd.server.shared.config.properties.GitHubProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatusCode
import org.springframework.util.StreamUtils
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import java.nio.charset.StandardCharsets

@Configuration
class GitHubHttpInterfaceConfig {

    @Bean
    fun githubApiClient(gitHubProperties: GitHubProperties): GitHubApiClient {
        val restClient = RestClient.builder()
            .baseUrl("https://api.github.com")
            .defaultHeader("Authorization", "Bearer ${gitHubProperties.token}")
            .defaultHeader("Accept", "application/vnd.github+json")
            .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
            .defaultStatusHandler(HttpStatusCode::isError) { _, response ->
                val status = response.statusCode
                val rawBody = try {
                    StreamUtils.copyToString(response.body, StandardCharsets.UTF_8)
                } catch (ex: Exception) {
                    "<unreadable body>"
                }
                throw GitHubApiException("GitHub API error ${'$'}{status.value()}: ${'$'}rawBody")
            }
            .build()

        val factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build()
        return factory.createClient(GitHubApiClient::class.java)
    }
}