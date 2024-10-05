package exercise.controller;

import exercise.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN

    @Autowired
    ProductMapper productMapper;

    @GetMapping(path = "")
    public List<ProductDTO> products() {
        var products = productRepository.findAll();
        return products.stream().map(it -> productMapper.map(it)).collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ProductDTO product(@PathVariable long id) {
        var product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id = " + id)
        );
        return productMapper.map(product);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public ProductDTO create(@RequestBody ProductCreateDTO productData) {
        var product = productMapper.map(productData);
        productRepository.save(product);
        return productMapper.map(product);
    }

    @PutMapping(path = "/{id}")
    public ProductDTO update(@RequestBody ProductUpdateDTO productData, @PathVariable long id) {
        var product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id = " + id)
        );
        productMapper.update(productData, product);
        productRepository.save(product);
        return productMapper.map(product);
    }
    // END
}
