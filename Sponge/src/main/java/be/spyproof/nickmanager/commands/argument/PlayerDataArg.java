package be.spyproof.nickmanager.commands.argument;

import be.spyproof.nickmanager.controller.ISpongeNicknameController;
import be.spyproof.nickmanager.model.NicknameData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class PlayerDataArg extends CommandElement {
  private static final Pattern uuidPattern = Pattern.compile("[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}");
  private ISpongeNicknameController playerController;

  public PlayerDataArg(String key, ISpongeNicknameController playerController) {
    super(Text.of(key));
    this.playerController = playerController;
  }

  @Nullable
  @Override
  protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
    String argument = args.next();
    if (argument == null) {
      return null;
    }

    Optional<? extends NicknameData> playerData;
    if (uuidPattern.matcher(argument).matches()) {
      playerData = this.playerController.getPlayer(UUID.fromString(argument));
    } else {
      playerData = this.playerController.getPlayer(argument);
    }

    return playerData.orElse(null);
  }

  @Override
  public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
    List<String> names = new ArrayList<>();
    if (!args.hasNext())
      return names;

    String peek;
    try {
      peek = args.peek().toLowerCase();
    } catch (ArgumentParseException e) {
      return names;
    }

    for (Player player : Sponge.getServer().getOnlinePlayers())
      if (player.getName().toLowerCase().startsWith(peek))
        names.add(player.getName());

    return names;
  }
}
