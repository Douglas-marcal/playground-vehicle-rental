package playground.vehiclerental.customer.domain

/** CPF with both check digits validated. Stores only the 11 digits. */
@JvmInline
value class Cpf private constructor(val value: String) {

    /** Display format: 123.456.789-09 */
    val formatted: String
        get() = "${value.substring(0, 3)}.${value.substring(3, 6)}.${value.substring(6, 9)}-${value.substring(9)}"

    companion object {
        fun of(raw: String): Cpf {
            val digits = raw.filter { it.isDigit() }
            if (digits.length != 11 || digits.all { it == digits[0] } || !hasValidCheckDigits(digits)) {
                throw InvalidCpfException(raw)
            }
            return Cpf(digits)
        }

        private fun hasValidCheckDigits(digits: String): Boolean {
            val first = checkDigit(digits.take(9), 10)
            val second = checkDigit(digits.take(9) + first, 11)
            return digits[9].digitToInt() == first && digits[10].digitToInt() == second
        }

        private fun checkDigit(base: String, initialWeight: Int): Int {
            val sum = base.mapIndexed { i, c -> c.digitToInt() * (initialWeight - i) }.sum()
            val remainder = sum % 11
            return if (remainder < 2) 0 else 11 - remainder
        }
    }

    override fun toString() = value
}
