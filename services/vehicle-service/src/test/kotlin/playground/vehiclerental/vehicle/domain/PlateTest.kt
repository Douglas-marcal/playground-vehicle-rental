package playground.vehiclerental.vehicle.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlateTest {

    @Test
    fun `aceita placa no formato antigo`() {
        assertEquals("ABC1234", Plate.of("abc-1234").value)
    }

    @Test
    fun `aceita placa Mercosul`() {
        assertEquals("ABC1D23", Plate.of("ABC1D23").value)
    }

    @Test
    fun `rejeita placa invalida`() {
        listOf("", "ABC12", "1234ABC", "ABCD123").forEach {
            assertFailsWith<InvalidPlateException>(it) { Plate.of(it) }
        }
    }
}
