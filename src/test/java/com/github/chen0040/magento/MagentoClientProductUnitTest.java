package com.github.chen0040.magento;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.magento.models.MagentoAttributeType;
import com.github.chen0040.magento.models.Product;
import com.github.chen0040.magento.models.ProductAttributePage;
import com.github.chen0040.magento.models.ProductPage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class MagentoClientProductUnitTest {

   @Test
   public void test_login_client(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsClient(Mediator.CUSTOMER_USERNAME, Mediator.CUSTOMER_PASSWORD);
      assert client.isAuthenticated();
      log.info("my account:\r\n{}", JSON.toJSONString(client.getMyAccount(), SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_login_admin(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);
      assert client.isAuthenticated();
      assert client.isAdminLoggedIn();
      log.info("admin account:\r\n{}", JSON.toJSONString(client.getMyAccount()));
   }

   @Test
   public void test_list_product(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      String token = client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);
      log.info("account with id = 1: {}", client.getAccountById(1));
      log.info("product types: \r\n{}", JSON.toJSONString(client.products().listProductTypes(), SerializerFeature.PrettyFormat));

      ProductPage page  = client.products().page(0, 10);
      log.info("product page: \r\n{}", JSON.toJSONString(page, SerializerFeature.PrettyFormat));
      Product p1 = page.getItems().get(0);
      Product p2 = client.products().getProductBySku(p1.getSku());
      log.info("product:\r\n{}", JSON.toJSONString(p2, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_get_product(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);

      Product p1 = client.products().getProductBySku("B201-SKU");
      log.info("product:\r\n{}", JSON.toJSONString(p1, SerializerFeature.PrettyFormat));
      Product p2 = client.products().getProductBySku("B202-SKU");
      log.info("product:\r\n{}", JSON.toJSONString(p2, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_delete_product(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);

      String sku = "B203-SKU";
      log.info("product exists ? {}", client.products().hasProduct(sku));
      log.info("client.deleteProduct(sku): {}", client.products().deleteProduct(sku));
      log.info("product exists ? {}", client.products().hasProduct(sku));
   }

   @Test
   public void test_list_product_attribute_types() {
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);

      List<MagentoAttributeType> attributeTypes = client.products().getProductAttributeTypes();
      log.info("product attribute types:\r\n{}", JSON.toJSONString(attributeTypes, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_list_product_attributes() {
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);

      ProductAttributePage page = client.products().getProductAttributes(0,10);
      log.info("product attribute types:\r\n{}", JSON.toJSONString(page, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_add_product() {
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);

      String sku = "B203-SKU";
      if(client.products().hasProduct(sku)) {
         log.info("Deleting {}", sku);
         client.products().deleteProduct(sku);
         try {
            Thread.sleep(3000L);
         }
         catch (InterruptedException e) {
            e.printStackTrace();
         }
      }

      Product product = new Product();
      product.setSku(sku);
      product.setName("B203");
      product.setPrice(30.00);
      product.setStatus(1);
      product.setType_id("simple");
      product.setAttribute_set_id(4);
      product.setWeight(1);
      product.setVisibility(Product.VISIBILITY_BOTH);
      product.setStatus(Product.STATUS_ENABLED);

      log.info("add product result: {}", JSON.toJSONString(client.products().saveProduct(product), SerializerFeature.PrettyFormat));
   }


}
