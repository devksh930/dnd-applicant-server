package ac.dnd.server.common.util

import ac.dnd.server.common.util.Constant.FORMAT_LOCAL_DATE
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateUtils {
    const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"
    const val FIRST_DAY_OF_MONTH = 1

    /**
     * [LocalDate] 변환
     *
     * @param source 원천 문자열
     * @return "yyyy-MM-dd" 형식으로 변환된 [LocalDate]
     */
    fun toLocalDate(source: String?): LocalDate? {
        return toLocalDate(source, FORMAT_LOCAL_DATE)
    }

    /**
     * [LocalDate] 변환
     *
     * @param source     원천문자열
     * @param dateFormat 변환날짜형식
     * @return 지정형식으로 변환된 [LocalDate]
     */
    fun toLocalDate(source: String?, dateFormat: String): LocalDate? {
        if (source.isNullOrEmpty()) {
            return null
        }
        return LocalDate.parse(source, DateTimeFormatter.ofPattern(dateFormat))
    }

    /**
     * 문자열 변환
     *
     * @param source 원천일
     * @return yyyy-MM-dd 형식 문자열
     */
    fun toString(source: LocalDate?): String? {
        if (source == null) {
            return null
        }
        return source.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT))
    }

    /**
     * 문자열 변환
     *
     * @param source     원천일
     * @param dateFormat 변환날짜형식
     * @return '변환날짜형식'으로 변환된 문자열
     */
    fun toString(source: LocalDate, dateFormat: String): String {
        return source.format(DateTimeFormatter.ofPattern(dateFormat))
    }

    /**
     * 해당월 첫번째 날
     *
     * @param source 원천일
     * @return 1 of source
     */
    fun ofMonthFirstDay(source: LocalDate): LocalDate {
        return source.withDayOfMonth(FIRST_DAY_OF_MONTH)
    }

    /**
     * 해당월 마지막 날
     *
     * @param source 원천일
     * @return lengthOfMonth of source
     */
    fun ofMonthLastDay(source: LocalDate): LocalDate {
        return source.withDayOfMonth(source.lengthOfMonth())
    }
}