package playground.vehiclerental.customer.domain

import java.time.LocalDate

enum class LicenseCategory { A, B, AB, C, D, E }

/** Driver license (CNH): 11-digit number, category and expiration date. */
class DriverLicense private constructor(
    val number: String,
    val category: LicenseCategory,
    val expiresAt: LocalDate,
) {
    /** The license is valid until the end of its expiration day. */
    fun isValidOn(date: LocalDate): Boolean = !date.isAfter(expiresAt)

    /** Only categories that allow driving cars (B, AB, C, D, E). */
    fun allowsCars(): Boolean = category != LicenseCategory.A

    companion object {
        fun of(number: String, category: LicenseCategory, expiresAt: LocalDate): DriverLicense {
            val digits = number.filter { it.isDigit() }
            if (digits.length != 11) throw InvalidLicenseException(number)
            return DriverLicense(digits, category, expiresAt)
        }
    }
}
