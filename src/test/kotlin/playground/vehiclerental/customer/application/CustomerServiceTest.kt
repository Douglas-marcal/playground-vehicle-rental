package playground.vehiclerental.customer.application

import playground.vehiclerental.customer.domain.Cpf
import playground.vehiclerental.customer.domain.Customer
import playground.vehiclerental.customer.domain.CustomerNotFoundException
import playground.vehiclerental.customer.domain.CustomerRepository
import playground.vehiclerental.customer.domain.DuplicateCpfException
import playground.vehiclerental.customer.domain.DuplicateLicenseException
import playground.vehiclerental.customer.domain.InvalidCpfException
import playground.vehiclerental.customer.domain.LicenseCategory
import playground.vehiclerental.customer.domain.LicenseCategoryNotAllowedException
import playground.vehiclerental.customer.domain.LicenseExpiredException
import playground.vehiclerental.customer.domain.UnderageCustomerException
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CustomerServiceTest {

    private val today = LocalDate.of(2026, 10, 15)
    private val clock = Clock.fixed(Instant.parse("2026-10-15T12:00:00Z"), ZoneOffset.UTC)
    private val service = CustomerService(InMemoryCustomerRepository(), clock)

    private fun command(
        cpf: String = "529.982.247-25",
        birthDate: LocalDate = LocalDate.of(1990, 5, 20),
        license: LicenseData = LicenseData("12345678901", LicenseCategory.B, today.plusYears(3)),
    ) = RegisterCustomerCommand("Maria Silva", cpf, birthDate, "maria@email.com", "11999990000", license)

    @Test
    fun `cadastra cliente com cpf e cnh normalizados`() {
        val customer = service.register(command())

        assertEquals(1L, customer.id)
        assertEquals("52998224725", customer.cpf.value)
        assertTrue(customer.canDriveOn(today))
    }

    @Test
    fun `rejeita cpf invalido`() {
        assertFailsWith<InvalidCpfException> { service.register(command(cpf = "111.111.111-11")) }
    }

    @Test
    fun `rejeita cliente menor de 21 anos`() {
        assertFailsWith<UnderageCustomerException> {
            service.register(command(birthDate = today.minusYears(21).plusDays(1)))
        }
        service.register(command(birthDate = today.minusYears(21)))
    }

    @Test
    fun `rejeita cnh vencida mas aceita no ultimo dia de validade`() {
        val expired = LicenseData("12345678901", LicenseCategory.B, today.minusDays(1))
        assertFailsWith<LicenseExpiredException> { service.register(command(license = expired)) }

        service.register(command(license = expired.copy(expiresAt = today)))
    }

    @Test
    fun `rejeita cnh categoria A`() {
        val motorcycle = LicenseData("12345678901", LicenseCategory.A, today.plusYears(1))
        assertFailsWith<LicenseCategoryNotAllowedException> { service.register(command(license = motorcycle)) }
    }

    @Test
    fun `nao permite cpf ou cnh duplicados`() {
        service.register(command())

        assertFailsWith<DuplicateCpfException> { service.register(command()) }
        assertFailsWith<DuplicateLicenseException> { service.register(command(cpf = "111.444.777-35")) }
    }

    @Test
    fun `renova a cnh`() {
        val id = service.register(command()).id!!
        val renewed = LicenseData("12345678901", LicenseCategory.AB, today.plusYears(5))

        val customer = service.renewLicense(id, renewed)

        assertEquals(LicenseCategory.AB, customer.license.category)
        assertEquals(today.plusYears(5), customer.license.expiresAt)
    }

    @Test
    fun `nao renova com cnh vencida`() {
        val id = service.register(command()).id!!

        assertFailsWith<LicenseExpiredException> {
            service.renewLicense(id, LicenseData("12345678901", LicenseCategory.B, today.minusDays(1)))
        }
    }

    @Test
    fun `cliente com cnh vencida nao pode dirigir`() {
        val customer = service.register(command())

        assertTrue(customer.canDriveOn(today.plusYears(3)))
        assertFalse(customer.canDriveOn(today.plusYears(3).plusDays(1)))
    }

    @Test
    fun `falha ao buscar cliente inexistente`() {
        assertFailsWith<CustomerNotFoundException> { service.get(99) }
    }
}

private class InMemoryCustomerRepository : CustomerRepository {
    private val store = linkedMapOf<Long, Customer>()

    override fun save(customer: Customer): Customer {
        val id = customer.id ?: (store.size + 1L)
        return Customer(
            id, customer.name, customer.cpf, customer.birthDate, customer.email, customer.phone, customer.license,
        ).also { store[id] = it }
    }

    override fun findById(id: Long) = store[id]
    override fun findAll() = store.values.toList()
    override fun existsByCpf(cpf: Cpf) = store.values.any { it.cpf == cpf }
    override fun existsByLicenseNumber(number: String) = store.values.any { it.license.number == number }
}
