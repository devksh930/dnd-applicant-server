package ac.dnd.server.notification.github.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class GitHubWebClientConfig {

    @Bean
    fun githubWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://api.github.com")
            .build()
    }
}