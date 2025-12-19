package ac.dnd.server.file.infrastructure.web

import ac.dnd.server.file.application.service.FileUploadService
import ac.dnd.server.file.infrastructure.web.dto.response.FileUploadResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/files")
@ConditionalOnBean(FileUploadService::class)
class FileController(
    private val fileUploadService: FileUploadService
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(@RequestParam("file") file: MultipartFile): FileUploadResponse {
        val savedFile = fileUploadService.upload(file)
        return FileUploadResponse(
            id = savedFile.id!!,
            originalFileName = savedFile.originalFileName,
            fileUrl = savedFile.fileUrl,
            fileSize = savedFile.fileSize,
            contentType = savedFile.contentType
        )
    }

    @PostMapping("/multiple", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadMultipleFiles(@RequestParam("files") files: List<MultipartFile>): List<FileUploadResponse> {
        val savedFiles = fileUploadService.uploadMultiple(files)
        return savedFiles.map { file ->
            FileUploadResponse(
                id = file.id!!,
                originalFileName = file.originalFileName,
                fileUrl = file.fileUrl,
                fileSize = file.fileSize,
                contentType = file.contentType
            )
        }
    }

    @GetMapping("/{fileId}/download")
    fun downloadFile(@PathVariable fileId: Long): ResponseEntity<InputStreamResource> {
        val downloadData = fileUploadService.download(fileId)
        val encodedFileName = URLEncoder.encode(downloadData.originalFileName, StandardCharsets.UTF_8)
            .replace("+", "%20")

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''$encodedFileName")
            .contentType(MediaType.parseMediaType(downloadData.contentType))
            .contentLength(downloadData.fileSize)
            .body(InputStreamResource(downloadData.inputStream))
    }
}
