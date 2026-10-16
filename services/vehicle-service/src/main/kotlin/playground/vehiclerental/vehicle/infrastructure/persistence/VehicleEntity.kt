package playground.vehiclerental.vehicle.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import playground.vehiclerental.vehicle.domain.Plate
import playground.vehiclerental.vehicle.domain.Vehicle
import playground.vehiclerental.vehicle.domain.VehicleCategory
import playground.vehiclerental.vehicle.domain.VehicleStatus

@Entity
@Table(name = "vehicle")
class VehicleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 7, unique = true)
    var plate: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var category: VehicleCategory,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: VehicleStatus,

    @Column(name = "mileage_km", nullable = false)
    var mileageKm: Int,
) {
    fun toDomain() = Vehicle(id, Plate.of(plate), category, status, mileageKm)

    companion object {
        fun from(vehicle: Vehicle) = VehicleEntity(
            id = vehicle.id,
            plate = vehicle.plate.value,
            category = vehicle.category,
            status = vehicle.status,
            mileageKm = vehicle.mileageKm,
        )
    }
}
