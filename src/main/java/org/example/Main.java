package org.example;

import org.example.lib.Injector;
import org.example.model.Product;
import org.example.service.ProductService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInjector();
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        List<Product> products = productService.getProductsFromFile("products.txt");
        products.forEach(System.out::println);
    }
}
