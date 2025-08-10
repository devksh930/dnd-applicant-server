package ac.dnd.server.notification

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Objects


@RestController
@RequestMapping("/google")
class GoogleFromController {

    @GetMapping("/form")
    fun googleForm(
        @RequestBody body: Map<String, Objects>
    ): Map<String, Objects> {
        println(body)
        return body;

    }
}