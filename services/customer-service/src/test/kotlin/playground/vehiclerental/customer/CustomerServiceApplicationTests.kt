package playground.vehiclerental.customer

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.HttpStatus
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.mysql.MySQLContainer
import java.time.LocalDate
import kotlin.test.assertEquals

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class CustomerServiceApplicationTests {

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val mysql = MySQLContainer("mysql:8.4")
    }

    @Autowired
    lateinit var rest: TestRestTemplate

    private fun request(cpf: String, licenseNumber: String) = mapOf(
        "name" to "Maria Silva",
        "cpf" to cpf,
        "birthDate" to "1990-05-20",
        "license" to mapOf(
            "number" to licenseNumber,
            "category" to "B",
            "expiresAt" to LocalDate.now().plusYears(3).toString(),
        ),
    )

    @Test
    fun `aplica as migracoes e cadastra um cliente`() {
        val created = rest.postForEntity("/customers", request("529.982.247-25", "12345678901"), Map::class.java)
        assertEquals(HttpStatus.CREATED, created.statusCode)
        assertEquals("52998224725", created.body?.get("cpf"))

        val duplicate = rest.postForEntity("/customers", request("52998224725", "99999999999"), Map::class.java)
        assertEquals(HttpStatus.CONFLICT, duplicate.statusCode)
    }
}
