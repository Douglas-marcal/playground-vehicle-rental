package playground.vehiclerental.customer.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import playground.vehiclerental.customer.domain.Cpf
import playground.vehiclerental.customer.domain.Customer
import playground.vehiclerental.customer.domain.CustomerNotFoundException
import playground.vehiclerental.customer.domain.CustomerRepository
import playground.vehiclerental.customer.domain.DriverLicense
import playground.vehiclerental.customer.domain.DuplicateCpfException
import playground.vehiclerental.customer.domain.DuplicateLicenseException
import playground.vehiclerental.customer.domain.LicenseCategory
import java.time.Clock
import java.time.LocalDate

data class LicenseData(
    val number: String,
    val category: LicenseCategory,
    val expiresAt: LocalDate,
)

data class RegisterCustomerCommand(
    val name: String,
    val cpf: String,
    val birthDate: LocalDate,
    val email: String? = null,
    val phone: String? = null,
    val license: LicenseData,
)

@Service
@Transactional
class CustomerService(
    private val customers: CustomerRepository,
    private val clock: Clock,
) {

    fun register(command: RegisterCustomerCommand): Customer {
        val cpf = Cpf.of(command.cpf)
        val license = command.license.toDomain()
        if (customers.existsByCpf(cpf)) throw DuplicateCpfException(cpf)
        if (customers.existsByLicenseNumber(license.number)) throw DuplicateLicenseException(license.number)

        val customer = Customer.register(
            name = command.name,
            cpf = cpf,
            birthDate = command.birthDate,
            email = command.email,
            phone = command.phone,
            license = license,
            today = LocalDate.now(clock),
        )
        return customers.save(customer)
    }

    @Transactional(readOnly = true)
    fun get(id: Long): Customer = customers.findById(id) ?: throw CustomerNotFoundException(id)

    @Transactional(readOnly = true)
    fun list(): List<Customer> = customers.findAll()

    fun renewLicense(id: Long, data: LicenseData): Customer {
        val customer = get(id)
        val newLicense = data.toDomain()
        if (newLicense.number != customer.license.number && customers.existsByLicenseNumber(newLicense.number)) {
            throw DuplicateLicenseException(newLicense.number)
        }
        return customers.save(customer.renewLicense(newLicense, LocalDate.now(clock)))
    }

    private fun LicenseData.toDomain() = DriverLicense.of(number, category, expiresAt)
}
