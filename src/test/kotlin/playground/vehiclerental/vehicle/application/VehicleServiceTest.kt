package playground.vehiclerental.vehicle.application

import playground.vehiclerental.vehicle.domain.DuplicatePlateException
import playground.vehiclerental.vehicle.domain.Plate
import playground.vehiclerental.vehicle.domain.Vehicle
import playground.vehiclerental.vehicle.domain.VehicleCategory
import playground.vehiclerental.vehicle.domain.VehicleNotFoundException
import playground.vehiclerental.vehicle.domain.VehicleRepository
import playground.vehiclerental.vehicle.domain.VehicleStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class VehicleServiceTest {

    private val service = VehicleService(InMemoryVehicleRepository())

    @Test
    fun `cadastra veiculo disponivel`() {
        val vehicle = service.register(RegisterVehicleCommand("ABC1D23", VehicleCategory.SUV, 1500))

        assertEquals(1L, vehicle.id)
        assertEquals(VehicleStatus.AVAILABLE, vehicle.status)
        assertEquals(1500, vehicle.mileageKm)
    }

    @Test
    fun `nao permite placa duplicada`() {
        service.register(RegisterVehicleCommand("ABC1D23", VehicleCategory.SUV))

        assertFailsWith<DuplicatePlateException> {
            service.register(RegisterVehicleCommand("abc-1d23", VehicleCategory.ECONOMY))
        }
    }

    @Test
    fun `muda o status do veiculo`() {
        val id = service.register(RegisterVehicleCommand("ABC1234", VehicleCategory.COMPACT)).id!!

        assertEquals(VehicleStatus.MAINTENANCE, service.changeStatus(id, VehicleStatus.MAINTENANCE).status)
    }

    @Test
    fun `falha ao buscar veiculo inexistente`() {
        assertFailsWith<VehicleNotFoundException> { service.get(99) }
    }
}

private class InMemoryVehicleRepository : VehicleRepository {
    private val store = linkedMapOf<Long, Vehicle>()

    override fun save(vehicle: Vehicle): Vehicle {
        val id = vehicle.id ?: (store.size + 1L)
        return Vehicle(id, vehicle.plate, vehicle.category, vehicle.status, vehicle.mileageKm)
            .also { store[id] = it }
    }

    override fun findById(id: Long) = store[id]
    override fun findAll() = store.values.toList()
    override fun existsByPlate(plate: Plate) = store.values.any { it.plate == plate }
}
