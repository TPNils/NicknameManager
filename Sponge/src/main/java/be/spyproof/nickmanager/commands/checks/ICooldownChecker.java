package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.DateUtil;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.SpongeUtils;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Map;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface ICooldownChecker extends IMessageControllerHolder
{
    default void checkCooldown(NicknameData nicknameData, Player src) throws CommandException
    {
        if (!SpongeUtils.INSTANCE.canChangeNickname(nicknameData, src))
        {
            long cooldown;
            if (src.hasPermission(Reference.Permissions.BYPASS_COOLDOWN))
            {
                cooldown = 0;
            } else
            {
                cooldown = SpongeUtils.INSTANCE.getDefaultCooldown();
                for (Map.Entry<String, Long> entry : SpongeUtils.INSTANCE.getExtraCooldowns().entrySet())
                {
                    if (src.hasPermission(Reference.Permissions.COOLDOWN_PREFIX + entry.getKey()) && entry
                            .getValue() < cooldown)
                        cooldown = entry.getValue();
                }
            }

            long timeDiff = nicknameData.getLastChanged() + cooldown - System.currentTimeMillis();
            if (timeDiff > 0)
            {
                throw new CommandException(
                        this.getMessageController()
                            .getMessage(Reference.ErrorMessages.ON_COOLDOWN)
                            .apply(TemplateUtils.getParameters("time", DateUtil.timeformat(timeDiff))).build());
            }
        }
    }
}
