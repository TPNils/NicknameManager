package be.spyproof.nickmanager.commands;

import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface IPlayerControllerHolder
{
    ISpongePlayerController getPlayerController();
}
