package playground.vehiclerental.vehicle.domain

/** Output port: the domain defines the contract and the infrastructure implements it. */
interface VehicleRepository {
    fun save(vehicle: Vehicle): Vehicle
    fun findById(id: Long): Vehicle?
    fun findAll(): List<Vehicle>
    fun existsByPlate(plate: Plate): Boolean
}
