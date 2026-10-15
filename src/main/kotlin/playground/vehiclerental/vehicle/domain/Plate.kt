package playground.vehiclerental.vehicle.domain

/** Placa brasileira, no formato antigo (ABC1234) ou Mercosul (ABC1D23). */
@JvmInline
value class Plate private constructor(val value: String) {

    companion object {
        private val FORMAT = Regex("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$")

        fun of(raw: String): Plate {
            val normalized = raw.filter { it.isLetterOrDigit() }.uppercase()
            if (!FORMAT.matches(normalized)) throw InvalidPlateException(raw)
            return Plate(normalized)
        }
    }

    override fun toString() = value
}
