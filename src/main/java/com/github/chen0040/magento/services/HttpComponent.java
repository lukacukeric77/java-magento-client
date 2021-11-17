package com.github.chen0040.magento.services;


import java.util.Map;


public interface HttpComponent {
   String post(String url, String body, Map<String, String> headers);

   String put(String url, String body, Map<String, String> headers);

   String delete(String url, Map<String, String> headers);

   String get(String uri, Map<String, String> headers);

   String jsonPost(String uri, Map<String, String> data);
}
