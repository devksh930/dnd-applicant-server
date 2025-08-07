package ac.dnd.server.common.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object JsonUtils {

    private val MAPPER: ObjectMapper = ObjectMapper().apply {
        val javaTimeModule = JavaTimeModule().apply {
            addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            addSerializer(ZonedDateTime::class.java, ZonedDateTimeSerializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            addDeserializer(ZonedDateTime::class.java, InstantDeserializer.ZONED_DATE_TIME)
        }

        registerModule(javaTimeModule)
        registerModule(KotlinModule.Builder().build())
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    fun getMapper(): ObjectMapper = MAPPER

    fun <T> fromJson(inputStream: InputStream?, clazz: Class<T>): T? {
        if (inputStream == null) {
            return null
        }

        return try {
            MAPPER.readValue(inputStream, clazz)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun <T> fromJson(json: String?, clazz: Class<T>): T? {
        if (json == null) {
            return null
        }

        return try {
            MAPPER.readValue(json, clazz)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun <T> fromJson(jsonNode: JsonNode?, clazz: Class<T>): T? {
        if (jsonNode == null) {
            return null
        }

        return try {
            MAPPER.readValue(jsonNode.traverse(), clazz)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun <T> fromJson(inputStream: InputStream?, typeReference: TypeReference<T>): T? {
        if (inputStream == null) {
            return null
        }

        return try {
            MAPPER.readValue(inputStream, typeReference)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun <T> fromJson(json: String?, typeReference: TypeReference<T>): T? {
        if (json == null) {
            return null
        }

        return try {
            MAPPER.readValue(json, typeReference)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun <T> fromJson(jsonNode: JsonNode?, typeReference: TypeReference<T>): T? {
        if (jsonNode == null) {
            return null
        }

        return try {
            MAPPER.readValue(jsonNode.traverse(), typeReference)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun <T> fromJson(bytes: ByteArray?, typeReference: TypeReference<T>): T? {
        if (bytes == null || bytes.isEmpty()) {
            return null
        }

        return try {
            MAPPER.readValue(bytes, typeReference)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun <T> fromJson(bytes: ByteArray?, clazz: Class<T>): T? {
        if (bytes == null || bytes.isEmpty()) {
            return null
        }

        return try {
            MAPPER.readValue(bytes, clazz)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun fromJson(inputStream: InputStream?): JsonNode? {
        if (inputStream == null) {
            return null
        }

        return try {
            MAPPER.readTree(inputStream)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun fromJson(json: String?): JsonNode? {
        if (json == null) {
            return null
        }

        return try {
            MAPPER.readTree(json)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun <T> fromJsonArray(inputStream: InputStream?, clazz: Class<T>): List<T> {
        if (inputStream == null) {
            return emptyList()
        }

        val collectionType: CollectionType = TypeFactory.defaultInstance().constructCollectionType(List::class.java, clazz)

        return try {
            MAPPER.readValue(inputStream, collectionType)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun <T> fromJsonArray(json: String?, clazz: Class<T>): List<T> {
        if (json == null) {
            return emptyList()
        }

        val collectionType: CollectionType = TypeFactory.defaultInstance().constructCollectionType(List::class.java, clazz)

        return try {
            MAPPER.readValue(json, collectionType)
        } catch (e: IOException) {
            throw JsonDecodeException(e)
        }
    }

    fun toJson(obj: Any): String {
        return try {
            MAPPER.writeValueAsString(obj)
        } catch (e: IOException) {
            throw JsonEncodeException(e)
        }
    }

    fun toJsonByte(obj: Any): ByteArray {
        return try {
            MAPPER.writeValueAsBytes(obj)
        } catch (e: IOException) {
            throw JsonEncodeException(e)
        }
    }

    fun toPrettyJson(obj: Any): String {
        return try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
        } catch (e: IOException) {
            throw JsonEncodeException(e)
        }
    }

    fun get(jsonNode: JsonNode?): String? {
        if (jsonNode == null || jsonNode.isNull) {
            return null
        }
        return jsonNode.asText()
    }

    fun <T> convertValue(obj: Any, clazz: TypeReference<T>): T {
        return MAPPER.convertValue(obj, clazz)
    }

    class JsonEncodeException(cause: Throwable) : RuntimeException(cause)

    class JsonDecodeException(cause: Throwable) : RuntimeException(cause)
}
