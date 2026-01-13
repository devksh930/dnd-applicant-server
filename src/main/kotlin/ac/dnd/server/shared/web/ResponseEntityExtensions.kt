package ac.dnd.server.shared.web

import org.springframework.http.ResponseEntity
import java.net.URI

fun Boolean.toPutResponse(location: URI): ResponseEntity<Unit> =
    if (this) ResponseEntity.created(location).build()
    else ResponseEntity.noContent().build()