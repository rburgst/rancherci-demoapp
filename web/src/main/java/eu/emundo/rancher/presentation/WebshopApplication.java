package eu.emundo.rancher.presentation;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
@SpringBootApplication
public class WebshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebshopApplication.class, args);
    }
}

@Entity
@Data
@NoArgsConstructor
class Product implements Serializable {
    @GeneratedValue
    @Id
    Long id;

    String productName;

    String description;
}

@Repository
@RepositoryRestResource
interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    List<Product> findProductByProductNameIsLike(String productName);
}

@Controller
@Transactional(readOnly = true)
class ProductController {

    private final ProductRepository repository;

    ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = false)
    @PostMapping("/addRandomProduct")
    public String addRandomProduct() {
        Product product = new Product();
        product.setProductName("Product " + UUID.randomUUID());
        product.setDescription("randomly generated product");
        repository.save(product);
        return "redirect:/";
    }

    @GetMapping("/")
    public ModelAndView list() {
        ModelAndView result = new ModelAndView("list");
        // usually you would always page the result
        final Iterable<Product> products = repository.findAll(new PageRequest(0, 20));
        result.addObject("products", products);
        return result;
    }
}