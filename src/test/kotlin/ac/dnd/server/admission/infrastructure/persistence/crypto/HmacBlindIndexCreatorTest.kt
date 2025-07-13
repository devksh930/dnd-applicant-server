package ac.dnd.server.admission.infrastructure.persistence.crypto

import ac.dnd.server.annotation.UnitTest
import ac.dnd.server.fixture.EncryptionPropertiesFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotBeEmpty
import java.util.*

@UnitTest
class HmacBlindIndexCreatorTest : DescribeSpec({

    describe("HmacBlindIndexCreator") {

        it("평문을 HMAC 블라인드 인덱스로 변환한다") {
            // given
            val properties = EncryptionPropertiesFixture.create()
            val creator = HmacBlindIndexCreator(properties)
            val plainText = "test@example.com"

            // when
            val result = creator.create(plainText)

            // then
            result.shouldNotBeEmpty()
            // Base64로 인코딩된 결과인지 확인
            Base64.getDecoder().decode(result) // 예외가 발생하지 않으면 유효한 Base64
        }

        it("같은 평문에 대해 항상 같은 블라인드 인덱스를 생성한다") {
            // given
            val properties = EncryptionPropertiesFixture.create()
            val creator = HmacBlindIndexCreator(properties)
            val plainText = "consistent@example.com"

            // when
            val result1 = creator.create(plainText)
            val result2 = creator.create(plainText)

            // then
            result1 shouldBe result2
        }

        it("다른 평문에 대해 다른 블라인드 인덱스를 생성한다") {
            // given
            val properties = EncryptionPropertiesFixture.create()
            val creator = HmacBlindIndexCreator(properties)
            val plainText1 = "user1@example.com"
            val plainText2 = "user2@example.com"

            // when
            val result1 = creator.create(plainText1)
            val result2 = creator.create(plainText2)

            // then
            result1 shouldNotBe result2
        }

        it("다른 HMAC 키를 사용하면 다른 블라인드 인덱스를 생성한다") {
            // given
            val properties1 = EncryptionPropertiesFixture.createWithCustomHmacKey("key1")
            val properties2 = EncryptionPropertiesFixture.createWithCustomHmacKey("key2")
            val creator1 = HmacBlindIndexCreator(properties1)
            val creator2 = HmacBlindIndexCreator(properties2)
            val plainText = "test@example.com"

            // when
            val result1 = creator1.create(plainText)
            val result2 = creator2.create(plainText)

            // then
            result1 shouldNotBe result2
        }

        it("빈 문자열에 대해서도 블라인드 인덱스를 생성한다") {
            // given
            val properties = EncryptionPropertiesFixture.create()
            val creator = HmacBlindIndexCreator(properties)
            val plainText = ""

            // when
            val result = creator.create(plainText)

            // then
            result.shouldNotBeEmpty()
            Base64.getDecoder().decode(result) // 유효한 Base64인지 확인
        }

        it("한글 문자열에 대해서도 블라인드 인덱스를 생성한다") {
            // given
            val properties = EncryptionPropertiesFixture.create()
            val creator = HmacBlindIndexCreator(properties)
            val plainText = "홍길동@테스트.com"

            // when
            val result = creator.create(plainText)

            // then
            result.shouldNotBeEmpty()
            Base64.getDecoder().decode(result) // 유효한 Base64인지 확인
        }

        it("특수문자가 포함된 문자열에 대해서도 블라인드 인덱스를 생성한다") {
            // given
            val properties = EncryptionPropertiesFixture.create()
            val creator = HmacBlindIndexCreator(properties)
            val plainText = "test+special@example-domain.co.kr!@#$%"

            // when
            val result = creator.create(plainText)

            // then
            result.shouldNotBeEmpty()
            Base64.getDecoder().decode(result) // 유효한 Base64인지 확인
        }

        it("매우 긴 문자열에 대해서도 블라인드 인덱스를 생성한다") {
            // given
            val properties = EncryptionPropertiesFixture.create()
            val creator = HmacBlindIndexCreator(properties)
            val plainText = "a".repeat(1000) + "@example.com"

            // when
            val result = creator.create(plainText)

            // then
            result.shouldNotBeEmpty()
            Base64.getDecoder().decode(result) // 유효한 Base64인지 확인
        }

        it("빈 HMAC 키로 생성할 때 예외가 발생한다") {
            // given
            val properties = EncryptionPropertiesFixture.createWithEmptyHmacKey()

            // when & then
            shouldThrow<IllegalArgumentException> {
                HmacBlindIndexCreator(properties)
            }
        }

        it("생성된 블라인드 인덱스는 항상 Base64 형식이다") {
            // given
            val properties = EncryptionPropertiesFixture.create()
            val creator = HmacBlindIndexCreator(properties)
            val testCases = listOf(
                "test@example.com",
                "홍길동",
                "",
                "special!@#$%",
                "123456789"
            )

            testCases.forEach { plainText ->
                // when
                val result = creator.create(plainText)

                // then
                result.shouldNotBeEmpty()
                // Base64 디코딩이 성공하면 유효한 Base64 형식
                Base64.getDecoder().decode(result)
                // Base64 문자열은 알파벳, 숫자, +, /, = 만 포함
                result.all { it.isLetterOrDigit() || it in "+/=" } shouldBe true
            }
        }
    }
})
