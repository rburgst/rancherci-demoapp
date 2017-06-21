package demo

import eu.emundo.rancher.presentation.WebshopApplication
import geb.spock.GebReportingSpec

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

/**
 * Base class that correctly configures the port of the server.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = WebshopApplication)
class BaseGebSpec extends GebReportingSpec {
    @Value('${local.server.port}')
    int port

    def setup() {
        browser.setBaseUrl("http://localhost:${port}")
    }
}