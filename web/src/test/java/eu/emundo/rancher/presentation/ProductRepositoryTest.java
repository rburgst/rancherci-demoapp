package eu.emundo.rancher.presentation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple integration test for the repository.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository sut;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findProductByProductNameIsLike() throws Exception {
        givenProductWithName("my test product");

        final List<Product> result = sut.findProductByProductNameIsLike("%test%");

        assertThat(result).extracting("productName").contains("my test product");
    }

    @Test
    public void testThatSucceeds() {
        assertThat(2).isEqualTo(2);
    }

    private void givenProductWithName(String productName) {
        Product productWithName = new Product();

        productWithName.setProductName(productName);
        productWithName.setDescription("a test");

        entityManager.persist(productWithName);
    }

}