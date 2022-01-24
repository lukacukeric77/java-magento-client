package com.github.magento;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.magento.models.Account;
import com.github.magento.services.BasicHttpComponent;
import com.github.magento.services.HttpComponent;
import com.github.magento.services.MagentoCategoryManager;
import com.github.magento.services.MagentoGuestCartManager;
import com.github.magento.services.MagentoHttpComponent;
import com.github.magento.services.MagentoInventoryStockManager;
import com.github.magento.services.MagentoMyCartManager;
import com.github.magento.services.MagentoProductManager;
import com.github.magento.services.MagentoProductMediaManager;
import com.github.magento.utils.StringUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class MagentoClient extends MagentoHttpComponent implements Serializable {
   private static final long serialVersionUID = 3001998767951271632L;
   private static final String RELATIVE_PATH_4_LOGIN_AS_CLIENT = "rest/V1/integration/customer/token";
   private static final String RELATIVE_PATH_4_LOGIN_AS_ADMIN = "rest/V1/integration/admin/token";

   private String token = null;
   private String baseUri = "";

   private boolean adminLoggedIn = false;
   private boolean authenticated = false;

   private MagentoProductManager products;
   private MagentoCategoryManager categories;
   private MagentoInventoryStockManager inventory;
   private MagentoProductMediaManager media;
   private MagentoGuestCartManager guestCart;
   private MagentoMyCartManager myCart;

   public MagentoClient(String baseUri, HttpComponent httpComponent) {

      super(httpComponent);

      this.baseUri = baseUri;
      this.products = new MagentoProductManager(this);
      this.categories = new MagentoCategoryManager(this);
      this.inventory = new MagentoInventoryStockManager(this);
      this.media = new MagentoProductMediaManager(this);
      this.guestCart = new MagentoGuestCartManager(this);
      this.myCart = new MagentoMyCartManager(this);
   }

   public MagentoClient(String baseUri) {

      super(new BasicHttpComponent());

      this.baseUri = baseUri;
      this.products = new MagentoProductManager(this);
      this.categories = new MagentoCategoryManager(this);
      this.inventory = new MagentoInventoryStockManager(this);
      this.media = new MagentoProductMediaManager(this);
      this.guestCart = new MagentoGuestCartManager(this);
      this.myCart = new MagentoMyCartManager(this);
   }

   public Account getMyAccount() {
      if (adminLoggedIn) {
         log.warn("my account access api is not supported for admin rest call");
         return null;
      }

      //"http://magento.ll/index.php/rest/V1/customers/me" -H "Authorization: Bearer asdf3hjklp5iuytre"
      String uri = this.baseUri + "/rest/V1/customers/me";
      String json = getSecure(uri);

      if (!validate(json)) {
         return null;
      }

      return JSON.parseObject(json, Account.class);
   }

   public Map<String, Object> getAccountById(long id) {
      if (!adminLoggedIn) {
         log.warn("other account access api is not supported for client rest call");
         return new HashMap<>();
      }

      String uri = this.baseUri + "/rest/V1/customers/" + id;
      String json = getSecure(uri);
      return JSON.parseObject(json, new TypeReference<Map<String, Object>>(){}.getType());
   }

   public String loginAsClient(String username, String password) {
      String uri = baseUri + "/" + RELATIVE_PATH_4_LOGIN_AS_CLIENT;
      Map<String, String> data = new HashMap<>();
      data.put("username", username);
      data.put("password", password);
      this.token = StringUtils.stripQuotation(httpComponent.jsonPost(uri, data));
      log.info("loginAsClient returns: {}", token);

      if(token.contains("You did not sign in correctly or your account is temporarily disabled") || token.contains("Invalid login or password")) {
         this.token = "";
         return token;
      }
      authenticated = true;
      return token;
   }

   public void logout() {
      //String uri = baseUri + "/rest/V1/integration/customer/revoke";
      authenticated = false;
      adminLoggedIn = false;
      token = null;
   }

   public String loginAsAdmin(String username, String password) {
      String uri = baseUri + "/" + RELATIVE_PATH_4_LOGIN_AS_ADMIN;
      Map<String, String> data = new HashMap<>();
      data.put("username", username);
      data.put("password", password);
      token = StringUtils.stripQuotation(httpComponent.jsonPost(uri, data));
      log.info("loginAsClient returns, token check: {}", token);

      if(token.contains("You did not sign in correctly or your account is temporarily disabled")
          || token.contains("Invalid login or password")) {
         this.token = "";
         return token;
      }
      authenticated = true;
      adminLoggedIn = true;
      return token;
   }

   public MagentoCategoryManager categories() {
      return categories;
   }

   public MagentoProductManager products() {
      return products;
   }

   public MagentoInventoryStockManager inventory() {
      return inventory;
   }

   public MagentoProductMediaManager media() {return media;}

   public MagentoGuestCartManager guestCart() {return guestCart; }

   public MagentoMyCartManager myCart() { return myCart; }


   @Override
   public String token() {
      return this.token;
   }


   @Override
   public String baseUri() {
      return this.baseUri;
   }

}
