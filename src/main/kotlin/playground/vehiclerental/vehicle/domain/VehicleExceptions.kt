package playground.vehiclerental.vehicle.domain

import playground.vehiclerental.shared.domain.ConflictException
import playground.vehiclerental.shared.domain.NotFoundException
import playground.vehiclerental.shared.domain.ValidationException

class InvalidPlateException(plate: String) : ValidationException("Placa inválida: $plate")

class VehicleNotFoundException(id: Long) : NotFoundException("Veículo $id não encontrado")

class DuplicatePlateException(plate: Plate) : ConflictException("Já existe um veículo com a placa $plate")
