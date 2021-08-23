package com.project.internetshop.rest;

import com.project.internetshop.model.*;
import com.project.internetshop.repository.CategoryRepository;
import com.project.internetshop.repository.OrderRepository;
import com.project.internetshop.repository.ProductRepository;
import com.project.internetshop.security.jwt.JwtTokenProvider;
import com.project.internetshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ProductControllerTest {

  @Value("${jwt.authorization-header}")
  private String authorizationHeader;

  @LocalServerPort
  private int port;

  private String token;
  private User user;
  private String baseUrl;


  private final TestRestTemplate restTemplate;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;
  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  @Autowired
  public ProductControllerTest(TestRestTemplate restTemplate, JwtTokenProvider jwtTokenProvider, UserService userService, CategoryRepository categoryRepository, ProductRepository productRepository, OrderRepository orderRepository) {
    this.restTemplate = restTemplate;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userService = userService;
    this.categoryRepository = categoryRepository;
    this.productRepository = productRepository;
    this.orderRepository = orderRepository;
  }

  @BeforeEach
  void setUp() {
    user = new User();
    user.setRole(Role.ADMIN);
    user.setUsername("test");
    user.setPassword("test");

    user = userService.save(user);
    token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
    baseUrl = "http://localhost:" + port + "/api/v1/product";
  }

  @AfterEach
  void tearDown() {
    userService.delete(user);
    orderRepository.deleteAll();
    productRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  @Test
  void addNewProductWithoutCategory() {

    HttpHeaders headers = new HttpHeaders();
    headers.add(authorizationHeader, "Bearer " + token);

    ResponseEntity<Product> entity = ResponseEntity.ok().headers(headers).body(new Product(null, null, "1", 1, "1", true));

    ResponseEntity<?> request = restTemplate.postForEntity(baseUrl + "/updatable/add",
        entity, Product.class);

    assertEquals(HttpStatus.BAD_REQUEST, request.getStatusCode());
  }

  @Test
  void addNewProductWithUserRole() {
    user.setRole(Role.USER);
    user = userService.save(user);
    token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());

    HttpHeaders headers = new HttpHeaders();
    headers.add(authorizationHeader, token);

    Category category = categoryRepository.save(new Category());
    Product product = new Product(null, category, "1", 1, "1", true);
    ResponseEntity<Product> entity = ResponseEntity.ok().headers(headers).body(product);

    ResponseEntity<?> request = restTemplate.postForEntity(baseUrl + "/updatable/add",
        entity, Product.class);

    assertEquals(HttpStatus.FORBIDDEN, request.getStatusCode());
  }

  @Test
  void addNewProductWithAdminRole() {
    HttpHeaders headers = new HttpHeaders();
    headers.add(authorizationHeader, token);

    Category category = categoryRepository.save(new Category());
    Product product = new Product(null, category, "1", 1, "1", true);
    ResponseEntity<Product> entity = ResponseEntity.ok().headers(headers).body(product);

    ResponseEntity<?> request = restTemplate.postForEntity(baseUrl + "/updatable/add",
        entity, Product.class);

    assertEquals(HttpStatus.CREATED, request.getStatusCode());
  }

  @Test
  void updateNewProductWithoutCategory() {

    HttpHeaders headers = new HttpHeaders();
    headers.add(authorizationHeader,  token);
    Product product = productRepository.save(new Product(null, null, "1", 1, "1", true));

    ResponseEntity<Product> entity = ResponseEntity.ok().headers(headers).body(product);


    ResponseEntity<?> request = restTemplate.exchange(baseUrl + "/updatable/update",
        HttpMethod.PUT, entity, Product.class);

    assertEquals(HttpStatus.BAD_REQUEST, request.getStatusCode());
  }

  @Test
  void updateNewProductWithUserRole() {
    user.setRole(Role.USER);
    user = userService.save(user);
    token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());

    HttpHeaders headers = new HttpHeaders();
    headers.add(authorizationHeader, token);

    Category category = categoryRepository.save(new Category());
    Product product = productRepository.save(new Product(null, category, "1", 1, "1", true));
    ResponseEntity<Product> entity = ResponseEntity.ok().headers(headers).body(product);

    ResponseEntity<?> request = restTemplate.exchange(baseUrl + "/updatable/update",
        HttpMethod.PUT, entity, Product.class);

    assertEquals(HttpStatus.FORBIDDEN, request.getStatusCode());
  }

  @Test
  void updateNewProductWithAdminRole() {
    HttpHeaders headers = new HttpHeaders();
    headers.add(authorizationHeader, token);

    Category category = categoryRepository.save(new Category());
    Product product = productRepository.save(new Product(null, category, "1", 1, "1", true));
    ResponseEntity<Product> entity = ResponseEntity.ok().headers(headers).body(product);

    ResponseEntity<?> request = restTemplate.exchange(baseUrl + "/updatable/update",
        HttpMethod.PUT, entity, Product.class);

    assertEquals(HttpStatus.CREATED, request.getStatusCode());
  }

  @Test
  void getProductByIdWithoutAuthorization() {
    Category category = categoryRepository.save(new Category());

    List<Product> productList = new ArrayList<>();
    List<ResponseEntity<Product>> entityList = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      productList.add(productRepository.save(new Product(null, category, "1", 1, "1", true)));
      entityList.add(restTemplate.getForEntity(
          baseUrl + "/" + productList.get(i).getId(),
          Product.class
      ));
    }

    assertEquals(productList.get(0), entityList.get(0).getBody());
    assertEquals(productList.get(1), entityList.get(1).getBody());
    assertEquals(productList.get(2), entityList.get(2).getBody());

    assertEquals(HttpStatus.OK, entityList.get(0).getStatusCode());
    assertEquals(HttpStatus.OK, entityList.get(1).getStatusCode());
    assertEquals(HttpStatus.OK, entityList.get(2).getStatusCode());
  }


  @Test
  void getProductsByCategoryIdWithoutAuthorization() {
    Category category = categoryRepository.save(new Category());
    Category categoryForAnotherProduct = categoryRepository.save(new Category());

    List<Product> productList = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      productList
          .add(productRepository.save(new Product(null, category, "1", 1, "1", true)));
    }

    for (int i = 0; i < 3; i++) {
      productList
          .add(productRepository.save(new Product(
              null, categoryForAnotherProduct, "1", 1, "1", true
          )));
    }


    ResponseEntity<Product[]> firstCategoryProductEntity = restTemplate.getForEntity(
        baseUrl + "/category/" + category.getId(),
        Product[].class
    );

    List<Product> firstCategoryProduct = Arrays.asList(
        Objects.requireNonNull(
            firstCategoryProductEntity.getBody()
        )
    );

    ResponseEntity<Product[]> secondCategoryProductEntity = restTemplate.getForEntity(
        baseUrl + "/category/" + categoryForAnotherProduct.getId(),
        Product[].class
    );

    List<Product> secondCategoryProduct = Arrays.asList(
        Objects.requireNonNull(
            secondCategoryProductEntity.getBody()
        )
    );

    assertEquals(
        productList.stream().filter(
            p->p.getCategory().getId().equals(category.getId())
        ).sorted(Comparator.comparing(Product::getName)).toList(),
        firstCategoryProduct
    );

    assertEquals(
        productList.stream().filter(
            p->p.getCategory().getId().equals(categoryForAnotherProduct.getId())
        ).sorted(Comparator.comparing(Product::getName)).toList(),
        secondCategoryProduct
    );

    assertEquals(HttpStatus.OK, firstCategoryProductEntity.getStatusCode());
    assertEquals(HttpStatus.OK, secondCategoryProductEntity.getStatusCode());
  }

  @Test
  void getAllProductWithoutAuthorization(){
    Category category = categoryRepository.save(new Category());
    Category categoryForAnotherProduct = categoryRepository.save(new Category());

    List<Product> productList = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      productList
          .add(productRepository.save(new Product(null, category, "1", 1, "1", true)));
    }

    for (int i = 0; i < 3; i++) {
      productList
          .add(productRepository.save(new Product(
              null, categoryForAnotherProduct, "1", 1, "1", true
          )));
    }

    ResponseEntity<Product[]> entity = restTemplate
        .getForEntity(baseUrl + "/all", Product[].class);

    assertEquals(
        productList.stream().sorted(Comparator.comparing(Product::getName)).toList(),
        Arrays.asList(Objects.requireNonNull(entity.getBody()))
    );

    assertEquals(HttpStatus.OK, entity.getStatusCode());
  }

  @Test
  void getProductsByOrderIdWithoutAuthorization(){
    Category category = categoryRepository.save(new Category());
    Category categoryForAnotherProduct = categoryRepository.save(new Category());

    List<Product> productList = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      productList
          .add(productRepository.save(new Product(null, category, "1", 1, "1", true)));
    }

    for (int i = 0; i < 3; i++) {
      productList
          .add(productRepository.save(new Product(
              null, categoryForAnotherProduct, "1", 1, "1", true
          )));
    }

    List<Product> orderProduct = new ArrayList<>();

    for (int i = 0; i < 15; i++) {
      orderProduct.add(productList.get((int)(Math.random()*7)));
    }

    Order order = new Order();
    order.setProducts(orderProduct);

    order = orderRepository.save(order);

    ResponseEntity<Product[]> entity = restTemplate
        .getForEntity(baseUrl + "/order/"+order.getId(), Product[].class);


    assertEquals(
        orderProduct.stream().sorted(Comparator.comparing(Product::getName)).toList(),
        Arrays.stream(Objects.requireNonNull(entity.getBody())).sorted(Comparator.comparing(Product::getName)).toList()
    );

    assertEquals(HttpStatus.OK, entity.getStatusCode());
  }


}