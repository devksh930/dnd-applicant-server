package ac.dnd.server.file.infrastructure.web

import ac.dnd.server.file.application.dto.FileDownloadData
import ac.dnd.server.file.application.service.FileUploadService
import ac.dnd.server.file.infrastructure.persistence.entity.File
import ac.dnd.server.shared.web.GlobalExceptionHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.filter.CharacterEncodingFilter
import java.io.ByteArrayInputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@ExtendWith(MockitoExtension::class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class FileControllerTest {

    @Mock
    private lateinit var fileUploadService: FileUploadService

    @InjectMocks
    private lateinit var controller: FileController

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        // Resource(InputStreamResource) 다운로드를 위해 기본 메시지 컨버터들을 사용하도록 빌더를 직접 구성
        val builder: StandaloneMockMvcBuilder = MockMvcBuilders
            .standaloneSetup(controller)
            .setControllerAdvice(GlobalExceptionHandler())
            .addFilter(CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
        mockMvc = builder.build()
    }

    @Test
    fun `단일 파일 업로드 - 200 OK 및 응답 본문 검증`() {
        // given
        val bytes = "hello world".toByteArray()
        val multipart = MockMultipartFile(
            "file",
            "hello.txt",
            MediaType.TEXT_PLAIN_VALUE,
            bytes
        )

        val saved = File(
            originalFileName = "hello.txt",
            storedFileName = "uuid-hello.txt",
            fileUrl = "https://cdn.example.com/uuid-hello.txt",
            fileSize = bytes.size.toLong(),
            contentType = MediaType.TEXT_PLAIN_VALUE
        )
        // BaseEntity.id는 val 이므로 리플렉션으로 설정
        ReflectionTestUtils.setField(saved, "id", 1L)

        given(fileUploadService.upload(any())).willReturn(saved)

        // when & then
        mockMvc.perform(
            multipart("/files").file(multipart)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.originalFileName").value("hello.txt"))
            .andExpect(jsonPath("$.fileUrl").value("https://cdn.example.com/uuid-hello.txt"))
            .andExpect(jsonPath("$.fileSize").value(bytes.size))
            .andExpect(jsonPath("$.contentType").value(MediaType.TEXT_PLAIN_VALUE))
    }

    @Test
    fun `파일 다운로드 - 200 OK, 헤더 및 바디 검증`() {
        // given
        val originalFileName = "테스트 파일.pdf"
        val contentType = MediaType.APPLICATION_PDF_VALUE
        val contentBytes = "%PDF-1.7 mock pdf".toByteArray()

        val downloadData = FileDownloadData(
            inputStream = ByteArrayInputStream(contentBytes),
            originalFileName = originalFileName,
            contentType = contentType,
            fileSize = contentBytes.size.toLong()
        )

        given(fileUploadService.download(1L)).willReturn(downloadData)

        val encoded = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8).replace("+", "%20")
        val expectedContentDisposition = "attachment; filename*=UTF-8''$encoded"

        // when & then
        mockMvc.perform(
            get("/files/{fileId}/download", 1L)
        )
            .andExpect(status().isOk)
            .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, expectedContentDisposition))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PDF))
            .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, contentBytes.size.toString()))
            .andExpect(content().bytes(contentBytes))
    }
}
