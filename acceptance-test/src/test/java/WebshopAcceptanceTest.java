import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Acceptance test for the REST API using RestAssured.
 */
public class WebshopAcceptanceTest {
    private static String serverAddress = "http://localhost:8080";

    @BeforeClass
    public static void beforeClass() {
        final String overrideAcceptanceUrl = System.getProperty("ACCEPTANCE_TEST_URL");
        if (StringUtils.isNotBlank(overrideAcceptanceUrl)) {
            serverAddress = overrideAcceptanceUrl;
        }
        System.out.println("WebshopAcceptanceTest.beforeClass using Server: " + serverAddress);
        RestAssured.baseURI = serverAddress;

        waitUntilServerIsHealthy();
    }

    @Test
    public void testAddListDelete() {
        System.out.println("checking initial product count");
        final int originalCount = givenHaveOriginalProductCount();
        System.out.println("   ... initial product count: " + originalCount);

        String productUrl = whenAddOneProduct();
        thenShouldHaveProductCount(originalCount + 1);

        whenDelete(productUrl);
        thenShouldHaveProductCount(originalCount);
    }

    private static void waitUntilServerIsHealthy() {
        System.out.println("WebshopAcceptanceTest.waitUntilServerIsHealthy");
        // @formatter:off
        await()
                .atMost(120, TimeUnit.SECONDS)
                .pollDelay(5, TimeUnit.SECONDS)
                .until(() -> serverIsHealthy());
        // @formatter:on
    }

    private static Boolean serverIsHealthy() {
        System.out.println("  checking");
        try {
            // @formatter:off
            given()
                    .contentType(ContentType.JSON)
            .when()
                    .get("health")
            .then()
                    .body("status", equalTo("UP"));

            System.out.println("   ... SUCCESS");

            return true;
            // @formatter:on
        } catch (Throwable throwable) {
            System.out.println("    ... exception: " + throwable.getLocalizedMessage());
            return false;
        }
    }

    private int givenHaveOriginalProductCount() {
        return getProductCount();
    }

    private Product givenProduct() {
        return new Product("product " + UUID.randomUUID(), "product inserted by acceptance test");
    }

    private String whenAddOneProduct() {
        System.out.println("adding one product ");
        Product product = givenProduct();
        // @formatter:off
        final String newProductUrl =
                given()
                        .body(product)
                        .contentType(ContentType.JSON)
                .when()
                        .contentType(ContentType.JSON)
                        .post("/products")
                .then()
                        .statusCode(SC_CREATED)
                        .extract()
                            .jsonPath()
                                .get("_links.self.href");
        // @formatter:on
        System.out.println("  ... done, new product URL: " + newProductUrl);
        return newProductUrl;
    }

    private void whenDelete(String productUrl) {
        System.out.println("deleting product " + productUrl);
        //@formatter:off
        when()
                .delete(URI.create(productUrl))
        .then()
                .statusCode(SC_NO_CONTENT)
        ;
        //@formatter:on
        System.out.println("  ... done");
    }

    private void thenShouldHaveProductCount(int expectedProductCount) {
        int newProductCount = getProductCount();
        assertThat(newProductCount).isEqualTo(expectedProductCount);
    }

    private int getProductCount() {
        // @formatter:off
        final ExtractableResponse<Response> response =
                given()
                        .contentType(ContentType.JSON)
                .when()
                        .get("/products")
                .then()
                        .statusCode(200)
                        .extract();
        // @formatter:on

        final List<Map<String, Object>> products = response.body().jsonPath().get("_embedded.products");
        return products.size();
    }
}
