package ac.dnd.server.file.application.service

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.file.application.storage.FileStorage
import ac.dnd.server.file.exception.FileNotFoundException
import ac.dnd.server.file.infrastructure.persistence.entity.FileEntity
import ac.dnd.server.file.infrastructure.persistence.repository.FileJpaRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import java.io.ByteArrayInputStream
import java.util.Optional

@UnitTest
class FileUploadServiceTest : DescribeSpec({

    val fileStorage: FileStorage = mock()
    val fileJpaRepository: FileJpaRepository = mock()
    val fileUploadService = FileUploadService(fileStorage, fileJpaRepository)

    beforeEach {
        reset(fileStorage, fileJpaRepository)
    }

    describe("FileUploadService") {

        context("upload") {
            it("정상적인 파일을 업로드한다") {
                // given
                val content = "test content".toByteArray()
                val file = MockMultipartFile(
                    "file",
                    "test.txt",
                    MediaType.TEXT_PLAIN_VALUE,
                    content
                )
                val expectedUrl = "https://cdn.example.com/uuid.txt"

                whenever(fileStorage.store(any(), any(), any(), any())).thenReturn(expectedUrl)
                whenever(fileJpaRepository.save(any())).thenAnswer { it.arguments[0] }

                // when
                val result = fileUploadService.upload(file)

                // then
                result.originalFileName shouldBe "test.txt"
                result.contentType shouldBe MediaType.TEXT_PLAIN_VALUE
                result.fileSize shouldBe content.size.toLong()
                result.fileUrl shouldBe expectedUrl
                result.storedFileName shouldContain ".txt"

                verify(fileStorage).store(any(), any(), any(), any())
                verify(fileJpaRepository).save(any())
            }

            it("확장자가 없는 파일도 업로드된다") {
                // given
                val content = "test content".toByteArray()
                val file = MockMultipartFile(
                    "file",
                    "testfile",
                    MediaType.TEXT_PLAIN_VALUE,
                    content
                )
                val expectedUrl = "https://cdn.example.com/uuid"

                whenever(fileStorage.store(any(), any(), any(), any())).thenReturn(expectedUrl)
                whenever(fileJpaRepository.save(any())).thenAnswer { it.arguments[0] }

                // when
                val result = fileUploadService.upload(file)

                // then
                result.originalFileName shouldBe "testfile"
                result.storedFileName shouldNotContain "."
            }

            it("파일명이 없으면 unknown으로 저장된다") {
                // given
                val content = "test content".toByteArray()
                val file = MockMultipartFile(
                    "file",
                    null as String?,
                    MediaType.TEXT_PLAIN_VALUE,
                    content
                )
                val expectedUrl = "https://cdn.example.com/uuid"

                whenever(fileStorage.store(any(), any(), any(), any())).thenReturn(expectedUrl)
                whenever(fileJpaRepository.save(any())).thenAnswer { it.arguments[0] }

                // when
                val result = fileUploadService.upload(file)

                // then
                result.originalFileName shouldBe "unknown"
            }

            it("ContentType이 없으면 기본값으로 저장된다") {
                // given
                val content = "test content".toByteArray()
                val file = MockMultipartFile(
                    "file",
                    "test.txt",
                    null,
                    content
                )
                val expectedUrl = "https://cdn.example.com/uuid.txt"

                whenever(fileStorage.store(any(), any(), any(), any())).thenReturn(expectedUrl)
                whenever(fileJpaRepository.save(any())).thenAnswer { it.arguments[0] }

                // when
                val result = fileUploadService.upload(file)

                // then
                result.contentType shouldBe "application/octet-stream"
            }
        }

        context("uploadMultiple") {
            it("여러 파일을 업로드한다") {
                // given
                val file1 = MockMultipartFile("file", "test1.txt", "text/plain", "content1".toByteArray())
                val file2 = MockMultipartFile("file", "test2.txt", "text/plain", "content2".toByteArray())
                val files = listOf(file1, file2)

                whenever(fileStorage.store(any(), any(), any(), any())).thenReturn("url")
                whenever(fileJpaRepository.save(any())).thenAnswer { it.arguments[0] }

                // when
                val results = fileUploadService.uploadMultiple(files)

                // then
                results.size shouldBe 2
                verify(fileStorage, org.mockito.kotlin.times(2)).store(any(), any(), any(), any())
                verify(fileJpaRepository, org.mockito.kotlin.times(2)).save(any())
            }
        }

        context("download") {
            it("존재하는 파일을 다운로드한다") {
                // given
                val fileId = 1L
                val fileEntity = FileEntity(
                    originalFileName = "test.txt",
                    storedFileName = "stored.txt",
                    fileUrl = "url",
                    fileSize = 100L,
                    contentType = "text/plain"
                )
                val inputStream = ByteArrayInputStream("content".toByteArray())

                whenever(fileJpaRepository.findById(fileId)).thenReturn(Optional.of(fileEntity))
                whenever(fileStorage.load("stored.txt")).thenReturn(inputStream)

                // when
                val result = fileUploadService.download(fileId)

                // then
                result.originalFileName shouldBe "test.txt"
                result.contentType shouldBe "text/plain"
                result.fileSize shouldBe 100L
                result.inputStream shouldBe inputStream
            }

            it("존재하지 않는 파일이면 예외가 발생한다") {
                // given
                val fileId = 999L
                whenever(fileJpaRepository.findById(fileId)).thenReturn(Optional.empty())

                // when & then
                shouldThrow<FileNotFoundException> {
                    fileUploadService.download(fileId)
                }
            }
        }
    }
})