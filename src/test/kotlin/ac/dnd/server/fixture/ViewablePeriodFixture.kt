package ac.dnd.server.fixture

import ac.dnd.server.admission.domain.model.ViewablePeriod
import java.time.LocalDateTime

object ViewablePeriodFixture {
    
    fun create(): ViewablePeriod {
        val now = LocalDateTime.now()
        return ViewablePeriod.of(
            now.minusDays(10),
            now.plusDays(10)
        )
    }
    
    fun createViewable(now: LocalDateTime): ViewablePeriod {
        return ViewablePeriod.of(
            now.minusDays(10),
            now.plusDays(10)
        )
    }
    
    fun createWithDates(startDate: LocalDateTime, endDate: LocalDateTime): ViewablePeriod {
        return ViewablePeriod.of(startDate, endDate)
    }
    
    fun createStandard(): ViewablePeriod {
        return ViewablePeriod.of(
            LocalDateTime.of(2024, 1, 1, 0, 0),
            LocalDateTime.of(2024, 1, 31, 23, 59)
        )
    }
    
    fun createSameDateTime(): ViewablePeriod {
        val dateTime = LocalDateTime.of(2024, 1, 1, 0, 0)
        return ViewablePeriod.of(dateTime, dateTime)
    }
    
    fun createForTesting(now: LocalDateTime): ViewablePeriod {
        return ViewablePeriod.of(
            now.minusHours(1),
            now.plusHours(1)
        )
    }
}