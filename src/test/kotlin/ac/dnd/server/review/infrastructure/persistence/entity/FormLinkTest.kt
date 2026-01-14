package ac.dnd.server.review.infrastructure.persistence.entity

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.review.domain.enums.FormLinkType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDateTime
import java.util.UUID

@UnitTest
class FormLinkTest : DescribeSpec({

    describe("FormLink.isExpired") {

        it("expired 플래그가 true이면 만료로 판단한다") {
            // given
            val link = FormLinkEntity(
                linkType = FormLinkType.PROJECT,
                key = UUID.randomUUID(),
                targetId = 1L,
                expired = true,
                expirationDateTime = LocalDateTime.now().plusWeeks(2)
            )

            // expect
            link.isExpired().shouldBeTrue()
        }

        it("만료 시간이 과거면 만료로 판단한다") {
            // given
            val link = FormLinkEntity(
                linkType = FormLinkType.PROJECT,
                key = UUID.randomUUID(),
                targetId = 1L,
                expired = false,
                expirationDateTime = LocalDateTime.now().minusSeconds(1)
            )

            // expect
            link.isExpired().shouldBeTrue()
        }

        it("만료 시간이 미래면 만료가 아니다") {
            // given
            val link = FormLinkEntity(
                linkType = FormLinkType.PROJECT,
                key = UUID.randomUUID(),
                targetId = 1L,
                expired = false,
                expirationDateTime = LocalDateTime.now().plusSeconds(3)
            )

            // expect
            link.isExpired().shouldBeFalse()
        }
    }
})
