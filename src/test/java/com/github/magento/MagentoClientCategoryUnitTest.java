package com.github.magento;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.magento.models.Category;
import com.github.magento.models.CategoryProduct;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;


@Slf4j
public class MagentoClientCategoryUnitTest {

   @Test
   public void test_get_category_by_id(){
      long id = 15;
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);
      Category category = client.categories().getCategoryByIdClean(id);
      log.info("category:\r\n{}", JSON.toJSONString(category, SerializerFeature.PrettyFormat));

      category = client.categories().getCategoryByIdWithChildren(id);
      log.info("category:\r\n{}", JSON.toJSONString(category, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_delete_category_by_id(){
      long id = 15;
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);
      boolean deleted = client.categories().deleteCategory(id);
      log.info("category deleted: {}", deleted);
   }

   @Test
   public void test_list_categories() {
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);

      Category page = client.categories().all();
      log.info("categories: {}\r\n", JSON.toJSONString(page, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_list_products_in_category() {
      long id = 1;
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);

      List<CategoryProduct> products = client.categories().getProductsInCategory(id);
      log.info("products in category" + id + ":\r\n{}", JSON.toJSONString(products, SerializerFeature.PrettyFormat));
   }

   @Test
   public void add_product_to_category() {
      long categoryId = 15;
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);

      String productSku = "B202-SKU";
      boolean added = client.categories().addProductToCategory(categoryId, productSku, 1);
      log.info("added ? {}", added);
   }

   @Test
   public void delete_product_from_category(){
      long categoryId = 15;
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);

      String productSku = "B202-SKU";
      boolean removed = client.categories().removeProductFromCategory(categoryId, productSku);
      log.info("removed ? {}", removed);
   }

}
