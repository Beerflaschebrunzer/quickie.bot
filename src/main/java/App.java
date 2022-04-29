
import core.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    public static void main(String[] args)
    {
        Logger logger = LoggerFactory.getLogger(App.class);

        logger.info("Started the console");

        Bot bot = Bot.value();

        bot.start();
        logger.info("I'm out !");
    }
}

