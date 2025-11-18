package ac.dnd.server.shared.event

import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationEventPublisherAware
import org.springframework.stereotype.Component

@Component
object EventDispatcher : ApplicationEventPublisherAware {

    private lateinit var publisher: ApplicationEventPublisher

    override fun setApplicationEventPublisher(publisher: ApplicationEventPublisher) {
        EventDispatcher.publisher = publisher
    }

    fun publish(event: Any) {
        publisher.publishEvent(event)
    }
}