package com.github.magento.models;


import lombok.Getter;
import lombok.Setter;


/**
 * Created by xschen on 11/7/2017.
 */
@Getter
@Setter
public class CartTotalSegment {


   private String code =  "" ;
   private String title =  "" ;
   private double value =  0;
   private String area =  "" ;
   private CartTotalSegmentExtensionAttributes extension_attributes =  new CartTotalSegmentExtensionAttributes();

}
