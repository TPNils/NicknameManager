package be.spyproof.nickmanager.commands.argument;

import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.Reference;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Spyproof on 15/11/2016.
 */
public class NicknameArg extends CommandElement
{
    private ISpongePlayerController playerController;

    public NicknameArg(String key, ISpongePlayerController playerController)
    {
        super(Text.of(key));
        this.playerController = playerController;
    }

    @Nullable
    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException
    {
        return args.next();
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context)
    {
        List<String> options = new ArrayList<>();
        Optional<String> arg = args.nextIfPresent();
        if (!arg.isPresent())
            return Collections.EMPTY_LIST;

        if (src instanceof Player)
        {
            PlayerData playerData = this.playerController.wrapPlayer((Player) src);
            for (String oldNick : playerData.getPastNicknames())
                if (oldNick.replaceAll(Reference.COLOR_AND_STYLE_PATTERN, "").startsWith(arg.get()) && !options.contains(oldNick))
                    options.add(oldNick);
        }

        return options;
    }
}
