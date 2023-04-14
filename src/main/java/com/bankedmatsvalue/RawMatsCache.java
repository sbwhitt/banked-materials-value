package com.bankedmatsvalue;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class RawMatsCache {

    @AllArgsConstructor
    class RawMatData {
        public int id;
        public String name;
        public int amount;
    }

    @AllArgsConstructor
    class RawMatContainer {
        List<RawMatData> mats;
    }

    private static HashMap<Integer, RawMatData> cache = new HashMap<>();

    @Inject
    public RawMatsCache() {
        populateCache();
    }

    public void populateCache() {
        final Gson gson = new Gson();
        final InputStream data = RawMatsCache.class.getResourceAsStream("/raw_mats_data.json");

        RawMatContainer matsContainer = gson.fromJson(new InputStreamReader(data, StandardCharsets.UTF_8), RawMatContainer.class);
        for (int i = 0; i < matsContainer.mats.size(); i++) {
            cache.put(matsContainer.mats.get(i).id, matsContainer.mats.get(i));
        }
    }

    public RawMatData getRawMat(int id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        return null;
    }
}
