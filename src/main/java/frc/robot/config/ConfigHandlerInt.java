package frc.robot.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Provides an alternative to ConfigHandler
 *
 * Deprecated in favor of ConfigUtil.loadAllInts()
 *
 * @author Owen Avery
 */
@Deprecated
public abstract class ConfigHandlerInt extends ConfigHandler {
    public ConfigHandlerInt(JsonObject configIn, String subItemName) {
        super(configIn, subItemName);
    }

    @Override
    public void loadItem(String k, JsonElement v) {
        if (!v.isJsonPrimitive() || !((JsonPrimitive) v).isNumber()) return;
        loadItem(k, v.getAsInt());
    }

    public abstract void loadItem(String k, int v);
}