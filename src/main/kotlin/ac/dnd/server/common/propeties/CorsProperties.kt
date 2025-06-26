package ac.dnd.server.common.propeties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cors")
class CorsProperties(
    val allowedOriginsPatterns: List<String> = emptyList(),
    val allowedHeaders: List<String> = emptyList(),
    val exposedHeaders: List<String> = emptyList(),
    val allowedMethods: List<String> = emptyList(),
    val allowCredentials: Boolean = false,
    val maxAge: Long = 3600L
)