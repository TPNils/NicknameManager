package be.spyproof.nickmanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Optional;

/**
 * Created by Spyproof on 15/11/2016.
 */
public interface ICommand extends CommandExecutor
{
    String[] getKeys();
    Optional<ICommand> getParent();
    void setParent(ICommand parent);
    void sendHelpMsg(CommandSender src);

    default boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        try{
            execute(commandSender, s, strings);
        }catch (CommandException e) {
            commandSender.sendMessage(e.getMessage().split("\\n"));
        }
        return true;
    }

    void execute(CommandSender src, String cmd, String[] args) throws CommandException;
}
