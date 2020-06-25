package me.koply.botbase.data;

import me.koply.botbase.App;
import me.koply.botbase.utilities.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Koply
 * @since 11/06/2020
 */
public final class DataManager {

    private final File dataFile;

    public DataManager() {
        String dataFilePath = ConfigManager.getInstance().getDataFilePath();
        dataFile = new File(dataFilePath);
        dataFileGen(dataFile);
        App.logger.info("Data file path: " + dataFilePath);
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveDatas, "Shutdown-thread"));
    }

    private void dataFileGen(File file) {
        if (!dataFile.exists()) {
            try {
                App.logger.info("Data file doesn't exists. Creating...");
                if (dataFile.createNewFile()) App.logger.info("Data file created.");
            } catch (IOException ex) {
                App.logger.info("Exception: " + ex.getMessage());
            }
        }
    }

    private final HashMap<String, GuildData> guildDatas = new HashMap<>();
    public final HashMap<String, GuildData> getGuildDatas() { return guildDatas; }
    public final GuildData getGuildData(String id) {
        if (!guildDatas.containsKey(id)) {
            guildDatas.put(id, new GuildData().setGuildId(id));
        }
        return guildDatas.get(id);
    }

    public final void initDatas() {
        App.logger.info("Data file is reading...");
        String dataFileStr = Util.readAll(dataFile);
        if (dataFileStr == null || dataFileStr.equals("")) {
            App.logger.info("Data file is empty.");
            return;
        }


        JSONObject dataJson = new JSONObject(dataFileStr);
        JSONArray jsonGuildDatas = dataJson.getJSONArray("guildDatas");
        for (Object o : jsonGuildDatas) {
            GuildData innerData = new GuildData().toGuildData((JSONObject) o);
            guildDatas.put(innerData.getGuildId(), innerData);
        }

        /*
         * TODO: JSONArray allUserData = dataJson.getJSONArray("userDatas")
         */
        App.logger.info("Data file is loaded!");
    }

    public void saveDatas() {
        JSONObject json = new JSONObject();

        JSONArray jsonGuildDatas = new JSONArray();
        for (Map.Entry<String, GuildData> entry : guildDatas.entrySet()) {
            jsonGuildDatas.put(entry.getValue().toJson());
        }

        json.put("guildDatas", jsonGuildDatas);

        Util.writeToFile(dataFile, json.toString());
        /*
         * TODO: JSONArray allUserData = dataJson.getJSONArray("userDatas")
         */
    }

    // instance
    private static DataManager instance;
    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }
}