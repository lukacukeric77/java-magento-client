package com.github.chen0040.magento.models;

import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class ProductJsonUnitTest {



    @Test
    public void testJsonDeserialization() {
        String json = readStream(ProductJsonUnitTest.class.getClassLoader().getResourceAsStream("product.json"));
        log.info("json: {}", json);
        Product product = JSON.parseObject(json, Product.class);

        log.info("sku: {}", product.getSku());
        for(MagentoAttribute ma : product.getCustom_attributes()){
            log.info("custom attribute: key = {}, value = {}", ma.getAttribute_code(), ma.getValue());
        }
        for(MagentoAttribute ma : product.getExtension_attributes()){
            log.info("extension attribute: key = {}, value = {}", ma.getAttribute_code(), ma.getValue());
        }
    }

    private String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(is))){
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }catch(IOException ioex){
            log.error("Failed to read stream", ioex);
        }
        return sb.toString();
    }

}
