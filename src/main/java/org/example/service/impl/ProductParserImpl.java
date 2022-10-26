package org.example.service.impl;

import org.example.lib.Component;
import org.example.model.Product;
import org.example.service.ProductParser;

import java.math.BigDecimal;

@Component
public class ProductParserImpl implements ProductParser {
    public static final int ID = 0;
    public static final int NAME = 1;
    public static final int CATEGORY = 2;
    public static final int DESCRIPTION = 3;
    public static final int PRICE = 4;

    @Override
    public Product parse(String productInfo) {
        String[] data = productInfo.split(",");
        Product product = new Product();
        product.setId(Long.valueOf(data[ID]));
        product.setName(data[NAME]);
        product.setCategory(data[CATEGORY]);
        product.setDescription(data[DESCRIPTION]);
        product.setPrice(new BigDecimal(data[PRICE]));
        return product;
    }
}
