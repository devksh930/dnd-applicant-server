package ac.dnd.server.review.infrastructure.persistence.entity

import ac.dnd.server.review.domain.enums.FormLinkType
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class FormLinkJUnitTest {

    @Test
    fun `expired 플래그가 true이면 만료로 판단한다`() {
        // given
        val link = FormLink(
            linkType = FormLinkType.PROJECT,
            key = UUID.randomUUID(),
            targetId = 1L,
            expired = true,
            expirationDateTime = LocalDateTime.now().plusWeeks(2)
        )

        // expect
        assertTrue(link.isExpired())
    }

    @Test
    fun `만료 시간이 과거면 만료로 판단한다`() {
        // given
        val link = FormLink(
            linkType = FormLinkType.PROJECT,
            key = UUID.randomUUID(),
            targetId = 1L,
            expired = false,
            expirationDateTime = LocalDateTime.now().minusSeconds(5)
        )

        // expect
        assertTrue(link.isExpired())
    }

    @Test
    fun `만료 시간이 미래면 만료가 아니다`() {
        // given
        val link = FormLink(
            linkType = FormLinkType.PROJECT,
            key = UUID.randomUUID(),
            targetId = 1L,
            expired = false,
            expirationDateTime = LocalDateTime.now().plusSeconds(5)
        )

        // expect
        assertFalse(link.isExpired())
    }
}
