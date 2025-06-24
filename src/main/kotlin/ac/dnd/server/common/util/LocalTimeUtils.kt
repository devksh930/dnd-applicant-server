package ac.dnd.server.common.util

import ac.dnd.server.common.util.Constant.FORMAT_LOCAL_TIME
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object LocalTimeUtils {

    /**
     * [LocalTime] 변환
     *
     * @param source 원천문자열
     * @return "HH:mm:ss" 형식으로 변환된 [LocalTime]
     */
    fun toLocalTime(source: String?): LocalTime? {
        return toLocalTime(source, FORMAT_LOCAL_TIME)
    }

    /**
     * [LocalTime] 변환
     *
     * @param source     원천문자열
     * @param timeFormat 변환시간형식
     * @return null or "HH:mm:ss" 형식으로 변환된 [LocalTime]
     */
    fun toLocalTime(source: String?, timeFormat: String): LocalTime? {
        if (source.isNullOrEmpty()) {
            return null
        }
        return LocalTime.parse(source, DateTimeFormatter.ofPattern(timeFormat))
    }

    /**
     * 문자열 변환
     *
     * @param source 원천
     * @return "HH:mm:ss" 형식으로 변환된 문자열
     */
    fun toString(source: LocalTime?): String? {
        return toString(source, FORMAT_LOCAL_TIME)
    }

    /**
     * 문자열 변환
     *
     * @param source     원천
     * @param timeFormat 변환시간형식
     * @return '변환시간형식'으로 변환된 문자열 or null
     */
    fun toString(source: LocalTime?, timeFormat: String): String? {
        if (source == null) {
            return null
        }
        return source.format(DateTimeFormatter.ofPattern(timeFormat))
    }
}