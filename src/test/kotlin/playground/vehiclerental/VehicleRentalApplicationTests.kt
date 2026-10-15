package playground.vehiclerental

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
import kotlin.test.assertEquals

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class VehicleRentalApplicationTests {

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val mysql = MySQLContainer("mysql:8.4")
    }

    @Autowired
    lateinit var rest: TestRestTemplate

    @Test
    fun `aplica as migracoes e cadastra um veiculo`() {
        val created = rest.postForEntity(
            "/vehicles",
            mapOf("plate" to "ABC1D23", "category" to "SUV", "mileageKm" to 10),
            Map::class.java,
        )
        assertEquals(HttpStatus.CREATED, created.statusCode)
        assertEquals("ABC1D23", created.body?.get("plate"))

        val duplicate = rest.postForEntity(
            "/vehicles",
            mapOf("plate" to "ABC-1D23", "category" to "ECONOMY"),
            Map::class.java,
        )
        assertEquals(HttpStatus.CONFLICT, duplicate.statusCode)
    }
}
