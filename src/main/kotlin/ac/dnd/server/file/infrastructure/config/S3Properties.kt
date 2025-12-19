package ac.dnd.server.file.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cloud.aws")
data class S3Properties(
    val s3: S3Config,
    val region: String,
    val credentials: CredentialsConfig
) {
    data class S3Config(
        val bucket: String
    )

    data class CredentialsConfig(
        val accessKey: String,
        val secretKey: String
    )
}
