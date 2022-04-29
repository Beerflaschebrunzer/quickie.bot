package core.listeners;

import discord4j.core.event.domain.VoiceStateUpdateEvent;
import reactor.core.publisher.Mono;

public interface IActionOnVoiceUpdate
{
    long channel();

    Mono<Void> parseAndExecute(VoiceStateUpdateEvent event);
}
