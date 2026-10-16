package playground.vehiclerental.vehicle.domain

enum class VehicleCategory { ECONOMY, COMPACT, SEDAN, SUV, VAN, LUXURY }

enum class VehicleStatus { AVAILABLE, RENTED, MAINTENANCE }

class Vehicle(
    val id: Long? = null,
    val plate: Plate,
    val category: VehicleCategory,
    val status: VehicleStatus = VehicleStatus.AVAILABLE,
    val mileageKm: Int = 0,
) {
    init {
        require(mileageKm >= 0) { "A quilometragem não pode ser negativa" }
    }

    fun withStatus(newStatus: VehicleStatus) = copy(status = newStatus)

    private fun copy(status: VehicleStatus = this.status) =
        Vehicle(id, plate, category, status, mileageKm)
}
