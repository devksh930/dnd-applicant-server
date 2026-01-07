package ac.dnd.server.file.application.storage.impl

import ac.dnd.server.file.application.storage.FileStorage
import ac.dnd.server.file.infrastructure.config.S3Properties
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.InputStream

@Component
@ConditionalOnProperty(prefix = "cloud.aws", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class S3FileStorage(
    private val s3Client: S3Client,
    private val s3Properties: S3Properties
) : FileStorage {

    private val log = LoggerFactory.getLogger(S3FileStorage::class.java)

    override fun store(storedFileName: String, contentType: String, inputStream: InputStream, size: Long): String {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(s3Properties.s3.bucket)
            .key(storedFileName)
            .contentType(contentType)
            .build()

        s3Client.putObject(
            putObjectRequest,
            RequestBody.fromInputStream(inputStream, size)
        )

        val fileUrl = "https://${s3Properties.s3.bucket}.s3.${s3Properties.region}.amazonaws.com/$storedFileName"
        log.debug("Uploaded to S3: {}", fileUrl)
        return fileUrl
    }

    override fun load(storedFileName: String): InputStream {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(s3Properties.s3.bucket)
            .key(storedFileName)
            .build()

        return s3Client.getObject(getObjectRequest)
    }
}
