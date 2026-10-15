package playground.vehiclerental.vehicle.domain

/** Porta de saída: o domínio define o contrato, a infraestrutura implementa. */
interface VehicleRepository {
    fun save(vehicle: Vehicle): Vehicle
    fun findById(id: Long): Vehicle?
    fun findAll(): List<Vehicle>
    fun existsByPlate(plate: Plate): Boolean
}
