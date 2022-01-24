package com.github.magento.services;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.magento.MagentoClient;
import com.github.magento.models.Account;
import com.github.magento.models.Cart;
import com.github.magento.models.CartItem;
import com.github.magento.models.CartTotal;
import com.github.magento.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class MagentoMyCartManager extends MagentoHttpComponent {
   protected final MagentoClient client;
   private static final String RELATIVE_PATH_CART_MANAGER = "rest/V1/carts";
   private static final String CART_ID = "mine";
   private long customerId = -1L;
   private long storeId = -1L;

   public MagentoMyCartManager(MagentoClient client) {
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

   public String newQuote() {
      String json = postSecure(baseUri() + "/" + RELATIVE_PATH_CART_MANAGER + "/" + CART_ID, "");

      if(!validate(json)){
         return null;
      }

      return StringUtils.stripQuotation(json);
   }

   public Cart getCart() {

      String json = getSecure(baseUri() + "/" + RELATIVE_PATH_CART_MANAGER + "/" + CART_ID);

      if(!validate(json)){
         return null;
      }

      return JSON.parseObject(json, Cart.class);
   }

   public CartTotal getCartTotal() {
      String json = getSecure(baseUri() + "/" + RELATIVE_PATH_CART_MANAGER + "/" + CART_ID + "/totals");

      if(!validate(json)){
         return null;
      }

      return JSON.parseObject(json, CartTotal.class);
   }

   public CartItem addItemToCart(String quoteId, CartItem item) {
      Map<String, Map<String, Object>> request = new HashMap<>();
      Map<String, Object> cartItem = new HashMap<>();
      cartItem.put("qty", item.getQty());
      cartItem.put("sku", item.getSku());
      cartItem.put("quote_id", quoteId);
      request.put("cartItem", cartItem);
      String json = JSON.toJSONString(request, SerializerFeature.BrowserCompatible);
      json = postSecure(baseUri() + "/" + RELATIVE_PATH_CART_MANAGER + "/" + CART_ID + "/items", json);

      if(!validate(json)){
         return null;
      }

      return JSON.parseObject(json, CartItem.class);
   }

   public CartItem updateItemInCart(String quoteId, CartItem item) {
      Map<String, Map<String, Object>> request = new HashMap<>();
      Map<String, Object> cartItem = new HashMap<>();
      cartItem.put("qty", item.getQty());
      cartItem.put("sku", item.getSku());
      cartItem.put("item_id", item.getItem_id());
      cartItem.put("quote_id", quoteId);
      request.put("cartItem", cartItem);
      String json = JSON.toJSONString(request, SerializerFeature.BrowserCompatible);
      json = putSecure(baseUri() + "/" + RELATIVE_PATH_CART_MANAGER + "/" + CART_ID + "/items/" + item.getItem_id(), json);

      if(!validate(json)){
         return null;
      }

      return JSON.parseObject(json, CartItem.class);
   }

   public boolean deleteItemInCart(int itemId) {

      String json = deleteSecure(baseUri() + "/" + RELATIVE_PATH_CART_MANAGER + "/" + CART_ID + "/items/" + itemId);

      if(!validate(json)){
         return false;
      }

      return json.equalsIgnoreCase("true");
   }

   public boolean transferGuestCartToMyCart(String guestCartId) {
      if (customerId == -1L) {
         Account account = client.getMyAccount();
         customerId = account.getId();
         storeId = account.getStore_id();
      }
      Map<String, Object> request = new HashMap<>();
      request.put("customerId", customerId);
      request.put("storeId", storeId);
      String json = JSON.toJSONString(request, SerializerFeature.BrowserCompatible);
      json = putSecure(baseUri() + "/rest/V1/guest-carts/" + guestCartId, json);

      return validate(json);
   }

}
