package playground.vehiclerental.vehicle

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class VehicleController {

    @GetMapping("/vehicles")
    fun list(): ResponseEntity<Map<String, Any>> =
        ResponseEntity.ok(
            mapOf(
                "status" to "ok",
                "vehicles" to emptyList<String>(),
            )
        )
}
