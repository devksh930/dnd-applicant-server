package ac.dnd.server.common.event

import org.springframework.context.ApplicationEventPublisher

object Events {
    private var publisher: ApplicationEventPublisher? = null

    fun setPublisher(publisher: ApplicationEventPublisher) {
        Events.publisher = publisher
    }

    fun raise(event: Any) {
        publisher?.publishEvent(event)
    }
}