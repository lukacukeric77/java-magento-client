package com.github.magento.models;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xschen on 12/6/2017.
 */
@Setter
@Getter
public class ProductPage {
   private List<Product> items = new ArrayList<>();
   private int total_count = 1000;
   private SearchCriteria search_criteria = new SearchCriteria();
}
