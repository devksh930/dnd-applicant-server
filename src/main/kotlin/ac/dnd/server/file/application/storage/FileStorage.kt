package ac.dnd.server.file.application.storage

import java.io.InputStream

interface FileStorage {
    fun store(storedFileName: String, contentType: String, inputStream: InputStream, size: Long): String
    fun load(storedFileName: String): InputStream
}
