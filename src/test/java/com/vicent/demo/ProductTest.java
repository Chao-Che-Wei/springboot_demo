package com.vicent.demo;

import com.vicent.demo.entity.Product;
import com.vicent.demo.repository.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {

    private HttpHeaders httpHeaders;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void init(){
        httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        productRepository.deleteAll();
    }

    @After
    public void clear(){
        productRepository.deleteAll();
    }

    @Test
    public void testGetProduct() throws Exception{
        Product product = createProduct("Economics", 450);
        productRepository.insert(product);

        mockMvc.perform(get("/products/" + product.getId()).
                headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    public void testCreateProduct() throws Exception{
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        JSONObject request = new JSONObject();
        request.put("name", "Harry Potter");
        request.put("price", 450);

        RequestBuilder requestBuilder =
                MockMvcRequestBuilders
                .post("/products")
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.price").value(request.getInt("price")))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    public void testReplaceProduct() throws Exception{

        Product product = createProduct("Economic", 450);
        productRepository.insert(product);

        JSONObject request = new JSONObject();
        request.put("name", "Macroeconomics");
        request.put("price", 550);

        mockMvc.perform(put("/products/" + product.getId())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.price").value(request.getInt("price")));
    }

    @Test
    public void testDeleteProduct() throws Exception{
        Product product = createProduct("Economics", 450);
        productRepository.insert(product);

        mockMvc.perform(delete("/products/" + product.getId())
                .headers(httpHeaders))
                .andExpect(status().isNoContent());

        // It will stop test due to throw exception
        /*productRepository.findById(product.getId())
                .orElseThrow(RuntimeException::new);*/
    }

    @Test
    public void testSearchProductsSortByPriceAsc() throws Exception{
        Product p1 = createProduct("Operation Management", 350);
        Product p2 = createProduct("Marketing Management", 200);
        Product p3 = createProduct("Human Resource Management", 420);
        Product p4 = createProduct("Finance Management", 400);
        Product p5 = createProduct("Enterprise Resource Planning", 440);
        productRepository.insert(Arrays.asList(p1, p2, p3, p4, p5));

        // method 1
        /*mockMvc.perform(get("/products")
                .headers(httpHeaders)
                .param("keyword", "Manage")
                .param("orderBy", "price")
                .param("sortRule", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id").value(p2.getId()))
                .andExpect(jsonPath("$[1].id").value(p1.getId()))
                .andExpect(jsonPath("$[2].id").value(p4.getId()))
                .andExpect(jsonPath("$[3].id").value(p3.getId()));*/

        // method 2
        MvcResult result = mockMvc.perform(get("/products")
                .headers(httpHeaders)
                .param("keyword", "Manage")
                .param("orderBy", "price")
                .param("sortRule", "asc"))
                .andReturn();

        MockHttpServletResponse mockHttpServletResponse = result.getResponse();
        String responseJSONStr = mockHttpServletResponse.getContentAsString();
        JSONArray productJSONArray = new JSONArray(responseJSONStr);

        List<String> productIds = new ArrayList<>();
        for(int i = 0; i < productJSONArray.length(); i++){
            JSONObject productJSON = productJSONArray.getJSONObject(i);
            productIds.add(productJSON.getString("id"));
        }

        Assert.assertEquals(4, productIds.size());
        Assert.assertEquals(p2.getId(), productIds.get(0));
        Assert.assertEquals(p1.getId(), productIds.get(1));
        Assert.assertEquals(p4.getId(), productIds.get(2));
        Assert.assertEquals(p3.getId(), productIds.get(3));
        Assert.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());
        Assert.assertEquals("application/json;charset=UTF-8", mockHttpServletResponse.getContentType());

        Assert.assertTrue(productIds.contains(p1.getId()));
        Assert.assertTrue(productIds.contains(p2.getId()));
        Assert.assertTrue(productIds.contains(p3.getId()));
        Assert.assertTrue(productIds.contains(p4.getId()));
    }

    @Test
    public void get400WhenCreateProductWithEmptyName() throws Exception{
        JSONObject request = new JSONObject();
        request.put("name", "");
        request.put("price", 350);

        mockMvc.perform(post("/products")
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get400WhenReplaceProductWithNegativePrice() throws Exception{
        Product product = createProduct("Computer Science", 350);
        productRepository.insert(product);

        JSONObject request = new JSONObject();
        request.put("name", "Computer SScience");
        request.put("price", -100);

        mockMvc.perform(put("/products/" + product.getId())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isBadRequest());
    }

    private Product createProduct(String name, int price){
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}
