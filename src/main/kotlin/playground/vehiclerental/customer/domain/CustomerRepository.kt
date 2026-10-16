package playground.vehiclerental.customer.domain

interface CustomerRepository {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Customer?
    fun findAll(): List<Customer>
    fun existsByCpf(cpf: Cpf): Boolean
    fun existsByLicenseNumber(number: String): Boolean
}
