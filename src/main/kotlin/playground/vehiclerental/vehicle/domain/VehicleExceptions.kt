package playground.vehiclerental.vehicle.domain

class InvalidPlateException(plate: String) : RuntimeException("Placa inválida: $plate")

class VehicleNotFoundException(id: Long) : RuntimeException("Veículo $id não encontrado")

class DuplicatePlateException(plate: Plate) : RuntimeException("Já existe um veículo com a placa $plate")
