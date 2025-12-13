package ac.dnd.server.account.infrastructure.security.jwt

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.fixture.JwtPropertiesFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotBeEmpty
import java.util.UUID

@UnitTest
class JwtTokenProviderTest : DescribeSpec({

    val jwtProperties = JwtPropertiesFixture.create()
    val jwtTokenProvider = JwtTokenProvider(jwtProperties)

    describe("generateAccessToken") {

        it("유효한 Access Token을 생성한다") {
            // given
            val userKey = UUID.randomUUID()
            val email = "test@example.com"
            val role = "ADMIN"

            // when
            val token = jwtTokenProvider.generateAccessToken(userKey, email, role)

            // then
            token.shouldNotBeEmpty()
            jwtTokenProvider.validateToken(token) shouldBe true
        }

        it("생성된 토큰에서 userKey를 추출할 수 있다") {
            // given
            val userKey = UUID.randomUUID()
            val email = "test@example.com"
            val role = "ADMIN"

            // when
            val token = jwtTokenProvider.generateAccessToken(userKey, email, role)

            // then
            jwtTokenProvider.extractUserKey(token) shouldBe userKey
        }

        it("생성된 토큰에서 email을 추출할 수 있다") {
            // given
            val userKey = UUID.randomUUID()
            val email = "test@example.com"
            val role = "ADMIN"

            // when
            val token = jwtTokenProvider.generateAccessToken(userKey, email, role)

            // then
            jwtTokenProvider.extractEmail(token) shouldBe email
        }

        it("생성된 토큰에서 role을 추출할 수 있다") {
            // given
            val userKey = UUID.randomUUID()
            val email = "test@example.com"
            val role = "ADMIN"

            // when
            val token = jwtTokenProvider.generateAccessToken(userKey, email, role)

            // then
            jwtTokenProvider.extractRole(token) shouldBe role
        }
    }

    describe("generateRefreshToken") {

        it("유효한 Refresh Token을 생성한다") {
            // given
            val userKey = UUID.randomUUID()

            // when
            val token = jwtTokenProvider.generateRefreshToken(userKey)

            // then
            token.shouldNotBeEmpty()
            jwtTokenProvider.validateToken(token) shouldBe true
        }

        it("생성된 Refresh Token에서 userKey를 추출할 수 있다") {
            // given
            val userKey = UUID.randomUUID()

            // when
            val token = jwtTokenProvider.generateRefreshToken(userKey)

            // then
            jwtTokenProvider.extractUserKey(token) shouldBe userKey
        }

        it("Access Token과 Refresh Token은 서로 다른 값이다") {
            // given
            val userKey = UUID.randomUUID()
            val email = "test@example.com"
            val role = "ADMIN"

            // when
            val accessToken = jwtTokenProvider.generateAccessToken(userKey, email, role)
            val refreshToken = jwtTokenProvider.generateRefreshToken(userKey)

            // then
            accessToken shouldNotBe refreshToken
        }
    }

    describe("validateToken") {

        it("유효한 토큰이면 true를 반환한다") {
            // given
            val userKey = UUID.randomUUID()
            val token = jwtTokenProvider.generateRefreshToken(userKey)

            // when
            val result = jwtTokenProvider.validateToken(token)

            // then
            result shouldBe true
        }

        it("잘못된 형식의 토큰이면 false를 반환한다") {
            // given
            val invalidToken = "invalid.token.format"

            // when
            val result = jwtTokenProvider.validateToken(invalidToken)

            // then
            result shouldBe false
        }

        it("빈 문자열이면 false를 반환한다") {
            // given
            val emptyToken = ""

            // when
            val result = jwtTokenProvider.validateToken(emptyToken)

            // then
            result shouldBe false
        }

        it("다른 secret key로 생성된 토큰이면 false를 반환한다") {
            // given
            val otherProperties = JwtPropertiesFixture.create(
                secret = "other-secret-key-for-jwt-token-generation-must-be-at-least-256-bits"
            )
            val otherProvider = JwtTokenProvider(otherProperties)
            val tokenFromOtherProvider = otherProvider.generateRefreshToken(UUID.randomUUID())

            // when
            val result = jwtTokenProvider.validateToken(tokenFromOtherProvider)

            // then
            result shouldBe false
        }
    }
})