package core.config;

public class Configuration
{
    private static Configuration configuration;

    public String token = "";

    public long supportActionVoiceChannel = 0;
    public long supportActionLogChannel = 0;
    public long supportTicketCategory = 0;
    public long supportPingRole = 0;

    public static Configuration value()
    {
        if (configuration == null) configuration = new Configuration();
        return configuration;
    }

    public static void set(Configuration newConfiguration)
    {
        if (newConfiguration != null) configuration = newConfiguration;
        else configuration = new Configuration();
    }
}
