package playground.vehiclerental

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VehicleRentalApplication

fun main(args: Array<String>) {
    runApplication<VehicleRentalApplication>(*args)
}
