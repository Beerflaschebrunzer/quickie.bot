package core.listeners;

import discord4j.core.event.domain.channel.TextChannelCreateEvent;
import reactor.core.publisher.Mono;

public interface IActionOnChannelCreation
{
    long category();

    Mono<Void> parseAndExecute(TextChannelCreateEvent event);
}
