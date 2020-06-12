package me.koply.botbase.data;

import me.koply.botbase.App;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * @author Koply
 * @since 11/06/2020
 */
public final class GuildData {

    String description = "";
    String guildId = "";

    public final String getDescription() {
        return description;
    }
    public final GuildData setDescription(String desc) {
        description = desc;
        return this;
    }

    public final String getGuildId() { return guildId; }
    public final GuildData setGuildId(String id) { guildId = id; return this; }

    /* ------------ Serialize and Deserialize --------------*/
    public JSONObject toJson() {
        Field[] fields = this.getClass().getDeclaredFields();

        JSONObject json = new JSONObject();

        for (Field field : fields) {
            try {
                json.put(field.getName(), field.get(this));
            } catch (Exception ex) {App.logger.info("toJson Exception: " + ex.getMessage());}
        }

        return json;
    }

    public GuildData toGuildData(JSONObject json) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            String value = json.get(field.getName()).toString();
            if (value == null) continue;

            try {
                field.set(this, value);
            } catch (Exception ex) {
                App.logger.info("GuildDataManager - toGuildData caught an exception in loop. " + ex.getMessage());
            }
        }

        return this;
    }
}