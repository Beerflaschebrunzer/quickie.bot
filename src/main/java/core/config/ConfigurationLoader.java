package core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;

public class ConfigurationLoader
{
    public static void load()
    {
        ObjectMapper mapper = new JsonMapper();
        try {
            Configuration configuration = mapper.readValue(new File("config.json"), Configuration.class);
            Configuration.set(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write()
    {
        Configuration configuration = new Configuration();
        ObjectMapper mapper = new JsonMapper();
        try {
            mapper.writeValue(new File("test.json"), configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
