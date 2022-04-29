package core.action;

import core.config.Configuration;
import core.listeners.AActionOnChannelCreation;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.channel.TextChannelCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import reactor.core.publisher.Mono;

/**
 * A
 */
public class SupportTicketCreation extends AActionOnChannelCreation
{
    @Override
    public long category() {
        return Configuration.value().supportTicketCategory;
    }

    private final long logTextChannel = Configuration.value().supportActionLogChannel;
    private final long supportPingRole = Configuration.value().supportPingRole;

    @Override
    public Mono<Void> execute(TextChannelCreateEvent event) {
        final TextChannel textChannel = event.getChannel();

        event.getClient().getChannelById(Snowflake.of(logTextChannel)).cast(MessageChannel.class)
                .flatMap(channel -> channel.createMessage("Nouveau ticket : " +
                        ((textChannel != null) ? textChannel.getMention() : "deleted channel") //channel location
                        + " ||<@&" + supportPingRole + ">||"))
                .block();
        return Mono.empty();
    }
}
