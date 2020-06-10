package me.koply.botbase.data;

import me.koply.botbase.App;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Koply
 * @since 03/06/2020
 */
public final class ConfigManager {
    // variables
    private String prefix = "$$";
    private String token = "--";
    private int cooldown = 5000;
    private List<String> ownerList = new ArrayList<>();
    private Map<String, String> groupReplaceList = new HashMap<>();

    // getter methods
    public final String getPrefix() { return prefix; }
    public final String getToken() { return token; }
    public final int getCooldown() { return cooldown; }
    public final List<String> getOwnerList() { return ownerList; }
    public final Map<String, String> getGroupReplaceList() { return groupReplaceList; }

    public final ConfigManager loadConfig() {
        String config = readAll(new File(ConfigManager.class.getResource("/config.json").getFile()));

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

        App.logger.info("Config loaded.");
        return instance;
    }

    private String readAll(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            line = sb.toString();
            br.close();
            return line;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "null";
        }
    }

    // INSTANCE
    private static ConfigManager instance;

    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }
}