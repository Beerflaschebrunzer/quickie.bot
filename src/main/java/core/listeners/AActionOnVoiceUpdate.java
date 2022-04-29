package core.listeners;

import discord4j.core.event.domain.VoiceStateUpdateEvent;
import reactor.core.publisher.Mono;

public abstract class AActionOnVoiceUpdate implements IActionOnVoiceUpdate
{
    public Mono<Void> parseAndExecute(VoiceStateUpdateEvent event)
    {
        reset();
        init();

        return execute(event);
    }

    protected void reset() {}
    protected void init() {}
    public abstract Mono<Void> execute(VoiceStateUpdateEvent event);
}
