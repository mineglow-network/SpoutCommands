package com.modwiz.spoutcommands;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by starb_000 on 6/13/13.
 */
public class WrappedCommandSource implements CommandSource {
    private final CommandSender sender;

    public WrappedCommandSource(CommandSender sender) {
        super();
        this.sender = sender;
    }

    public CommandSender getSender() {
        return sender;
    }

    public void processCommand(String command, String... args) {
        List<String> commandArgs = Arrays.asList(args);
        String commandLine = command;
        for (int i = 0; i <args.length; i++) {
            commandLine += " " + args[i];
        }
        Bukkit.dispatchCommand(sender, commandLine);
    }

    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    public void sendMessage(String[] messages) {
        sender.sendMessage(messages);
    }

    public Server getServer() {
        return sender.getServer();
    }

    public String getName() {
        return sender.getName();
    }

    public boolean isPermissionSet(String name) {
        return sender.isPermissionSet(name);
    }

    public boolean isPermissionSet(Permission perm) {
        return sender.isPermissionSet(perm);
    }

    public boolean hasPermission(String name) {
        return sender.hasPermission(name);
    }

    public boolean hasPermission(Permission perm) {
        return sender.hasPermission(perm);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return sender.addAttachment(plugin, name, value);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return sender.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return sender.addAttachment(plugin, name, value, ticks);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return sender.addAttachment(plugin, ticks);
    }

    public void removeAttachment(PermissionAttachment attachment) {
        sender.removeAttachment(attachment);
    }

    public void recalculatePermissions() {
        sender.recalculatePermissions();
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return sender.getEffectivePermissions();
    }

    public boolean isOp() {
        return sender.isOp();
    }

    public void setOp(boolean value) {
        sender.setOp(value);
    }

}
