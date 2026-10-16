package playground.vehiclerental.customer.domain

import java.time.LocalDate
import java.time.Period

class Customer(
    val id: Long? = null,
    val name: String,
    val cpf: Cpf,
    val birthDate: LocalDate,
    val email: String? = null,
    val phone: String? = null,
    val license: DriverLicense,
) {
    init {
        require(name.isNotBlank()) { "O nome é obrigatório" }
        require(email == null || EMAIL_FORMAT.matches(email)) { "E-mail inválido: $email" }
    }

    /** Indica se o cliente pode conduzir na data informada, regra que a locação vai usar. */
    fun canDriveOn(date: LocalDate): Boolean = license.isValidOn(date) && license.allowsCars()

    fun renewLicense(newLicense: DriverLicense, today: LocalDate): Customer {
        if (!newLicense.isValidOn(today)) throw LicenseExpiredException(newLicense.expiresAt)
        return Customer(id, name, cpf, birthDate, email, phone, newLicense)
    }

    companion object {
        const val MINIMUM_AGE = 21
        private val EMAIL_FORMAT = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

        /** Cadastra um cliente validando idade mínima e CNH em vigor na data de [today]. */
        fun register(
            name: String,
            cpf: Cpf,
            birthDate: LocalDate,
            email: String?,
            phone: String?,
            license: DriverLicense,
            today: LocalDate,
        ): Customer {
            if (Period.between(birthDate, today).years < MINIMUM_AGE) throw UnderageCustomerException(MINIMUM_AGE)
            if (!license.isValidOn(today)) throw LicenseExpiredException(license.expiresAt)
            if (!license.allowsCars()) throw LicenseCategoryNotAllowedException(license.category)
            return Customer(null, name.trim(), cpf, birthDate, email?.trim(), phone?.trim(), license)
        }
    }
}
