package com.project.internetshop.service;

import com.project.internetshop.model.Product;
import com.project.internetshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
  private ProductRepository productRepository;

  @Autowired
  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product findById(Long id){
    return productRepository.findById(id).orElse(null);
  }

  public List<Product> findAllByCategory_IdOrderByName(Long id) {
    return productRepository.findAllByCategory_IdOrderByName(id);
  }

  public List<Product> findAllByOrder_id(Long id) {
    return productRepository.findAllByOrder_id(id);
  }

  public Product save(Product product) {
    return productRepository.save(product);
  }

  public List<Product> findAllByOrderByNameAsc() {
    return productRepository.findAllByOrderByNameAsc();
  }

  public List<Product> findAllByNameContainsOrderByName(String name) {
    return productRepository.findAllByOrderByNameAsc()
        .stream()
        .filter(p->p.getName().toLowerCase().contains(name.toLowerCase()))
        .toList();
  }
}
