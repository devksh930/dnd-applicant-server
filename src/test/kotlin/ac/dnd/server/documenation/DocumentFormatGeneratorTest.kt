package ac.dnd.server.documenation

import ac.dnd.server.enums.ApplicantStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

/**
 * enum 타입 아스키독 목록 스니펫 사용 문자형식 생성
 */
class DocumentFormatGeneratorTest : DescribeSpec({

    describe("지정된 enum 타입을 아스키독문법 목록(<li>) 형태로 출력") {

        it("속성값만 나열함") {
            // when
            val result = DocumentFormatGenerator.generatedEnums(ApplicantStatus::class.java)

            // then
            result shouldBe """* `NONE`
* `PASSED`
* `FAILED`
* `WAITLISTED`"""
        }

        it("속성값(설명) 나열함") {
            // when
            val attrs = DocumentFormatGenerator.generateEnumAttrs(
                ApplicantStatus::class.java
            ) { it.description }

            // then
            attrs.value shouldBe """* `NONE`(미정)
* `PASSED`(합격)
* `FAILED`(불합격)
* `WAITLISTED`(예비 후보자)"""
        }
    }
})