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
public final class DataManager {
    // variables
    private String prefix = "$$";
    private String token = "--";
    private List<String> ownerList = new ArrayList<>();
    private Map<String, String> groupReplaceList = new HashMap<>();

    // getter methods
    public String getPrefix() { return prefix; }
    public List<String> getOwnerList() { return ownerList; }
    public String getToken() { return token; }
    public Map<String, String> getGroupReplaceList() { return groupReplaceList; }

    public DataManager loadConfig() {
        String config = readAll(new File(DataManager.class.getResource("/config.json").getFile()));

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
    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }
}