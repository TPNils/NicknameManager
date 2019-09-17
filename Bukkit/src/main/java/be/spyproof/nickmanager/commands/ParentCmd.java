package be.spyproof.nickmanager.commands;

import be.spyproof.nickmanager.util.Reference;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class ParentCmd implements ICommand, TabCompleter {

  protected ICommand parent;
  private List<ICommand> children = new ArrayList<>();
  private String[] commands;

  public ParentCmd(String... commands) {
    this.commands = commands;
  }

  public void addChild(ICommand commandExecutor) {
    commandExecutor.setParent(this);
    this.children.add(commandExecutor);
  }

  public void register(JavaPlugin plugin) {
    for (String command : this.commands) {
      PluginCommand cmd = plugin.getCommand(command);
      if (cmd != null) {
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
      }
    }
  }

  @Override
  public String[] getKeys() {
    return this.commands;
  }

  public Optional<ICommand> getParent() {
    return Optional.ofNullable(this.parent);
  }

  public void setParent(ICommand parent) {
    this.parent = parent;
  }

  protected void sendHelpMsgWithHeader(CommandSender src) {
    src.sendMessage(ChatColor.GREEN + "============> " + ChatColor.GOLD + ChatColor.BOLD + Reference.MetaData.PLUGIN_NAME + ChatColor.GREEN + " <============");
    sendHelpMsg(src);
  }

  @Override
  public void sendHelpMsg(CommandSender src) {
    for (ICommand command : this.children) {
      command.sendHelpMsg(src);
    }
  }

  @Override
  public void execute(CommandSender src, String cmd, String[] args) {
    if (args.length > 0) {
      for (String c : commands) {
        if (c.equalsIgnoreCase(cmd)) {
          for (ICommand entry : this.children) {
            for (String sub : entry.getKeys()) {
              if (sub.equalsIgnoreCase(args[0])) {
                entry.execute(src, args[0], Arrays.copyOfRange(args, 1, args.length));
                return;
              }
            }
          }
          break;
        }
      }
    }

    sendHelpMsgWithHeader(src);
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
    List<String> options = new ArrayList<>();

    if (args.length > 1) {
      for (ICommand entry : this.children) {
        if (entry instanceof TabCompleter) {
          for (String sub : entry.getKeys()) {
            if (sub.equalsIgnoreCase(args[0])) {
              options.addAll(((TabCompleter) entry).onTabComplete(commandSender, command, args[0], Arrays.copyOfRange(args, 1, args.length)));
              break;
            }
          }
        }
      }
    } else {
      for (ICommand entry : this.children) {
        for (String sub : entry.getKeys()) {
          if (sub.toLowerCase().contains(args[0].toLowerCase())) {
            options.add(sub);
            break;
          }
        }
      }
    }

    return options;
  }

}
