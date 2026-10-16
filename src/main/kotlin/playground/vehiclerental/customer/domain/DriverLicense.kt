package playground.vehiclerental.customer.domain

import java.time.LocalDate

enum class LicenseCategory { A, B, AB, C, D, E }

/** CNH: número de 11 dígitos, categoria e validade. */
class DriverLicense private constructor(
    val number: String,
    val category: LicenseCategory,
    val expiresAt: LocalDate,
) {
    /** A CNH vale até o fim do dia de validade. */
    fun isValidOn(date: LocalDate): Boolean = !date.isAfter(expiresAt)

    /** Só categorias que habilitam a dirigir carros (B, AB, C, D, E). */
    fun allowsCars(): Boolean = category != LicenseCategory.A

    companion object {
        fun of(number: String, category: LicenseCategory, expiresAt: LocalDate): DriverLicense {
            val digits = number.filter { it.isDigit() }
            if (digits.length != 11) throw InvalidLicenseException(number)
            return DriverLicense(digits, category, expiresAt)
        }
    }
}
