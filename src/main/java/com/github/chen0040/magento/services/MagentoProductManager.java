package com.github.chen0040.magento.services;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.MagentoAttributeType;
import com.github.chen0040.magento.models.MagentoType;
import com.github.chen0040.magento.models.Product;
import com.github.chen0040.magento.models.ProductAttributePage;
import com.github.chen0040.magento.models.ProductPage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MagentoProductManager extends MagentoHttpComponent {

   private final MagentoClient client;
   private static final String RELATIVE_PATH_4_PRODUCTS = "rest/V1/products";
   private static final String RELATIVE_PATH_4_PRODUCT_TYPES = "/rest/V1/products/types";
   private static final String SEARCH_CRITERIA_CURRENT_PAGE = "?searchCriteria[currentPage]=";
   private static final String SEARCH_CRITERIA_PAGE_SIZE = "&searchCriteria[pageSize]=";

   public MagentoProductManager(MagentoClient client) {
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


   public ProductPage page(int pageIndex, int pageSize) {
      String uri = baseUri() + "/" + RELATIVE_PATH_4_PRODUCTS
              + SEARCH_CRITERIA_CURRENT_PAGE + pageIndex
              + SEARCH_CRITERIA_PAGE_SIZE + pageSize;
      String json = getSecure(uri);
      if(!validate(json)){
         return null;
      }
      return JSON.parseObject(json, ProductPage.class);
   }



   public Product getProductBySku(String sku) {
      String uri = baseUri() + "/" + RELATIVE_PATH_4_PRODUCTS + "/" + escape(sku);
      String json = getSecure(uri);

      if(!validate(json)){
         return null;
      }
      log.info("Output: {}", json);
      return JSON.parseObject(json, Product.class);
   }

   public List<MagentoAttributeType> getProductAttributeTypes() {
      String uri = baseUri() + "/rest/V1/products/attributes/types";

      String json = getSecure(uri);

      if(!validate(json)) {
         return Collections.emptyList();
      }
      return JSON.parseArray(json, MagentoAttributeType.class);
   }

   public ProductAttributePage getProductAttributes(int pageIndex, int pageSize){
      String uri = baseUri() + "/rest/V1/products/attributes"
              + SEARCH_CRITERIA_CURRENT_PAGE + pageIndex
              + SEARCH_CRITERIA_PAGE_SIZE + pageSize;

      String json = getSecure(uri);

      if(!validate(json)) {
         return null;
      }

      return JSON.parseObject(json, ProductAttributePage.class);

   }

   public boolean hasProduct(String sku) {
      return getProductBySku(sku) != null;
   }

   public Product saveProduct(Product product){
      String sku = product.getSku();
      String url = baseUri() + "/" + RELATIVE_PATH_4_PRODUCTS + "/" + escape(sku);

      Map<String, Object> detail = new HashMap<>();

      detail.put("sku", product.getSku());
      detail.put("name", product.getName());
      detail.put("price", product.getPrice());
      detail.put("status", product.getStatus());
      detail.put("type_id", product.getType_id());
      detail.put("attribute_set_id", product.getAttribute_set_id());
      detail.put("weight", product.getWeight());
      detail.put("visibility", product.getVisibility());
//      detail.put("status", product.getStatus());

      Map<String, Object> req = new HashMap<>();
      req.put("product", detail);

      String body = JSON.toJSONString(req, SerializerFeature.PrettyFormat);
      log.info("posting:\r\n{}", body);
      String json = putSecure(url, body);

      if(!validate(json)){
         return null;
      }
      return JSON.parseObject(json, Product.class);
   }


   public String page(String name, String value, String condition_type) {
      String uri = baseUri() + "/" + RELATIVE_PATH_4_PRODUCTS
              + "?searchCriteria[filter_groups][0][filters][0][field]=category_gear"
              + "&searchCriteria[filter_groups][0][filters][0][value]=86"
              + "&searchCriteria[filter_groups][0][filters][0][condition_type]=finset";
      return getSecure(uri);
   }

   public List<MagentoType> listProductTypes() {
      String uri = baseUri() + RELATIVE_PATH_4_PRODUCT_TYPES
          + SEARCH_CRITERIA_CURRENT_PAGE + "0"
          + SEARCH_CRITERIA_PAGE_SIZE + "1000";
      String json = getSecure(uri);
      return JSON.parseArray(json, MagentoType.class);
   }

   public List<MagentoType> listProductTypes(int page, int pageSize) {
      String uri = baseUri() + RELATIVE_PATH_4_PRODUCT_TYPES
              + SEARCH_CRITERIA_CURRENT_PAGE + page
              + SEARCH_CRITERIA_PAGE_SIZE + pageSize;
      String json = getSecure(uri);
      return JSON.parseArray(json, MagentoType.class);
   }

   public String deleteProduct(String sku) {
      String url = baseUri() + "/" + RELATIVE_PATH_4_PRODUCTS + "/" + escape(sku);
      return deleteSecure(url);
   }


}
