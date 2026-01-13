package ac.dnd.server.file.application.storage.impl

import ac.dnd.server.file.application.storage.FileStorage
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.InputStream

@Component
@ConditionalOnProperty(
    prefix = "cloud.aws",
    name = ["enabled"],
    havingValue = "false",
    matchIfMissing = true
)
class LoggingFileStorage : FileStorage {

    private val log = LoggerFactory.getLogger(LoggingFileStorage::class.java)

    override fun store(
        storedFileName: String,
        contentType: String,
        inputStream: InputStream,
        size: Long
    ): String {
        // 파일 내용을 실제로 저장하지 않고 로그만 남김
        log.info("[DEV/TEST] store called: name={}, contentType={}, size={}", storedFileName, contentType, size)
        // 로컬/더미 URL 반환(클라이언트 표시에만 사용)
        return "http://localhost/mock-storage/$storedFileName"
    }

    override fun load(storedFileName: String): InputStream {
        log.info("[DEV/TEST] load called: name={}", storedFileName)
        // 실제 저장을 하지 않으므로 빈 스트림 반환
        return ByteArrayInputStream(ByteArray(0))
    }
}
