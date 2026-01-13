package ac.dnd.server.file.application.dto

import java.io.InputStream

data class FileDownloadData(
    val inputStream: InputStream,
    val originalFileName: String,
    val contentType: String,
    val fileSize: Long
)