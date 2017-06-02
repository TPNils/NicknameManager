package be.spyproof.nickmanager.listener;

import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.model.PlayerData;
import br.net.fabiozumbi12.UltimateChat.API.SendChannelMessageEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;

import java.util.Optional;

/**
 * Created by Spyproof on 25/04/2017.
 */
public class UltimateChatListener implements EventListener<SendChannelMessageEvent>
{
    private ISpongePlayerController playerController;

    public UltimateChatListener(ISpongePlayerController playerController)
    {
        this.playerController = playerController;
    }

    public void handle(SendChannelMessageEvent event)
    {
        if (event.getSender() instanceof Player)
        {
            PlayerData playerData = playerController.wrapPlayer((Player) event.getSender());
            Optional<String> nickname = playerData.getNickname();

            if (nickname.isPresent())
                event.addTag("{Nickname}", nickname.get());
            else
                event.addTag("{Nickname}", "");
        }
    }
}
