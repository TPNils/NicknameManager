package be.spyproof.nickmanager.commands;

import be.spyproof.nickmanager.controller.IBukkitNicknameController;
import be.spyproof.nickmanager.controller.MessageController;

import java.util.Optional;

/**
 * Created by Spyproof on 14/11/2016.
 */
public abstract class AbstractCmd implements ICommand, IMessageControllerHolder, IPlayerControllerHolder {

  protected MessageController messageController;
  protected IBukkitNicknameController playerController;
  protected String[] keys;
  protected ICommand parent;

  protected AbstractCmd(MessageController messageController, IBukkitNicknameController playerController, String... keys) {
    this.messageController = messageController;
    this.playerController = playerController;
    this.keys = keys;
  }

  @Override
  public String[] getKeys() {
    return this.keys;
  }

  public Optional<ICommand> getParent() {
    return Optional.ofNullable(this.parent);
  }

  public void setParent(ICommand parent) {
    this.parent = parent;
  }

  @Override
  public IBukkitNicknameController getPlayerController() {
    return this.playerController;
  }

  @Override
  public MessageController getMessageController() {
    return this.messageController;
  }

}
