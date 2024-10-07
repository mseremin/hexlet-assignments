package exercise.controller;

import java.util.List;
import java.util.Set;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.mapper.ProductMapper;
import exercise.repository.CategoryRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    // BEGIN

//    POST /products – добавление нового товара. При указании id несуществующей категории должен вернуться ответ с кодом 400 Bad request
//    PUT /products/{id} – редактирование товара. При редактировании мы должны иметь возможность поменять название, цену и категорию товара. При указании id несуществующей категории также должен вернуться ответ с кодом 400 Bad request
//    DELETE /products/{id} – удаление товара

    @GetMapping(path = "")
    public List<ProductDTO> index() {
        var products = productRepository.findAll();
        return products.stream().map(productMapper::map).toList();
    }

    @GetMapping(path = "/{id}")
    public ProductDTO index(@PathVariable long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return productMapper.map(product);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        var category = categoryRepository.findById(productCreateDTO.getCategoryId())
                .orElseThrow(() -> new ConstraintViolationException("No category with id " + productCreateDTO.getCategoryId(), null));
        var product = productMapper.map(productCreateDTO);
        product.setCategory(category);
        productRepository.save(product);
        return productMapper.map(product);
    }

    @PutMapping(path = "/{id}")
    public ProductDTO update(@Valid @RequestBody ProductUpdateDTO productUpdateDTO, @PathVariable long id) {
        var product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id " + id)
        );
        var category = categoryRepository.findById(productUpdateDTO.getCategoryId().get())
                .orElseThrow(() -> new ConstraintViolationException("No category with id " + productUpdateDTO.getCategoryId(), null));
        product.setCategory(category);
        productMapper.update(productUpdateDTO, product);
        productRepository.save(product);
        return productMapper.map(product);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }
    // END
}
