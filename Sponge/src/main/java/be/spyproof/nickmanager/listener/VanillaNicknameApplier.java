package be.spyproof.nickmanager.listener;

import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.model.PlayerData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Optional;

/**
 * Created by Spyproof on 30/10/2016.
 *
 * The nickname applier will listen to all chat messages.
 * If the sender has a nickname, the header will be reset and replace with its nickname.
 *
 * The new header will have the same layout as the default minecraft header.
 */
public class VanillaNicknameApplier
{
    private ISpongePlayerController playerController;

    public VanillaNicknameApplier(ISpongePlayerController playerController)
    {
        this.playerController = playerController;
    }

    @Listener(order = Order.LATE)
    public void onMessageEvent(MessageChannelEvent.Chat event)
    {
        Optional<Player> player = event.getCause().first(Player.class);

        if (!player.isPresent())
            return;

        PlayerData playerData = this.playerController.wrapPlayer(player.get());

        if (!event.getFormatter().getHeader().isEmpty() && playerData.getNickname().isPresent())
        {
            Text.Builder builder = Text.builder();

            Text header = event.getFormatter().getHeader().format();
            String rawHeader = TextSerializers.FORMATTING_CODE.serialize(header);
            if (rawHeader.contains(player.get().getName()))
                rawHeader = rawHeader.replace(player.get().getName(), playerData.getNickname().get() + "&r");

            builder.append(Text.of(TextSerializers.FORMATTING_CODE.deserialize(rawHeader)));

            Optional<ClickAction<?>> clickAction = header.getClickAction();
            if (clickAction.isPresent())
                builder.onClick(clickAction.get());
            else
                builder.onClick(TextActions.suggestCommand("/msg " + player.get().getName() + " "));

            Optional<HoverAction<?>> hoverAction = header.getHoverAction();
            if (clickAction.isPresent())
                builder.onHover(hoverAction.get());
            else
                builder.onHover(TextActions.showEntity(player.get().getUniqueId(), player.get().getName()));

            event.getFormatter().setHeader(builder.build());
        }
    }
}
