package ac.dnd.server.file.infrastructure.web.dto.response

data class FileUploadResponse(
    val id: Long,
    val originalFileName: String,
    val fileUrl: String,
    val fileSize: Long,
    val contentType: String
)
