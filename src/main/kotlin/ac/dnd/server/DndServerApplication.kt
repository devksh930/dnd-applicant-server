package ac.dnd.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DndServerApplication

fun main(args: Array<String>) {
    runApplication<DndServerApplication>(*args)
}