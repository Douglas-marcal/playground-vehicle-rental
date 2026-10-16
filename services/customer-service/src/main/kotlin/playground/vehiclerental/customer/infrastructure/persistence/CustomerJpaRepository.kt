package playground.vehiclerental.customer.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import playground.vehiclerental.customer.domain.Cpf
import playground.vehiclerental.customer.domain.Customer
import playground.vehiclerental.customer.domain.CustomerRepository

interface SpringDataCustomerRepository : JpaRepository<CustomerEntity, Long> {
    fun existsByCpf(cpf: String): Boolean
    fun existsByLicenseNumber(licenseNumber: String): Boolean
}

@Component
class CustomerJpaRepository(private val jpa: SpringDataCustomerRepository) : CustomerRepository {

    override fun save(customer: Customer): Customer = jpa.save(CustomerEntity.from(customer)).toDomain()

    override fun findById(id: Long): Customer? = jpa.findById(id).map { it.toDomain() }.orElse(null)

    override fun findAll(): List<Customer> = jpa.findAll().map { it.toDomain() }

    override fun existsByCpf(cpf: Cpf): Boolean = jpa.existsByCpf(cpf.value)

    override fun existsByLicenseNumber(number: String): Boolean = jpa.existsByLicenseNumber(number)
}
