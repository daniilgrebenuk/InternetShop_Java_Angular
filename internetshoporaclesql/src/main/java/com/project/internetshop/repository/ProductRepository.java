package com.project.internetshop.repository;


import com.project.internetshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  String FIND_ALL_PRODUCT_BY_ORDER_ID = "SELECT pr.* " +
                                        "FROM product pr JOIN orders_products op ON pr.id = op.products_id " +
                                        "WHERE op.order_id = ?1";

  @Query(value = FIND_ALL_PRODUCT_BY_ORDER_ID, nativeQuery = true)
  List<Product> findAllByOrder_id(Long id);

  List<Product> findAllByCategory_IdOrderByName(Long id);

  List<Product> findAllByOrderByNameDesc();
  List<Product> findAllByOrderByNameAsc();

  List<Product> findAllByOrderByPriceDesc();
  List<Product> findAllByOrderByPriceAsc();
}
