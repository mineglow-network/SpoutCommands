/*
 * This file is part of SpoutAPI.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * SpoutAPI is licensed under the Spout License Version 1.
 *
 * SpoutAPI is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * SpoutAPI is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */

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
