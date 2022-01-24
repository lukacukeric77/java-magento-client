package com.github.magento;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.magento.models.StockItems;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class MagentoClientInventoryUnitTest {

   @Test
   public void test_getStockItems(){
      String productSku = "product_dynamic_571";

      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);
//      log.info("stock item, product dynamic: {}", JSON.toJSONString(client.inventory().getStockItems(productSku), SerializerFeature.PrettyFormat));
//
//      productSku = "B203-SKU";
//      log.info("stock item, B203-SKU: {}", JSON.toJSONString(client.inventory().getStockItems(productSku), SerializerFeature.PrettyFormat));

      productSku = "WJ02";
      log.info("Josie Yoga Jacket: {}", JSON.toJSONString(client.inventory().getStockItems(productSku), SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_saveStockItems(){
      String productSku;

      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsAdmin(Mediator.ADMIN_USERNAME, Mediator.ADMIN_PASSWORD);

      productSku = "B203-SKU";
      StockItems si = client.inventory().getStockItems(productSku);
      si.setQty(2);
      String stockId = client.inventory().saveStockItems(productSku, si);
      log.info("stock item saved: {}", stockId);
   }
}
