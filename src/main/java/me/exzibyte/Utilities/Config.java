package me.exzibyte.Utilities;

import me.exzibyte.Singularity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Config {

    private final Singularity singularity;
    Logging logging = new Logging();
    public HashMap<String, String> configuration = new HashMap<>();
    public HashMap<String, JSONArray> conf = new HashMap<>();
    private static HashMap<String, String> locales = new HashMap<>();

    public Config(Singularity singularity){
        this.singularity = singularity;
    }

    public void load(){
        JSONParser parser = new JSONParser();
        try(FileReader reader = new FileReader("config.json")){
            JSONObject obj = (JSONObject) parser.parse(reader);

            configuration.put("token", obj.get("token").toString());
            configuration.put("dbURI", obj.get("databaseURI").toString());

        } catch(IOException | ParseException exception){
            logging.error(this.getClass(), exception.getMessage());
        }
    }

    public String get(String key){
        return configuration.get(key);
    }


}
