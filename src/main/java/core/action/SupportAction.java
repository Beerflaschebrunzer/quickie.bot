package core.action;

import core.config.Configuration;
import core.listeners.AActionOnVoiceUpdate;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.VoiceChannel;
import reactor.core.publisher.Mono;

/**
 * Notify in a text channel when a user joins a specific voice channel
 */
public class SupportAction extends AActionOnVoiceUpdate
{
    public long channel()
    {
        return Configuration.value().supportActionVoiceChannel;
    }

    private final long logTextChannel = Configuration.value().supportActionLogChannel;
    private final long supportPingRole = Configuration.value().supportPingRole;

    @Override
    public Mono<Void> execute(VoiceStateUpdateEvent event)
    {
        final VoiceChannel voiceChannel = event.getCurrent().getChannel().block();

        event.getCurrent().getMember()
                .flatMap
                    (
                        member -> event.getClient().getChannelById(Snowflake.of(logTextChannel)).cast(MessageChannel.class)
                        // message
                        .flatMap(messageChannel -> messageChannel.createMessage
                            (
                                member.getNicknameMention() + " joined " //user ping
                                        + ((voiceChannel != null) ? voiceChannel.getMention() : "unknown channel (" + channel() + ")") //channel location
                                        + " ||<@&" + supportPingRole + ">||" //hidden ping
                            )
                        )
                    )
                .block();
        return Mono.empty();
    }
}
