package playground.vehiclerental.vehicle.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import playground.vehiclerental.vehicle.domain.Plate
import playground.vehiclerental.vehicle.domain.Vehicle
import playground.vehiclerental.vehicle.domain.VehicleRepository

interface SpringDataVehicleRepository : JpaRepository<VehicleEntity, Long> {
    fun existsByPlate(plate: String): Boolean
}

@Component
class VehicleJpaRepository(private val jpa: SpringDataVehicleRepository) : VehicleRepository {

    override fun save(vehicle: Vehicle): Vehicle = jpa.save(VehicleEntity.from(vehicle)).toDomain()

    override fun findById(id: Long): Vehicle? = jpa.findById(id).map { it.toDomain() }.orElse(null)

    override fun findAll(): List<Vehicle> = jpa.findAll().map { it.toDomain() }

    override fun existsByPlate(plate: Plate): Boolean = jpa.existsByPlate(plate.value)
}
