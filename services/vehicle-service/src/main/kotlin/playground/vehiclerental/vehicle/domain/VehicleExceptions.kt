package playground.vehiclerental.vehicle.domain

import playground.commons.exception.ConflictException
import playground.commons.exception.NotFoundException
import playground.commons.exception.ValidationException

class InvalidPlateException(plate: String) : ValidationException("Placa inválida: $plate")

class VehicleNotFoundException(id: Long) : NotFoundException("Veículo $id não encontrado")

class DuplicatePlateException(plate: Plate) : ConflictException("Já existe um veículo com a placa $plate")
