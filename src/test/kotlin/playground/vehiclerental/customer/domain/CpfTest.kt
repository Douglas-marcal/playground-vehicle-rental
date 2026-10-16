package playground.vehiclerental.customer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CpfTest {

    @Test
    fun `aceita cpf valido com ou sem mascara`() {
        assertEquals("52998224725", Cpf.of("529.982.247-25").value)
        assertEquals("11144477735", Cpf.of("11144477735").value)
    }

    @Test
    fun `formata o cpf para exibicao`() {
        assertEquals("529.982.247-25", Cpf.of("52998224725").formatted)
    }

    @Test
    fun `rejeita cpf invalido`() {
        listOf("", "123", "52998224726", "11111111111", "000.000.000-00", "abcdefghijk").forEach {
            assertFailsWith<InvalidCpfException>(it) { Cpf.of(it) }
        }
    }
}
