package com.github.chen0040.magento.services;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Cart;
import com.github.chen0040.magento.models.CartItem;
import com.github.chen0040.magento.models.CartTotal;
import com.github.chen0040.magento.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MagentoGuestCartManager extends MagentoHttpComponent {

   private static final String RELATIVE_PATH_GUEST_CARTS = "rest/V1/guest-carts";
   private static final String LOG_JSON = "Json: {}";
   private final MagentoClient client;

   public MagentoGuestCartManager(MagentoClient client){
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

   public String newCart() {
      String json = postSecure(baseUri() + "/" + RELATIVE_PATH_GUEST_CARTS, "");

      if(!validate(json)){
         return null;
      }
      return StringUtils.stripQuotation(json);
   }

   public Cart getCart(String cartId) {

      String json = getSecured(baseUri() + "/" + RELATIVE_PATH_GUEST_CARTS + "/" + cartId);

      if(!validate(json)){
         return null;
      }

      log.info(LOG_JSON, json);
      return JSON.parseObject(json, Cart.class);
   }

   public CartTotal getCartTotal(String cartId) {
      String json = getSecured(baseUri() + "/" + RELATIVE_PATH_GUEST_CARTS + "/" + cartId + "/totals");

      if(!validate(json)){
         return null;
      }
      log.info(LOG_JSON, json);
      return JSON.parseObject(json, CartTotal.class);
   }

   public CartItem addItemToCart(String cartId, CartItem item) {
      Map<String, Map<String, Object>> request = new HashMap<>();
      Map<String, Object> cartItem = new HashMap<>();
      cartItem.put("qty", item.getQty());
      cartItem.put("sku", item.getSku());
      cartItem.put("quote_id", cartId);
      request.put("cartItem", cartItem);
      String json = JSON.toJSONString(request, SerializerFeature.BrowserCompatible);
      json = postSecure(baseUri() + "/" + RELATIVE_PATH_GUEST_CARTS + "/" + cartId + "/items", json);

      if(!validate(json)){
         return null;
      }
      log.info(LOG_JSON, json);
      return JSON.parseObject(json, CartItem.class);
   }

   public CartItem updateItemInCart(String cartId, CartItem item) {
      Map<String, Map<String, Object>> request = new HashMap<>();
      Map<String, Object> cartItem = new HashMap<>();
      cartItem.put("qty", item.getQty());
      cartItem.put("sku", item.getSku());
      cartItem.put("quote_id", cartId);
      cartItem.put("item_id", item.getItem_id());
      request.put("cartItem", cartItem);
      String json = JSON.toJSONString(request, SerializerFeature.BrowserCompatible);
      json = putSecure(baseUri() + "/" + RELATIVE_PATH_GUEST_CARTS + "/" + cartId + "/items/" + item.getItem_id(), json);

      if(!validate(json)){
         return null;
      }
      log.info(LOG_JSON, json);
      return JSON.parseObject(json, CartItem.class);
   }

   public boolean deleteItemInCart(String cartId, int itemId) {
      String json = deleteSecure(baseUri() + "/" + RELATIVE_PATH_GUEST_CARTS + "/" + cartId + "/items/" + itemId);

      if(!validate(json)){
         return false;
      }
      log.info(LOG_JSON, json);
      return json.equalsIgnoreCase("true");
   }
}
