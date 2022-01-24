package com.github.magento.services;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.github.magento.utils.StringUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class MagentoHttpComponent {

   public abstract String token();
   public abstract String baseUri();

   private static final String AUTHORISATION = "Authorization";
   private static final String BEARER = "Bearer ";
   private static final String CONTENT_TYPE = "Content-Type";
   private static final String APPLICATION_JSON = "application/json";

   protected HttpComponent httpComponent;

   private MagentoHttpComponent() {
   }

   protected MagentoHttpComponent(HttpComponent httpComponent){
      this.httpComponent = httpComponent;
   }

   public HttpComponent getHttpComponent(){
      return httpComponent;
   }

   public String postSecure(String url, String body){
      Map<String, String> headers = getHeaders();
      return httpComponent.post(url, body, headers);
   }

   public String putSecure(String url, String body) {
      Map<String, String> headers = getHeaders();
      return httpComponent.put(url, body, headers);
   }

   public String deleteSecure(String url) {
      Map<String, String> headers = getHeaders();
      return httpComponent.delete(url, headers);
   }

   public String getSecure(String uri) {
      Map<String, String> headers = getHeaders();
      return httpComponent.get(uri, headers);
   }

   public String escape(String text) {
      String result = text;
      try{
         result = URLEncoder.encode(text, "UTF-8");
      }
      catch (UnsupportedEncodingException e) {
         log.error("Failed to escape " + text, e);
      }
      return result;
   }

   protected boolean validate(String json) {
      try {
         Map<String, Object> data = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
         }.getType());

         if (data.containsKey("message")) {
            log.error("query failed: {}", data.get("message"));
            log.warn("trace: {}", data.get("trace"));
            return false;
         }
      } catch(JSONException exception){
         return true;
      }
      return true;
   }

   private Map<String, String> getHeaders() {
      Map<String, String> headers = new HashMap<>();
      if (!StringUtils.isEmpty(this.token())) {
         headers.put(AUTHORISATION, BEARER + this.token());
      }
      headers.put(CONTENT_TYPE, APPLICATION_JSON);
      return headers;
   }

}
