package com.github.chen0040.magento.services;


import com.github.chen0040.magento.utils.HttpClient;

import java.util.Map;



public class BasicHttpComponent implements HttpComponent {
   @Override
   public String post(String url, String body, Map<String, String> headers){
      return HttpClient.post(url, body, headers);
   }


   @Override public String put(String url, String body, Map<String, String> headers) {
      return HttpClient.put(url, body, headers);
   }


   @Override public String delete(String url, Map<String, String> headers) {
      return HttpClient.delete(url, headers);
   }


   @Override public String get(String uri, Map<String, String> headers) {
      return HttpClient.get(uri, headers);
   }


   @Override public String jsonPost(String uri, Map<String, String> data) {
      return HttpClient.jsonPost(uri, data);
   }
}
