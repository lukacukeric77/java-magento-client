package com.github.chen0040.magento.services;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;


public class MagentoCategoryManager extends MagentoHttpComponent {
   private MagentoClient client;
   private static final String relativePath4Categories = "rest/V1/categories";

   public MagentoCategoryManager(MagentoClient client) {
      super(client.getHttpComponent());
      this.client = client;
   }

   public boolean deleteCategory(long categoryId) {
      String url = baseUri() + "/" + relativePath4Categories + "/" + categoryId;
      String json = deleteSecure(url);
      if(!validate(json)){
         return false;
      }
      return json.equalsIgnoreCase("true");
   }

   public long addCategory(Category category) {
      Map<String, Object> cat = new HashMap<>();
      cat.put("id", category.getId());
      cat.put("parent_id", category.getParent_id());
     cat.put("name", category.getName());
     cat.put("is_active", category.is_active());
     cat.put("position", category.getPosition());
     cat.put("level", category.getLevel());
     cat.put("children", "string");
      cat.put("include_in_menu", true);
      cat.put("available_sort_by", new ArrayList<>());
      cat.put("extension_attributes", new ArrayList<>());
      cat.put("custom_attributes", new ArrayList<>());
      Map<String, Object> req = new HashMap<>();
      req.put("category", cat);
      String url = baseUri() + "/" + relativePath4Categories;

      String body = JSON.toJSONString(req, SerializerFeature.BrowserCompatible);
      String json = postSecure(url, body);

      if(!validate(json)){
         return -1;
      }
      return Long.parseLong(json);
   }

   public boolean updateCategory(Category category) {
      Map<String, Object> cat = new HashMap<>();
      cat.put("id", category.getId());
      cat.put("parent_id", category.getParent_id());
      cat.put("name", category.getName());
      cat.put("is_active", category.is_active());
      cat.put("position", category.getPosition());
      cat.put("level", category.getLevel());
      cat.put("children", "string");
      cat.put("include_in_menu", true);
      cat.put("available_sort_by", new ArrayList<>());
      cat.put("extension_attributes", new ArrayList<>());
      cat.put("custom_attributes", new ArrayList<>());
      Map<String, Object> req = new HashMap<>();
      req.put("category", cat);
      String url = baseUri() + "/" + relativePath4Categories + "/" + category.getId();

      String body = JSON.toJSONString(req, SerializerFeature.BrowserCompatible);
      String json = postSecure(url, body);

      if(!validate(json)){
         return false;
      }
      return json.equalsIgnoreCase("true");
   }

   public Category all() {
      int pageIndex = 0;
      int pageSize = 1000;
      String uri = baseUri() + "/" + relativePath4Categories
              + "?searchCriteria[currentPage]=" + pageIndex
              + "&searchCriteria[pageSize]=" + pageSize;
      String json = getSecured(uri);
      if(!validate(json)){
         return null;
      }

      return JSON.parseObject(json, Category.class);
   }

   public Category getCategoryByIdClean(long id) {
      String uri = baseUri() + "/" + relativePath4Categories + "/" + id;
      return getCategoryByUrl(uri);
   }

   public Category getRootCategoryById(long id) {
      String uri = baseUri() + "/" + relativePath4Categories + "?rootCategoryId=" + id;
      return getCategoryByUrl(uri);
   }

   private Category getCategoryByUrl(String uri) {
      String json = getSecured(uri);
      if (!validate(json)) {
        return null;
      }
      return JSON.parseObject(json, Category.class);
   }

   public Category getCategoryByIdWithChildren(long id) {
      Category all = all();
      return getCategoryById(all, id);
   }

   private Category getCategoryById(Category x, long id){
      if(x.getId() == id) {
         return x;
      }
      for(Category child : x.getChildren_data()) {
         Category x_ = getCategoryById(child, id);
         if(x_ != null) {
            return x_;
         }
      }
      return null;
   }

   public List<CategoryProduct> getProductsInCategory(long id) {
      String uri = baseUri() + "/" + relativePath4Categories + "/" + id + "/products";
      String json = getSecured(uri);

      if(!validate(json)) {
         return null;
      }

      return JSON.parseArray(json, CategoryProduct.class);
   }

   public boolean addProductToCategory(long categoryId, String productSku, int position) {
      String uri = baseUri() + "/" + relativePath4Categories + "/" + categoryId + "/products";
      Map<String, Object> req = new HashMap<>();
      Map<String, Object> detail = new HashMap<>();
      detail.put("sku", productSku);
      detail.put("position", position);
      detail.put("category_id", categoryId);
      detail.put("extension_attributes", new HashMap<>());
      req.put("productLink", detail);
      String body = JSON.toJSONString(req, SerializerFeature.BrowserCompatible);
      String json = putSecure(uri, body);

      return json.equals("true");
   }


   @Override public String token() {
      return client.token();
   }


   @Override public String baseUri() {
      return client.baseUri();
   }


   public boolean removeProductFromCategory(long categoryId, String productSku) {
      String uri = baseUri() + "/" + relativePath4Categories + "/" + categoryId + "/products/" + productSku;

      String json = deleteSecure(uri);
      return json.equals("true");
   }
}
