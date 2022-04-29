package core;

import core.action.SupportAction;

import core.action.SupportTicketCreation;
import core.config.Configuration;
import core.config.ConfigurationLoader;
import core.listeners.IActionOnChannelCreation;
import core.utils.Toolbox;
import core.listeners.IActionOnVoiceUpdate;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.event.domain.channel.TextChannelCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;

import discord4j.core.object.entity.channel.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class Bot
{
    public static Bot bot;

    private final Map<Long, IActionOnVoiceUpdate> actionsOnVoiceUpdate = new HashMap<>();
    private final Map<Long, IActionOnChannelCreation> actionsOnChannelCreation = new HashMap<>();

    private GatewayDiscordClient client;

    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    public Bot()
    {
        init();
    }

    public void start()
    {
        client = DiscordClientBuilder.create(Configuration.value().token)
                .build()
                .login()
                .block();

        assert client != null;

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    final User self = event.getSelf();
                    LoggerFactory.getLogger(Bot.class).info(
                            "Logged in as {}#{}", self.getUsername(), self.getDiscriminator()
                    );
                });

        client.getEventDispatcher().on(VoiceStateUpdateEvent.class)
                .flatMap(event -> Mono.just(event.getCurrent().getChannelId())
                        .flatMap(channel -> Flux.fromIterable(actionsOnVoiceUpdate.entrySet())
                                .filter(entry -> (event.isJoinEvent() || event.isMoveEvent()) && channel.isPresent() && Toolbox.equals(channel.get().asLong(), entry.getKey()))
                                .flatMap(entry -> entry.getValue().parseAndExecute(event))
                                .doOnError(Throwable::printStackTrace)
                                .next()))
                .subscribe();

        client.getEventDispatcher().on(TextChannelCreateEvent.class)
                .flatMap(event -> Mono.just(event.getChannel().getCategory())
                        .flatMap(category -> {
                            long categoryID = category.block().getId().asLong();
                            return Flux.fromIterable(actionsOnChannelCreation.entrySet())
                                            .filter(entry -> Toolbox.equals(categoryID, entry.getKey()))
                                            .flatMap(entry -> entry.getValue().parseAndExecute(event))
                                            .doOnError(Throwable::printStackTrace)
                                            .next();
                                }
                        ))
                .subscribe();

        client.onDisconnect().block();
    }

    public void init()
    {
        ConfigurationLoader.load();

        registerVoiceAction(new SupportAction());
        registerChannelCreationAction(new SupportTicketCreation());
    }

    public void registerVoiceAction(IActionOnVoiceUpdate action)
    {
        actionsOnVoiceUpdate.put(action.channel(), action);
    }

    private void registerChannelCreationAction(IActionOnChannelCreation action)
    {
        actionsOnChannelCreation.put(action.category(), action);
    }

    public static Bot value()
    {
        if (bot == null) bot = new Bot();
        return bot;
    }
}
