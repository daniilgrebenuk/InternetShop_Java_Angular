package com.project.internetshop.repository;

import com.project.internetshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  String FIND_USED_AT_LEAST_ONCE = "SELECT DISTINCT c.* " +
                                  "FROM category c JOIN product p ON c.id = p.category_id " +
                                  "ORDER BY c.name";

  @Query(value = FIND_USED_AT_LEAST_ONCE, nativeQuery = true)
  List<Category> findAllActiveOrderByName();

  List<Category> findAllByOrderByNameDesc();
  List<Category> findAllByOrderByNameAsc();

}
