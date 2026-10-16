package playground.vehiclerental.customer.domain

import playground.vehiclerental.shared.domain.BusinessRuleException
import playground.vehiclerental.shared.domain.ConflictException
import playground.vehiclerental.shared.domain.NotFoundException
import playground.vehiclerental.shared.domain.ValidationException
import java.time.LocalDate

class InvalidCpfException(cpf: String) : ValidationException("CPF inválido: $cpf")

class InvalidLicenseException(number: String) : ValidationException("Número de CNH inválido: $number")

class CustomerNotFoundException(id: Long) : NotFoundException("Cliente $id não encontrado")

class DuplicateCpfException(cpf: Cpf) : ConflictException("Já existe um cliente com o CPF ${cpf.formatted}")

class DuplicateLicenseException(number: String) : ConflictException("Já existe um cliente com a CNH $number")

class UnderageCustomerException(minimumAge: Int) :
    BusinessRuleException("O cliente precisa ter pelo menos $minimumAge anos")

class LicenseExpiredException(expiresAt: LocalDate) : BusinessRuleException("CNH vencida em $expiresAt")

class LicenseCategoryNotAllowedException(category: LicenseCategory) :
    BusinessRuleException("A categoria $category não habilita a dirigir carros")
