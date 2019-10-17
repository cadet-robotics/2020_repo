package frc.robot.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Provides a base for config loading classes
 * Technically based off of the Controls class, but drastically modified
 *
 * Deprecated in favor of ConfigUtil.loadAll()
 *
 * @author Owen Avery
 */
@Deprecated
public abstract class ConfigHandler {
    private static final boolean DEBUG_DEFAULT = true;

    private JsonObject configJSON;
    private String subItemName;
    private boolean hasBeenInit = false;

    /**
     * The default constructor
     *
     * @param configIn The main robot config as a JsonObject
     * @param subItemNameIn The name of the object our config is stored in, used to read from configIn
     */
    public ConfigHandler(JsonObject configIn, String subItemNameIn) {
        this.configJSON = configIn;
        this.subItemName = subItemNameIn;
    }

    public void finishInit() {
        if (hasBeenInit) return;
        hasBeenInit = true;
        if (isDebug()) System.out.println("[INIT] Loading " + getClass().getSimpleName());

        JsonElement sube = configJSON.get(subItemName);
        if ((sube == null) || !sube.isJsonObject()) {
            error();
            return;
        }
        JsonObject subconfig = sube.getAsJsonObject();

        //Debug - outputs all the json
        if(isDebug()){
            for(String s : configJSON.keySet()){
                System.out.println(s + ": " + configJSON.get(s));
            }
        }

        for(String k : subconfig.keySet()){
            if(k.equals("desc") || k.contains("placeholder")) continue;
            loadItem(k, subconfig.get(k));
        }
    }

    /**
     * Initializes this class' objects/data/whatever
     *
     * @param k The name of the object we're loading
     * @param v The config element for this item
     */
    public abstract void loadItem(String k, JsonElement v);

    /**
     * Called on an error with JSON parsing
     *
     * Only called if there is no config item for us
     * EX: The config we're passed doesn't have a JsonObject under the key "dio"
     */
    public abstract void error();

    /**
     * Returns whether debug is enabled
     * Used for logging
     *
     * @return whether debug is on
     */
    public boolean isDebug() {
        return DEBUG_DEFAULT;
    }
}