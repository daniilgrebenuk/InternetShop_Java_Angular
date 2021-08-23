package com.project.internetshop.rest;

import com.project.internetshop.model.Category;
import com.project.internetshop.repository.CategoryRepository;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class TestControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private CategoryRepository categoryRepository;


  private MockMvc mockMvc;

  @Autowired
  private TestRestTemplate restTemplate;

  @BeforeEach
  void setUp(){
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }


  @Test
  void test1() throws Exception {
    ResponseEntity<Category[]> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/test", Category[].class);
    assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    assertEquals(categoryRepository.findAllActiveOrderByName(), Arrays.asList(responseEntity.getBody()));
  }
}