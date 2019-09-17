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
import java.util.regex.Pattern;

/**
 * Created by Spyproof on 15/11/2016.
 */
public class OnlinePlayerNicknameArg extends CommandElement {

  private static final Pattern COLOUR_CODES_PATTERN = Pattern.compile("&[0-9A-FK-OR]");

  private ISpongeNicknameController playerController;

  public OnlinePlayerNicknameArg(String key, ISpongeNicknameController playerController) {
    super(Text.of(key));
    this.playerController = playerController;
  }

  @Nullable
  @Override
  protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
    return args.next();
  }

  @Override
  public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
    List<String> matchingNicknames = new ArrayList<>();
    Optional<String> arg = args.nextIfPresent();
    String nickname = arg.orElse("").toLowerCase();

    for (Player player : Sponge.getServer().getOnlinePlayers()) {
      NicknameData nicknameData = this.playerController.wrapPlayer(player);
      String playerNickname = nicknameData.getNickname().orElse(nicknameData.getName());
      String strippedPlayerNickname = COLOUR_CODES_PATTERN.matcher(playerNickname).replaceAll("");

      if (nickname.equals("") || strippedPlayerNickname.toLowerCase().startsWith(nickname) || playerNickname.toLowerCase().startsWith(nickname)) {
        matchingNicknames.add(strippedPlayerNickname);
      }
    }
    return matchingNicknames;
  }

}
