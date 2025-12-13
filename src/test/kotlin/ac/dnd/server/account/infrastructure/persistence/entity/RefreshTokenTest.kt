package ac.dnd.server.account.infrastructure.persistence.entity

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.fixture.RefreshTokenFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

@UnitTest
class RefreshTokenTest : DescribeSpec({

    describe("isExpired") {

        it("만료 시간이 지나면 true를 반환한다") {
            // given
            val refreshToken = RefreshTokenFixture.createExpiredByTime()

            // when
            val result = refreshToken.isExpired()

            // then
            result shouldBe true
        }

        it("expired 플래그가 true이면 만료 시간과 관계없이 true를 반환한다") {
            // given
            val refreshToken = RefreshTokenFixture.createExpiredByFlag()

            // when
            val result = refreshToken.isExpired()

            // then
            result shouldBe true
        }

        it("만료 시간이 지나지 않고 expired 플래그가 false이면 false를 반환한다") {
            // given
            val refreshToken = RefreshTokenFixture.createValid()

            // when
            val result = refreshToken.isExpired()

            // then
            result shouldBe false
        }
    }

    describe("updateToken") {

        it("토큰과 만료 시간을 업데이트한다") {
            // given
            val refreshToken = RefreshTokenFixture.createValid()
            val newToken = "new-refresh-token"
            val newExpiresAt = LocalDateTime.now().plusDays(60)

            // when
            refreshToken.updateToken(newToken, newExpiresAt)

            // then
            refreshToken.token shouldBe newToken
            refreshToken.expiresAt shouldBe newExpiresAt
        }

        it("토큰 업데이트 후에도 유효한 상태를 유지한다") {
            // given
            val refreshToken = RefreshTokenFixture.createValid()
            val newToken = "rotated-refresh-token"
            val newExpiresAt = LocalDateTime.now().plusDays(30)

            // when
            refreshToken.updateToken(newToken, newExpiresAt)

            // then
            refreshToken.isExpired() shouldBe false
        }
    }
})
