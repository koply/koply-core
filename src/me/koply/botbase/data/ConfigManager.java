package me.koply.botbase.data;

import me.koply.botbase.App;
import me.koply.botbase.utilities.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

/**
 * @author Koply
 * @since 03/06/2020
 */
public final class ConfigManager {

    public ConfigManager() {
        loadConfig();
    }

    // variables
    private String prefix = "$$";
    private String token = "--";
    private int cooldown = 5000;
    private String dataFilePath = "data.json";
    private List<String> ownerList = new ArrayList<>();
    private Map<String, String> groupReplaceList = new HashMap<>();

    // getter methods
    public final String getPrefix() { return prefix; }
    public final String getToken() { return token; }
    public final int getCooldown() { return cooldown; }
    public final String getDataFilePath() { return dataFilePath; }
    public final List<String> getOwnerList() { return ownerList; }
    public final Map<String, String> getGroupReplaceList() { return groupReplaceList; }

    public final void loadConfig() {
        String config = Util.readAll(new File(ConfigManager.class.getResource("/config.json").getFile()));

        if (config == null || config.equals("")) {
            App.logger.log(Level.WARNING,"Config file is empty. Bot will be doesn't run normal.");
            return;
        }

        JSONObject jsonObject = new JSONObject(config);
        prefix = jsonObject.get("prefix").toString();
        token = jsonObject.get("token").toString();
        JSONArray array = new JSONArray(jsonObject.get("owners").toString());
        for (Object object : array) {
            String id = (String) object;
            ownerList.add(id);
        }

        JSONObject groupsObject = new JSONObject(jsonObject.get("groups").toString());
        Iterator<String> groupsIterator = groupsObject.keys();
        while (groupsIterator.hasNext()) {
            String key = groupsIterator.next();
            groupReplaceList.put(key, groupsObject.get(key).toString());
        }

        try {
            cooldown = jsonObject.getInt("cooldown");
        } catch (Exception ignored) {
            cooldown = 1000;
        }

        dataFilePath = jsonObject.get("data-file-path").toString();

        App.logger.info("Config loaded.");
    }

    // INSTANCE
    private static ConfigManager instance;
    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }
}