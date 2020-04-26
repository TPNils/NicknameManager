package be.spyproof.nickmanager.listener;

import be.spyproof.nickmanager.controller.ISpongeNicknameController;
import be.spyproof.nickmanager.model.NicknameData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.transform.SimpleTextFormatter;
import org.spongepowered.api.text.transform.TextTemplateApplier;

/**
 * Created by Spyproof on 30/10/2016.
 * <p>
 * The nickname applier will listen to all chat messages.
 * If the sender has a nickname, the header will be reset and replace with its nickname.
 * <p>
 * The new header will have the same layout as the default minecraft header.
 */
public class VanillaNicknameApplier {

  private ISpongeNicknameController playerController;

  public VanillaNicknameApplier(ISpongeNicknameController playerController) {
    this.playerController = playerController;
  }

  @Listener(order = Order.EARLY)
  public void onMessageEvent(MessageChannelEvent.Chat event, @First Player player) {
    NicknameData nicknameData = this.playerController.wrapPlayer(player);

    if (!event.getFormatter().getHeader().isEmpty() && nicknameData.getNickname().isPresent()) {
      Text.Builder builder = Text.of(TextSerializers.FORMATTING_CODE.deserialize(nicknameData.getNickname().get() + "&r")).toBuilder();
      builder.onClick(TextActions.suggestCommand("/msg " + player.getName() + " "));
      builder.onHover(TextActions.showEntity(player.getUniqueId(), player.getName()));

      boolean isVanillaHeader = TextSerializers.FORMATTING_CODE.serialize(event.getFormatter().getHeader().toText()).equals("<" + player.getName() + "> ");

      final Text nickName = builder.build();
      for (SimpleTextFormatter formatter : event.getFormatter()) {
        for (TextTemplateApplier applier : formatter) {
          if (isVanillaHeader) {
            applier.setParameter("header", nickName);
          }
          applier.setParameter("nickname_manager:nickname", nickName);
        }
      }
    }
  }

}
