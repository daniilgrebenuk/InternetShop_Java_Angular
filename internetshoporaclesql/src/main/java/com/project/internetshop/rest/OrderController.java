package com.project.internetshop.rest;

import com.project.internetshop.model.Order;
import com.project.internetshop.model.User;
import com.project.internetshop.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
  private final OrderRepository orderRepository;

  @Autowired
  public OrderController(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @GetMapping("/newOrder")
  public ResponseEntity<Order> getNewOrder(@AuthenticationPrincipal User user){
    return ResponseEntity.ok(user != null ? new Order(user): new Order());
  }


  @GetMapping("/recent")
  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
  public ResponseEntity<List<Order>> getRecentOrders(@AuthenticationPrincipal User user){
    List<Order> orders = orderRepository.findAllByUserId(user.getId());
    log.info(orders.toString());
    return ResponseEntity.ok(orders);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addOrder(@RequestBody Order order, @AuthenticationPrincipal User user){
    order.setUser(user);
    order.setCreatedAt(new Date());
    Map<String, String> res = new HashMap<>();

    res.put("orderId", orderRepository.save(order).getId().toString());
    return ResponseEntity.ok(res);
  }
}
