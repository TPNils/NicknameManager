package be.spyproof.nickmanager.util;

import be.spyproof.nickmanager.model.PlayerData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Spyproof on 05/11/2016.
 */
public class TemplateUtils
{
    /**
     * Get all placeholders that can be used for a player.
     *
     * nick_manager:player
     *      see getFormattedPlayer(PlayerData)
     * nick_manager:player.name
     *      The name of the player
     * nick_manager:player.uuid
     *      The uuid of the player
     * nick_manager:player.tokens
     *      The amount of token the player holds
     * nick_manager:player.lastchanged
     *      The last time the player changed their nickname
     * nick_manager:player.nickname
     *      The nickname of the player
     * nick_manager:player.pastNicknames
     *      All nicknames the player had beforehand
     *
     * @param player All placeholder values will be read from the player
     * @return A map with all the placeholder values associated with a player
     */
    public static Map<String, Text> getParameters(PlayerData player)
    {
        Map<String, Text> params = new HashMap<>();

        params.putAll(getParameters("player.name", Text.of(player.getName())));
        params.putAll(getParameters("player.tokens", Text.of(player.getTokensRemaining())));
        params.putAll(getParameters("player.uuid", Text.of(player.getUuid())));
        params.putAll(getParameters("player.lastchanged", Text.of(SimpleDateFormat.getDateInstance().format(new Date(player.getLastChanged())))));
        params.putAll(getParameters("player", getFormattedPlayer(player)));

        if (player.getNickname().isPresent())
            params.putAll(getParameters("player.nickname", formatNickname(player.getNickname().get())));
        else
            params.putAll(getParameters("player.nickname", Text.of("None")));

        if (player.getPastNicknames().size() > 0)
        {
            Text.Builder builder = Text.builder();
            for (int i = 0; i < player.getPastNicknames().size(); i++)
            {
                builder.append(formatNickname(player.getPastNicknames().get(i)));
                if (i + 1 < player.getPastNicknames().size())
                    builder.append(Text.NEW_LINE);
            }
            params.putAll(getParameters("player.pastNicknames", builder.build()));
        } else
        {
            params.putAll(getParameters("player.pastNicknames", Text.of("None")));
        }

        return params;
    }

    /**
     * An easy and standard way to represent a player in text form
     * @param player The player to represent
     * @return Text representing the player
     */
    public static Text getFormattedPlayer(PlayerData player)
    {
        if (player.getNickname().isPresent())
        {
            Text.Builder builder = Text.builder();
            builder.append(Text.of(TextSerializers.FORMATTING_CODE.deserialize(player.getNickname().get())));
            builder.onHover(TextActions.showText(Text.of(TextColors.GOLD, player.getName(),
                                                         Text.NEW_LINE,
                                                         TextColors.YELLOW, player.getUuid())));
            builder.onClick(TextActions.suggestCommand("/msg " + player.getName() + " "));
            return builder.build();
        }
        else
        {
            Text.Builder builder = Text.builder();
            builder.append(Text.of(player.getName()));
            builder.onHover(TextActions.showText(Text.of(TextColors.YELLOW, player.getUuid())));
            builder.onClick(TextActions.suggestCommand("/msg " + player.getName() + " "));
            return builder.build();
        }
    }

    /**
     * @param name The key of the placeholder
     * @param value The value for the placeholder
     * @return A singleton with key nickname_manager:name and value
     */
    public static <T> Map<String, T> getParameters(String name, T value)
    {
        return Collections.singletonMap(Reference.MetaData.PLUGIN_ID + ":" + name, value);
    }

    private static Text formatNickname(String nickname)
    {
        return Text.of(TextColors.WHITE, TextSerializers.FORMATTING_CODE.deserialize(nickname)
                                                                        .toBuilder()
                                                                        .onHover(TextActions.showText(
                                                                                Text.of(TextColors.YELLOW,
                                                                                        TextSerializers.FORMATTING_CODE.stripCodes(nickname),
                                                                                        Text.NEW_LINE,
                                                                                        nickname)))
                                                                        .build());
    }
}
