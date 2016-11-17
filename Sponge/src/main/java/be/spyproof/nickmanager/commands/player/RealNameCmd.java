package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.checks.IArgumentChecker;
import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Spyproof on 31/10/2016.
 */
public class RealNameCmd extends AbstractCmd implements IArgumentChecker
{
    private static final String ARG = "nickname";

    private RealNameCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        String nickname = getArgument(args, ARG);
        List<PlayerData> matches = this.getPlayerController().getPlayerByNickname(TextSerializers.FORMATTING_CODE.stripCodes(nickname));

        Text.Builder players = Text.builder();
        if (matches.size() > 0)
        {
            for (int i = 0; i < matches.size(); i++)
            {
                players.append(Text.of(matches.get(i).getName())
                                   .toBuilder()
                                   .onHover(TextActions.showText(Text.of(matches.get(i).getUuid().toString())))
                                   .build());
                if (i + 1 < matches.size())
                    players.append(Text.NEW_LINE);

            }
        } else
        {
            players.append(Text.of("None"));
        }

        Map<String, Text> params = new HashMap<>();
        params.putAll(TemplateUtils.getParameters("nickname", TextSerializers.FORMATTING_CODE.deserialize(nickname)));
        params.putAll(TemplateUtils.getParameters("players", players.build()));
        src.sendMessage(this.getMessageController().getMessage(Reference.SuccessMessages.NICK_REAL_NAME).apply(params).build());

        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongePlayerController playerController)
    {
        return CommandSpec.builder()
                          .executor(new RealNameCmd(messageController, playerController))
                          .arguments(GenericArguments.string(Text.of(ARG)))
                          .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                          .build();
    }
}
