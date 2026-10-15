package playground.vehiclerental.vehicle.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import playground.vehiclerental.vehicle.domain.DuplicatePlateException
import playground.vehiclerental.vehicle.domain.Plate
import playground.vehiclerental.vehicle.domain.Vehicle
import playground.vehiclerental.vehicle.domain.VehicleCategory
import playground.vehiclerental.vehicle.domain.VehicleNotFoundException
import playground.vehiclerental.vehicle.domain.VehicleRepository
import playground.vehiclerental.vehicle.domain.VehicleStatus

data class RegisterVehicleCommand(
    val plate: String,
    val category: VehicleCategory,
    val mileageKm: Int = 0,
)

@Service
@Transactional
class VehicleService(private val vehicles: VehicleRepository) {

    fun register(command: RegisterVehicleCommand): Vehicle {
        val plate = Plate.of(command.plate)
        if (vehicles.existsByPlate(plate)) throw DuplicatePlateException(plate)
        return vehicles.save(Vehicle(plate = plate, category = command.category, mileageKm = command.mileageKm))
    }

    @Transactional(readOnly = true)
    fun get(id: Long): Vehicle = vehicles.findById(id) ?: throw VehicleNotFoundException(id)

    @Transactional(readOnly = true)
    fun list(): List<Vehicle> = vehicles.findAll()

    fun changeStatus(id: Long, status: VehicleStatus): Vehicle =
        vehicles.save(get(id).withStatus(status))
}
