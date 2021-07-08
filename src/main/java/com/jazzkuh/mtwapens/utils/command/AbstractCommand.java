package com.jazzkuh.mtwapens.utils.command;

import com.google.common.collect.ImmutableList;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.Utils;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileFormat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractCommand implements TabExecutor {
    public CommandSender sender;
    public Command command;
    public String commandName;
    public ImmutableList<Object> arguments;

    public AbstractCommand(String commandName) {
        this.commandName = commandName;
        this.arguments = ImmutableList.builder().build();
    }

    public AbstractCommand(String commandName, ImmutableList<Object> arguments) {
        this.commandName = commandName;
        this.arguments = arguments;
    }

    public void register(Main plugin) {
        PluginCommand cmd = plugin.getCommand(commandName);
        if (cmd != null) {
            cmd.setExecutor(this);
        }

        //Commodore
        if (CommodoreProvider.isSupported()) {
            Commodore commodore = CommodoreProvider.getCommodore(plugin);
            try {
                InputStream inputStream = plugin.getResource(commandName + ".commodore");
                if (inputStream == null) return;

                LiteralCommandNode<?> literalCommandNode = CommodoreFileFormat.parse(inputStream);
                commodore.register(cmd, literalCommandNode);
            } catch (IOException ignored) {}
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.command = command;
        this.sender = sender;
        this.execute(sender, command, label, args);
        return true;
    }

    public abstract void execute(CommandSender sender, Command command, String label, String[] args);

    protected boolean senderIsPlayer() {
        return sender instanceof Player;
    }

    protected boolean hasPermission(String permission, boolean silent) {
        if (sender.hasPermission(permission)) {
            return true;
        }

        if (!silent) {
            sender.sendMessage(Utils.color("&cTo use this command, you need permission " + permission + "."));
        }
        return false;
    }

    protected void addIfPermission(Collection<String> options, String permission, String option) {
        if (sender.hasPermission(permission)) {
            options.add(option);
        }
    }

    protected String getBasePermission() {
        return "mtwapens.command." + commandName;
    }

    protected void sendNotEnoughArguments(AbstractCommand command) {
        sender.sendMessage(Utils.color("&8 ----------------------------------------------"));
        sender.sendMessage(Utils.color("&8| &2Commands:"));
        for (Object arg : this.arguments) {
            if (!(arg instanceof Argument)) return;

            Argument argument = (Argument) arg;

            if (argument.getPermission() == null || sender.hasPermission(argument.getPermission())) {
                sender.sendMessage(Utils.color("&8|  &a/" + command.command.getName() + " " + argument.getArguments() + "&8 - &7" + argument.getDescription()));
            }
        }
        sender.sendMessage(Utils.color("&8 ----------------------------------------------"));
    }

    protected List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
        return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
    }
}
