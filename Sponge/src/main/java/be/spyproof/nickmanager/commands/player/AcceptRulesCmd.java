package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.checks.IPlayerCheck;
import be.spyproof.nickmanager.controller.ISpongeNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Spyproof on 30/10/2016.
 */
public class AcceptRulesCmd extends AbstractCmd implements IPlayerCheck
{
    private AcceptRulesCmd(MessageController messageController, ISpongeNicknameController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        checkIsPlayer(src);

        NicknameData nicknameData = this.getPlayerController().wrapPlayer((Player) src);

        if (!nicknameData.readRules())
            throw new CommandException(this.getMessageController().getMessage(Reference.ErrorMessages.MUST_READ_RULES).apply(TemplateUtils.getParameters("command", "/nick " + Reference.CommandKeys.PLAYER_RULES[0])).build());

        nicknameData.setAcceptedRules(true);
        this.getPlayerController().savePlayer(nicknameData);
        src.sendMessage(this.getMessageController().getMessage(Reference.SuccessMessages.ACCEPTED_RULES).toText());

        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongeNicknameController playerController)
    {
        return CommandSpec.builder()
                          .executor(new AcceptRulesCmd(messageController, playerController))
                          .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                          .build();
    }
}
