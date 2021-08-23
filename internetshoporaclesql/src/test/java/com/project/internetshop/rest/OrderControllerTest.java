package com.project.internetshop.rest;

import com.project.internetshop.model.Order;
import com.project.internetshop.model.Role;
import com.project.internetshop.model.User;
import com.project.internetshop.repository.OrderRepository;
import com.project.internetshop.repository.ProductRepository;
import com.project.internetshop.security.jwt.JwtTokenProvider;
import com.project.internetshop.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class OrderControllerTest {

  @Value("${jwt.authorization-header}")
  private String authorizationHeader;

  @LocalServerPort
  private int port;

  private HttpHeaders headers;
  private String baseUrl;
  private String token;
  private User user;

  private final TestRestTemplate restTemplate;
  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;

  @Autowired
  public OrderControllerTest(TestRestTemplate restTemplate,
                             UserService userService,
                             JwtTokenProvider jwtTokenProvider, OrderRepository orderRepository, ProductRepository productRepository) {
    this.restTemplate = restTemplate;
    this.userService = userService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
  }

  @BeforeEach
  void setUp() {
    user = new User();
    user.setRole(Role.USER);
    user.setUsername("test");
    user.setPassword("test");

    user = userService.save(user);
    token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
    baseUrl = "http://localhost:" + port + "/api/v1/order";

    headers = new HttpHeaders();
    headers.add(authorizationHeader, token);
  }

  @AfterEach
  synchronized void tearDown() {
    productRepository.deleteAll();
    orderRepository.deleteAll();
    userService.delete(user);
  }

  @Test
  void getNewOrderWithoutAuthorization(){
    Order order = new Order();


    ResponseEntity<Order> entity =
        restTemplate.getForEntity(baseUrl + "/newOrder", Order.class);

    assertEquals(order, entity.getBody());
    assertEquals(HttpStatus.OK, entity.getStatusCode());
  }

  @Test
  void getNewOrderWithAuthorization(){
    Order order = new Order(user);



    ResponseEntity<?> response = ResponseEntity.ok().headers(headers).build();


    ResponseEntity<Order> entity =
        restTemplate.exchange(
            "http://localhost:" + port + "/api/v1/order/newOrder",
            HttpMethod.GET,
            response,
            Order.class
        );


    assertEquals(order, entity.getBody());
    assertEquals(HttpStatus.OK, entity.getStatusCode());
  }

  @Test
  void getRecentOrderWithoutAuthorization(){
    Order order = new Order();
    order.setUser(user);

    order = orderRepository.save(order);

    ResponseEntity<?> entity =
        restTemplate.getForEntity(baseUrl + "/recent", Object.class);


    assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
  }

  @Test
  void getRecentOrderWithAuthorization(){

    User userForAnotherOrder = new User();
    userForAnotherOrder.setRole(Role.USER);
    userForAnotherOrder.setUsername("anotherUser");
    userForAnotherOrder.setPassword("anotherUser");

    userForAnotherOrder = userService.save(userForAnotherOrder);

    List<Order> orders = new ArrayList<>();

    for (int i = 0; i < 4; i++) {
      Order order = new Order();
      order.setProducts(new ArrayList<>());
      order.setUser(user);
      orders.add(orderRepository.save(order));
    }


    Order anotherOrder = new Order();
    anotherOrder.setUser(userForAnotherOrder);
    orderRepository.save(anotherOrder);

    ResponseEntity<?> response = ResponseEntity.ok().headers(headers).build();

    ResponseEntity<List<Order>> entity =
        restTemplate.exchange(
            "http://localhost:" + port + "/api/v1/order/recent",
            HttpMethod.GET,
            response,
            new ParameterizedTypeReference<>() {}
        );

    assertEquals(
        orders.toString(),
        Objects.requireNonNull(entity.getBody()).toString()
        );
    assertEquals(HttpStatus.OK, entity.getStatusCode());
  }
}
