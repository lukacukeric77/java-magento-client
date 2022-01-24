package com.github.magento.services;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.magento.MagentoClient;
import com.github.magento.models.StockItems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MagentoInventoryStockManager extends MagentoHttpComponent {
   private static final String RELATIVE_PATH = "rest/default/V1/stockItems";
   private final MagentoClient client;

   public MagentoInventoryStockManager(MagentoClient client) {
      super(client.getHttpComponent());
      this.client = client;
   }

   @Override
   public String token() {
      return client.token();
   }


   @Override
   public String baseUri() {
      return client.baseUri();
   }

   public StockItems getStockItems(String productSku) {
      String url = baseUri() + RELATIVE_PATH + "/" + productSku;
      log.info("URL to send: {}", url);
      String json = getSecure(url);
      if(!validate(json)){
         return null;
      }
       return JSON.parseObject(json, StockItems.class);
   }

   public String saveStockItems(String productSku, StockItems si){

      String url = baseUri() + "/rest/V1/products/" + escape(productSku) + "/stockItems/" + si.getItem_id();
      Map<String, Object> req = new HashMap<>();
      Map<String, Object> obj = new HashMap<>();
      obj.put("qty", si.getQty());

      obj.put("item_id", si.getItem_id());
      obj.put("product_id", si.getProduct_id());
              obj.put("stock_id", si.getStock_id());

      obj.put("is_in_stock", si.is_in_stock());
      obj.put("is_qty_decimal", si.is_qty_decimal());

      obj.put("show_default_notification_message", si.isShow_default_notification_message());
              obj.put("use_config_min_qty", si.isUse_config_min_qty());
              obj.put("min_qty", si.getMin_qty());
              obj.put("use_config_min_sale_qty", si.getUse_config_min_sale_qty());
              obj.put("min_sale_qty", si.getMin_sale_qty());
              obj.put("use_config_max_sale_qty", si.isUse_config_max_sale_qty());
              obj.put("max_sale_qty", si.getMax_sale_qty());
              obj.put("use_config_backorders", si.isUse_config_backorders());
              obj.put("backorders", si.getBackorders());
              obj.put("use_config_notify_stock_qty", si.isUse_config_notify_stock_qty());
              obj.put("notify_stock_qty", si.getNotify_stock_qty());
              obj.put("use_config_qty_increments", si.isUse_config_qty_increments());
              obj.put("qty_increments", si.getQty_increments());
              obj.put("use_config_enable_qty_inc", si.isUse_config_enable_qty_inc());
              obj.put("enable_qty_increments", si.isEnable_qty_increments());
              obj.put("use_config_manage_stock", si.isUse_config_manage_stock());
              obj.put("manage_stock", si.isManage_stock());
              obj.put("low_stock_date", si.getLow_stock_date());
              obj.put("is_decimal_divided", si.is_decimal_divided());
              obj.put("stock_status_changed_auto", si.getStock_status_changed_auto());
              obj.put("extension_attributes", new ArrayList<String>());

      req.put("stockItem", obj);
      String body = JSON.toJSONString(req, SerializerFeature.BrowserCompatible);
      String stockId = putSecure(url, body);

      if(!validate(stockId)){
         return null;
      }

      return stockId;
   }
}
