package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.checks.IPermissionCheck;
import be.spyproof.nickmanager.controller.IBukkitNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class CheckSelfCmd extends AbstractPlayerCmd implements IPermissionCheck {

  public CheckSelfCmd(MessageController messageController, IBukkitNicknameController playerController, String... keys) {
    super(messageController, playerController, keys);
  }

  @Override
  public void sendHelpMsg(CommandSender src) {
    src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.NICK_CHECK));
  }

  @Override
  public void execute(Player src, String cmd, String[] args) {
    checkPermission(src, Reference.Permissions.GENERIC_PLAYER_COMMANDS);

    NicknameData player = super.playerController.wrapPlayer(src);
    src.sendMessage(TemplateUtils.apply(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_CHECK), player).split("\\n"));
  }

}
