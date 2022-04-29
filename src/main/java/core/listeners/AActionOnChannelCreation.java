package core.listeners;

import discord4j.core.event.domain.channel.TextChannelCreateEvent;
import reactor.core.publisher.Mono;

public abstract class AActionOnChannelCreation implements IActionOnChannelCreation
{
    public Mono<Void> parseAndExecute(TextChannelCreateEvent event)
    {
        reset();
        init();

        return execute(event);
    }

    protected void reset() {}
    protected void init() {}
    public abstract Mono<Void> execute(TextChannelCreateEvent event);
}

