package ac.dnd.server.file.application.service

import ac.dnd.server.file.application.dto.FileDownloadData
import ac.dnd.server.file.application.storage.FileStorage
import ac.dnd.server.file.exception.FileNotFoundException
import ac.dnd.server.file.infrastructure.persistence.entity.FileEntity
import ac.dnd.server.file.infrastructure.persistence.repository.FileJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class FileUploadService(
    private val fileStorage: FileStorage,
    private val fileJpaRepository: FileJpaRepository
) {

    @Transactional
    fun upload(file: MultipartFile): FileEntity {
        val originalFileName = file.originalFilename ?: "unknown"
        val storedFileName = generateStoredFileName(originalFileName)
        val contentType = file.contentType ?: "application/octet-stream"

        val fileUrl = fileStorage.store(
            storedFileName = storedFileName,
            contentType = contentType,
            inputStream = file.inputStream,
            size = file.size
        )

        val fileEntity = FileEntity(
            originalFileName = originalFileName,
            storedFileName = storedFileName,
            fileUrl = fileUrl,
            fileSize = file.size,
            contentType = contentType
        )

        return fileJpaRepository.save(fileEntity)
    }

    @Transactional
    fun uploadMultiple(files: List<MultipartFile>): List<FileEntity> {
        return files.map { upload(it) }
    }

    fun download(fileId: Long): FileDownloadData {
        val file = fileJpaRepository.findById(fileId)
            .orElseThrow { FileNotFoundException() }

        return FileDownloadData(
            inputStream = fileStorage.load(file.storedFileName),
            originalFileName = file.originalFileName,
            contentType = file.contentType,
            fileSize = file.fileSize
        )
    }

    private fun generateStoredFileName(originalFileName: String): String {
        val extension = originalFileName.substringAfterLast(".", "")
        val uuid = UUID.randomUUID().toString()
        return if (extension.isNotEmpty()) {
            "$uuid.$extension"
        } else {
            uuid
        }
    }
}
