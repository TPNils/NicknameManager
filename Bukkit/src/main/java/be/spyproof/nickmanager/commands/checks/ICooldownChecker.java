package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.BukkitUtils;
import be.spyproof.nickmanager.util.DateUtil;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface ICooldownChecker extends IMessageControllerHolder {

  default void checkCooldown(NicknameData nicknameData, CommandSender src) throws CommandException {
    if (!BukkitUtils.INSTANCE.canChangeNickname(nicknameData, src)) {
      long cooldown;
      if (src.hasPermission(Reference.Permissions.BYPASS_COOLDOWN)) {
        cooldown = 0;
      } else {
        cooldown = BukkitUtils.INSTANCE.getDefaultCooldown();
        for (Map.Entry<String, Long> entry : BukkitUtils.INSTANCE.getExtraCooldowns().entrySet()) {
          if (src.hasPermission(Reference.Permissions.COOLDOWN_PREFIX + entry.getKey()) && entry
            .getValue() < cooldown) {
            cooldown = entry.getValue();
          }
        }
      }

      long timeDiff = nicknameData.getLastChanged() + cooldown - System.currentTimeMillis();
      if (timeDiff > 0) {
        throw new CommandException(
          this.getMessageController()
              .getFormattedMessage(Reference.ErrorMessages.ON_COOLDOWN)
              .replace("{time}", DateUtil.timeformat(timeDiff)));
      }
    }
  }

}
