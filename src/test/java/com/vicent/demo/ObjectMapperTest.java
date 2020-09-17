package com.vicent.demo;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ObjectMapperTest {

    private ObjectMapper mapper = new ObjectMapper();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class Book{
        private String id;
        private String name;
        private int price;
        @JsonIgnore
        private String isbn;
        @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss", timezone = "GMT+8")
        private Date createdTime;
        @JsonUnwrapped
        private Publisher publisher;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public void setCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
        }

        public void setPublisher(Publisher publisher) {
            this.publisher = publisher;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public String getIsbn() {
            return isbn;
        }

        public Date getCreatedTime() {
            return createdTime;
        }

        public Publisher getPublisher() {
            return publisher;
        }
    }

    private  static class  Publisher{
        private String companyName;
        private String address;

        @JsonProperty("telephone")
        private String tel;

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getAddress() {
            return address;
        }

        public String getTel() {
            return tel;
        }
    }

    @Test
    public void testSerializeBookToJSON() throws Exception{
        Book book = new Book();
        book.setId("B0001");
        book.setName("Computer Science");
        book.setPrice(350);
        book.setIsbn("978-986-123-456-7");
        book.setCreatedTime(new Date());

        Publisher publisher = new Publisher();
        publisher.setCompanyName("Taipei Company");
        publisher.setAddress("Taipei");
        publisher.setTel("02-1234-5678");
        book.setPublisher(publisher);

        String strBookJSON = mapper.writeValueAsString(book);
        JSONObject bookJSON = new JSONObject(strBookJSON);

        Assert.assertEquals(book.getId(), bookJSON.getString("id"));
        Assert.assertEquals(book.getName(), bookJSON.getString("name"));
        Assert.assertEquals(book.getPrice(), bookJSON.getInt("price"));
        //Assert.assertEquals(book.getIsbn(), bookJSON.getString("isbn"));
        //Assert.assertEquals(book.getCreatedTime().getTime(), bookJSON.getLong("createdTime"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        Assert.assertEquals(dateFormat.format(book.getCreatedTime()), bookJSON.getString("createdTime"));
        Assert.assertEquals(book.getPublisher().getCompanyName(), bookJSON.getString("companyName"));
        Assert.assertEquals(book.getPublisher().getAddress(), bookJSON.getString("address"));
        Assert.assertEquals(book.getPublisher().getTel(), bookJSON.getString("telephone"));
    }

    @Test
    public  void testDeserializeJSONToPublisher() throws Exception{
        JSONObject publisherJSON = new JSONObject();
        publisherJSON
                .put("companyName", "Taipei Company")
                .put("address", "Taipei")
                .put("telephone", "02-1234-5678");

        String strPublisherJSON = publisherJSON.toString();
        Publisher publisher = mapper.readValue(strPublisherJSON, Publisher.class);

        Assert.assertEquals(publisherJSON.getString("companyName"), publisher.getCompanyName());
        Assert.assertEquals(publisherJSON.getString("address"), publisher.getAddress());
        Assert.assertEquals(publisherJSON.getString("telephone"), publisher.getTel());
    }
}
