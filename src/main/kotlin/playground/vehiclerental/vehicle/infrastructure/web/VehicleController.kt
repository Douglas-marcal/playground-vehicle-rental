package playground.vehiclerental.vehicle.infrastructure.web

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import playground.vehiclerental.vehicle.application.RegisterVehicleCommand
import playground.vehiclerental.vehicle.application.VehicleService
import playground.vehiclerental.vehicle.domain.Vehicle
import playground.vehiclerental.vehicle.domain.VehicleCategory
import playground.vehiclerental.vehicle.domain.VehicleStatus

data class RegisterVehicleRequest(
    val plate: String,
    val category: VehicleCategory,
    val mileageKm: Int = 0,
)

data class ChangeStatusRequest(val status: VehicleStatus)

data class VehicleResponse(
    val id: Long,
    val plate: String,
    val category: VehicleCategory,
    val status: VehicleStatus,
    val mileageKm: Int,
) {
    companion object {
        fun from(vehicle: Vehicle) = VehicleResponse(
            id = requireNotNull(vehicle.id),
            plate = vehicle.plate.value,
            category = vehicle.category,
            status = vehicle.status,
            mileageKm = vehicle.mileageKm,
        )
    }
}

@RestController
@RequestMapping("/vehicles")
class VehicleController(private val service: VehicleService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody request: RegisterVehicleRequest): VehicleResponse =
        VehicleResponse.from(
            service.register(RegisterVehicleCommand(request.plate, request.category, request.mileageKm))
        )

    @GetMapping
    fun list(): List<VehicleResponse> = service.list().map(VehicleResponse::from)

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): VehicleResponse = VehicleResponse.from(service.get(id))

    @PatchMapping("/{id}/status")
    fun changeStatus(@PathVariable id: Long, @RequestBody request: ChangeStatusRequest): VehicleResponse =
        VehicleResponse.from(service.changeStatus(id, request.status))
}
