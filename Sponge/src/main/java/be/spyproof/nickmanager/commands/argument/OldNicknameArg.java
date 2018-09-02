package be.spyproof.nickmanager.commands.argument;

import be.spyproof.nickmanager.controller.ISpongeNicknameController;
import be.spyproof.nickmanager.model.NicknameData;
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
public class OldNicknameArg extends CommandElement
{
    private ISpongeNicknameController playerController;

    public OldNicknameArg(String key, ISpongeNicknameController playerController)
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
            NicknameData nicknameData = this.playerController.wrapPlayer((Player) src);
            for (String oldNick : nicknameData.getPastNicknames())
                if (oldNick.replaceAll(Reference.COLOUR_AND_STYLE_PATTERN, "").startsWith(arg.get()) && !options.contains(oldNick))
                    options.add(oldNick);
        }

        return options;
    }
}
