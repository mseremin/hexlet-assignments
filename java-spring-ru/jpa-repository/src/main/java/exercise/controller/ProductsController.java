package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping(path = "")
    public List<Product> get(
            @RequestParam(name = "min", required = false) String minPrice,
            @RequestParam(name = "max", required = false) String maxPrice
    ) {
        if (maxPrice == null && minPrice == null) {
            return productRepository.findAll(Sort.by(Sort.Order.asc("price")));
        }
        if (maxPrice != null && minPrice == null) {
            return productRepository.findAllByPriceLessThanEqualOrderByPrice(Integer.parseInt(maxPrice));
        }
        if (minPrice != null && maxPrice == null) {
            return productRepository.findAllByPriceGreaterThanEqualOrderByPrice(Integer.parseInt(minPrice));
        }
        return productRepository.findAllByPriceBetweenOrderByPrice(Integer.parseInt(minPrice), Integer.parseInt(maxPrice));
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
