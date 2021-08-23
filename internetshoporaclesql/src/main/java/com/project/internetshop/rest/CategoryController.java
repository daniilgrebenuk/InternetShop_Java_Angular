package com.project.internetshop.rest;

import com.project.internetshop.model.Category;
import com.project.internetshop.model.Order;
import com.project.internetshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryController(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }


  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id){
    return ResponseEntity.ok(categoryRepository.findById(id).get());
  }

  @GetMapping("/all")
  public ResponseEntity<List<Category>> getAllCategory(){
    return ResponseEntity.ok(categoryRepository.findAllActiveOrderByName());
  }

  @GetMapping("/updatable/all")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<List<Category>> getAllCategoryForPersonal(){
    return ResponseEntity.ok(categoryRepository.findAllByOrderByNameAsc());
  }

  @PostMapping("/updatable/add")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Category> addNewCategory(@RequestBody Category category){
    Category newCategory = categoryRepository.save(category);
    return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
  }


  @PutMapping("/updatable/update")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Category> updateCategory(@RequestBody Category category){
    Category newCategory = categoryRepository.save(category);
    return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
  }
}
