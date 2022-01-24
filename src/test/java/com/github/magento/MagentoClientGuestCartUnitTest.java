package com.github.magento;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.magento.models.Cart;
import com.github.magento.models.CartItem;
import com.github.magento.models.CartTotal;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class MagentoClientGuestCartUnitTest {

   @Test
   public void test_newCart(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      String cartId = client.guestCart().newCart();
      Cart cart = client.guestCart().getCart(cartId);
      CartTotal cartTotal = client.getGuestCart().getCartTotal(cartId);

      log.info("cart: \r\n{}", JSON.toJSONString(cart, SerializerFeature.PrettyFormat));
      log.info("cartTotal: \r\n{}", JSON.toJSONString(cartTotal, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_addItemToCart(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      String cartId = client.guestCart().newCart();

      CartItem item = new CartItem();
      item.setQty(1);
      item.setSku("product_dynamic_758");

      item = client.guestCart().addItemToCart(cartId, item);


      Cart cart = client.guestCart().getCart(cartId);
      CartTotal cartTotal = client.getGuestCart().getCartTotal(cartId);

      log.info("cartItem: \r\n{}", JSON.toJSONString(item, SerializerFeature.PrettyFormat));
      log.info("cart: \r\n{}", JSON.toJSONString(cart, SerializerFeature.PrettyFormat));
      log.info("cartTotal: \r\n{}", JSON.toJSONString(cartTotal, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_updateItemInCart(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      String cartId = client.guestCart().newCart();

      CartItem item = new CartItem();
      item.setQty(1);
      item.setSku("product_dynamic_758");

      item = client.guestCart().addItemToCart(cartId, item);
      item.setQty(3);
      item = client.guestCart().updateItemInCart(cartId, item);


      Cart cart = client.guestCart().getCart(cartId);
      CartTotal cartTotal = client.getGuestCart().getCartTotal(cartId);

      log.info("cartItem: \r\n{}", JSON.toJSONString(item, SerializerFeature.PrettyFormat));
      log.info("cart: \r\n{}", JSON.toJSONString(cart, SerializerFeature.PrettyFormat));
      log.info("cartTotal: \r\n{}", JSON.toJSONString(cartTotal, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_deleteItemInCart(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      String cartId = client.guestCart().newCart();

      CartItem item = new CartItem();
      item.setQty(1);
      item.setSku("product_dynamic_758");

      item = client.guestCart().addItemToCart(cartId, item);
      boolean result = client.guestCart().deleteItemInCart(cartId, item.getItem_id());


      Cart cart = client.guestCart().getCart(cartId);
      CartTotal cartTotal = client.getGuestCart().getCartTotal(cartId);

      log.info("result: {}", result);
      log.info("cart: \r\n{}", JSON.toJSONString(cart, SerializerFeature.PrettyFormat));
      log.info("cartTotal: \r\n{}", JSON.toJSONString(cartTotal, SerializerFeature.PrettyFormat));
   }


}
