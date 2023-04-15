package com.bankedmatsvalue;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class ProductsCache {

    @AllArgsConstructor
    class ProductData {
        public int id;
        public String name;
        public String skill;
        public int[] ingredients;
    }

    @AllArgsConstructor
    class ProductsContainer {
        List<ProductData> products;
    }

    public static HashMap<Integer, ProductData> cache = new HashMap<>();

    @Inject
    public ProductsCache() {
        populateCache();
    }

    public void populateCache() {
        final Gson gson = new Gson();
        final InputStream data = ProductsCache.class.getResourceAsStream("/products_data.json");

        ProductsContainer productContainer = gson.fromJson(new InputStreamReader(data, StandardCharsets.UTF_8), ProductsContainer.class);
        for (int i = 0; i < productContainer.products.size(); i++) {
            cache.put(productContainer.products.get(i).id, productContainer.products.get(i));
        }
    }

    public ProductData getProduct(int id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        return null;
    }
}
