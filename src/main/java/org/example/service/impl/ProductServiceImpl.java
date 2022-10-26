package org.example.service.impl;

import org.example.lib.Component;
import org.example.lib.Inject;
import org.example.model.Product;
import org.example.service.FileReaderService;
import org.example.service.ProductParser;
import org.example.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductServiceImpl implements ProductService {
    @Inject
    private ProductParser productParser;
    @Inject
    private FileReaderService fileReaderService;

    @Override
    public List<Product> getProductsFromFile(String filePath) {
        return fileReaderService.readFile(filePath)
                .stream()
                .map(productParser::parse)
                .collect(Collectors.toList());
    }
}
