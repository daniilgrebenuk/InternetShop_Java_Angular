package com.project.internetshop.rest;

import com.project.internetshop.model.Product;
import com.project.internetshop.repository.ProductRepository;
import com.project.internetshop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable("id") Long id){
    return ResponseEntity.ok(productService.findById(id));
  }

  @GetMapping("/category/{id}")
  public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable Long id){
    return ResponseEntity.ok(productService.findAllByCategory_IdOrderByName(id));
  }

  @GetMapping("/order/{id}")
  public ResponseEntity<List<Product>> getProductsByOrderId(@PathVariable Long id){
    return ResponseEntity.ok(productService.findAllByOrder_id(id));
  }

  @GetMapping("/all")
  public ResponseEntity<List<Product>> getAllProducts(){
    return ResponseEntity.ok(productService.findAllByOrderByNameAsc());
  }

  @GetMapping("/search/{name}")
  public ResponseEntity<List<Product>> getProductByNameContains(@PathVariable("name") String name){
    return ResponseEntity.ok(productService.findAllByNameContainsOrderByName(name));
  }

  @PostMapping("/updatable/add")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Product> addNewProduct(@RequestBody Product product){
    if (product.getCategory() == null) {
      return new ResponseEntity<>(product, HttpStatus.BAD_REQUEST);
    }
    Product newProduct = productService.save(product);
    return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
  }

  @PutMapping("/updatable/update")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Product> updateProduct(@RequestBody Product product){
    if (product.getCategory() == null) {
      return new ResponseEntity<>(product, HttpStatus.BAD_REQUEST);
    }
    Product newProduct = productService.save(product);
    return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
  }
}
