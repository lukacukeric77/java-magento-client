package com.github.chen0040.magento;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.magento.models.Cart;
import com.github.chen0040.magento.models.CartItem;
import com.github.chen0040.magento.models.CartTotal;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class MagentoClientMyCartUnitTest {

   @Test
   public void test_newCart(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      String token = client.loginAsClient(Mediator.CUSTOMER_USERNAME, Mediator.CUSTOMER_PASSWORD);
      String cartId = client.myCart().newQuote();
      Cart cart = client.myCart().getCart();
      CartTotal cartTotal = client.myCart().getCartTotal();

      log.info("token: {}", token);
      log.info("cart: \r\n{}", JSON.toJSONString(cart, SerializerFeature.PrettyFormat));
      log.info("cartTotal: \r\n{}", JSON.toJSONString(cartTotal, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_addItemToCart(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsClient(Mediator.CUSTOMER_USERNAME, Mediator.CUSTOMER_PASSWORD);
      String quoteId = client.myCart().newQuote();

      CartItem item = new CartItem();
      item.setQty(1);
      item.setSku("product_dynamic_758");

      System.out.println(quoteId);

      item = client.myCart().addItemToCart(quoteId, item);

      Cart cart = client.myCart().getCart();
      CartTotal cartTotal = client.myCart().getCartTotal();

      log.info("cartItem: \r\n{}", JSON.toJSONString(item, SerializerFeature.PrettyFormat));
      log.info("cart: \r\n{}", JSON.toJSONString(cart, SerializerFeature.PrettyFormat));
      log.info("cartTotal: \r\n{}", JSON.toJSONString(cartTotal, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_updateItemInCart(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsClient(Mediator.CUSTOMER_USERNAME, Mediator.CUSTOMER_PASSWORD);
      String quoteId = client.myCart().newQuote();

      CartItem item = new CartItem();
      item.setQty(1);
      item.setSku("product_dynamic_758");

      item = client.myCart().addItemToCart(quoteId, item);
      item.setQty(3);
      item = client.myCart().updateItemInCart(quoteId, item);

      Cart cart = client.myCart().getCart();
      CartTotal cartTotal = client.myCart().getCartTotal();

      log.info("cartItem: \r\n{}", JSON.toJSONString(item, SerializerFeature.PrettyFormat));
      log.info("cart: \r\n{}", JSON.toJSONString(cart, SerializerFeature.PrettyFormat));
      log.info("cartTotal: \r\n{}", JSON.toJSONString(cartTotal, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_deleteItemInCart(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);
      client.loginAsClient(Mediator.CUSTOMER_USERNAME, Mediator.CUSTOMER_PASSWORD);
      String quoteId = client.myCart().newQuote();

      CartItem item = new CartItem();
      item.setQty(1);
      item.setSku("product_dynamic_758");

      item = client.myCart().addItemToCart(quoteId, item);
      boolean result = client.myCart().deleteItemInCart(item.getItem_id());


      Cart cart = client.myCart().getCart();
      CartTotal cartTotal = client.myCart().getCartTotal();

      log.info("result: {}", result);
      log.info("cart: \r\n{}", JSON.toJSONString(cart, SerializerFeature.PrettyFormat));
      log.info("cartTotal: \r\n{}", JSON.toJSONString(cartTotal, SerializerFeature.PrettyFormat));
   }

   @Test
   public void test_transferGuestCartToMyCart(){
      MagentoClient client = new MagentoClient(Mediator.UBERTHEME_URL);

      String cartId = client.guestCart().newCart();

      CartItem item = new CartItem();
      item.setQty(1);
      item.setSku("product_dynamic_758");

      item = client.guestCart().addItemToCart(cartId, item);

      client.loginAsClient(Mediator.CUSTOMER_USERNAME, Mediator.CUSTOMER_PASSWORD);
      boolean result = client.myCart().transferGuestCartToMyCart(cartId);

      Cart cart = client.myCart().getCart();
      CartTotal cartTotal = client.myCart().getCartTotal();

      log.info("result: {}", result);
      log.info("cart: \r\n{}", JSON.toJSONString(cart, SerializerFeature.PrettyFormat));
      log.info("cartTotal: \r\n{}", JSON.toJSONString(cartTotal, SerializerFeature.PrettyFormat));
   }
}
