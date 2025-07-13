package ac.dnd.server.common.util

import ac.dnd.server.common.util.Constant.DEFAULT_ZONE_ID
import ac.dnd.server.common.util.Constant.FORMAT_LOCAL_DATE_TIME
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object LocalDateTimeUtils {

    /**
     * LocalDateTime 변환
     *
     * @param source 변환문자열
     * @return yyyy-MM-dd'T'HH:mm:ss 형식 [LocalDateTime]
     */
    fun toLocalDateTime(source: String?): LocalDateTime? {
        return toLocalDateTime(source, FORMAT_LOCAL_DATE_TIME)
    }

    /**
     * LocalDateTime 변환
     *
     * @param source         변환문자열
     * @param dateTimeFormat pattern
     * @return pattern 에 맞춰 변환된 [LocalDateTime]
     */
    fun toLocalDateTime(source: String?, dateTimeFormat: String): LocalDateTime? {
        if (source.isNullOrEmpty()) {
            return null
        }
        return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(dateTimeFormat))
    }

    /**
     * LocalDateTime to 문자열 변환
     *
     * @param source 원천
     * @return "yyyy-MM-dd'T'HH:mm:ss" 형식으로 변환된 문자열
     */
    fun toString(source: LocalDateTime?): String? {
        return toString(source, FORMAT_LOCAL_DATE_TIME)
    }

    /**
     * LocalDateTime to 문자열 변환
     *
     * @param source         원천
     * @param dateTimeFormat pattern
     * @return pattern 형식으로 변환된 문자열
     */
    fun toString(source: LocalDateTime?, dateTimeFormat: String): String? {
        if (source == null) {
            return null
        }
        return source.format(DateTimeFormatter.ofPattern(dateTimeFormat))
    }

    /**
     * 해당일 시작시
     *
     * @param source 원천일
     * @return 원천일 00:00:00
     */
    fun ofFirst(source: LocalDate): LocalDateTime {
        return LocalDateTime.of(source, LocalTime.MIN)
    }

    /**
     * 해당일 종료시
     *
     * @param source 원천일
     * @return 원천일 23:59:59.999999999
     */
    fun ofLast(source: LocalDate): LocalDateTime {
        return LocalDateTime.of(source, LocalTime.MAX)
    }

    /**
     * 해당월 시작일 시작시
     *
     * @param source 원천일
     * @return month-01 00:00:00
     */
    fun ofMonthFirstDateTime(source: LocalDate): LocalDateTime {
        return ofFirst(LocalDateUtils.ofMonthFirstDay(source))
    }

    /**
     * 해당월 마지막날 마지막시
     *
     * @param source 원천일
     * @return month-lengthOfMonth 23:59:59.999999999
     */
    fun ofMonthLastDateTime(source: LocalDate): LocalDateTime {
        return ofLast(LocalDateUtils.ofMonthLastDay(source))
    }

    /**
     * localDateTime을 Asia/Seoul ZoneDateTime 문자열로 변환합니다.
     *
     * @return Asia/Seoul ZoneDateTime 문자열
     */
    fun convertZoneDateTimeFormat(source: LocalDateTime?): String? {
        if (source == null) {
            return null
        }
        return source.atZone(DEFAULT_ZONE_ID)
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}
