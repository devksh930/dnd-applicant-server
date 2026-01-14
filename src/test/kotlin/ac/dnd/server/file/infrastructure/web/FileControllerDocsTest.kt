package ac.dnd.server.file.infrastructure.web

import ac.dnd.server.annotation.RestDocsTest
import ac.dnd.server.documenation.DocumentUtils
import ac.dnd.server.documenation.MockMvcFactory
import ac.dnd.server.file.application.service.FileUploadService
import ac.dnd.server.file.infrastructure.persistence.entity.FileEntity
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.partWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParts
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.multipart.MultipartFile

@RestDocsTest
class FileControllerDocsTest {

    @Mock
    private lateinit var fileUploadService: FileUploadService

    @InjectMocks
    private lateinit var controller: FileController

    companion object {
        private const val HOST_API = "api.dnd.ac"
    }

    @Test
    fun `단일 파일 업로드 - 200 문서화`(contextProvider: RestDocumentationContextProvider) {
        // given
        val bytes = "hello world".toByteArray()
        val multipart = MockMultipartFile(
            "file",
            "hello.txt",
            MediaType.TEXT_PLAIN_VALUE,
            bytes
        )

        val saved = FileEntity(
            originalFileName = "hello.txt",
            storedFileName = "uuid-hello.txt",
            fileUrl = "https://cdn.example.com/uuid-hello.txt",
            fileSize = bytes.size.toLong(),
            contentType = MediaType.TEXT_PLAIN_VALUE
        )
        ReflectionTestUtils.setField(saved, "id", 1L)

        given(fileUploadService.upload(any())).willReturn(saved)

        // when & then
        MockMvcFactory.getRestDocsMockMvc(contextProvider, HOST_API, controller)
            .perform(
                RestDocumentationRequestBuilders.multipart("/files")
                    .file(multipart)
            )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andDo(
                DocumentUtils.document(
                    "post-file-upload",
                    "파일",
                    "단일 파일 업로드",
                    "단일 파일을 업로드하고 파일 정보를 반환합니다.",
                    requestParts(
                        partWithName("file").description("업로드할 파일")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("파일 ID"),
                        fieldWithPath("originalFileName").type(JsonFieldType.STRING).description("원본 파일명"),
                        fieldWithPath("fileUrl").type(JsonFieldType.STRING).description("파일 접근 URL"),
                        fieldWithPath("fileSize").type(JsonFieldType.NUMBER).description("파일 크기(Byte)"),
                        fieldWithPath("contentType").type(JsonFieldType.STRING).description("컨텐트 타입")
                    )
                )
            )
    }

    @Test
    fun `다중 파일 업로드 - 200 문서화`(contextProvider: RestDocumentationContextProvider) {
        // given
        val file1 = MockMultipartFile("files", "a.png", MediaType.IMAGE_PNG_VALUE, byteArrayOf(1, 2, 3))
        val file2 = MockMultipartFile("files", "b.jpg", MediaType.IMAGE_JPEG_VALUE, byteArrayOf(4, 5))

        val saved1 = FileEntity(
            originalFileName = "a.png",
            storedFileName = "uuid-a.png",
            fileUrl = "https://cdn.example.com/uuid-a.png",
            fileSize = 3,
            contentType = MediaType.IMAGE_PNG_VALUE
        )
        ReflectionTestUtils.setField(saved1, "id", 10L)

        val saved2 = FileEntity(
            originalFileName = "b.jpg",
            storedFileName = "uuid-b.jpg",
            fileUrl = "https://cdn.example.com/uuid-b.jpg",
            fileSize = 2,
            contentType = MediaType.IMAGE_JPEG_VALUE
        )
        ReflectionTestUtils.setField(saved2, "id", 11L)

        given(fileUploadService.uploadMultiple(any<List<MultipartFile>>())).willReturn(listOf(saved1, saved2))

        // when & then
        MockMvcFactory.getRestDocsMockMvc(contextProvider, HOST_API, controller)
            .perform(
                RestDocumentationRequestBuilders.multipart("/files/multiple")
                    .file(file1)
                    .file(file2)
            )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andDo(
                DocumentUtils.document(
                    "post-file-upload-multiple",
                    "파일",
                    "다중 파일 업로드",
                    "여러 파일을 한 번에 업로드하고 파일 정보 목록을 반환합니다.",
                    requestParts(
                        partWithName("files").description("업로드할 파일 목록")
                    ),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                        fieldWithPath("[].originalFileName").type(JsonFieldType.STRING).description("원본 파일명"),
                        fieldWithPath("[].fileUrl").type(JsonFieldType.STRING).description("파일 접근 URL"),
                        fieldWithPath("[].fileSize").type(JsonFieldType.NUMBER).description("파일 크기(Byte)"),
                        fieldWithPath("[].contentType").type(JsonFieldType.STRING).description("컨텐트 타입")
                    )
                )
            )
    }
}
