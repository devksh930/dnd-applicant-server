package ac.dnd.server.common.util

import java.time.ZoneId

/**
 * 애플리케이션에서 공통으로 사용되는 불변변수를 선언하는 목적으로 사용합니다.
 */
object Constant {
    const val FORMAT_LOCAL_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss"
    const val FORMAT_LOCAL_DATE = "yyyy-MM-dd"
    const val FORMAT_LOCAL_TIME = "HH:mm:ss"

    val DEFAULT_ZONE_ID: ZoneId = ZoneId.of("Asia/Seoul")
}
