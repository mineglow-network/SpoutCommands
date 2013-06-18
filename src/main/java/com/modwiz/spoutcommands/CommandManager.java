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

import com.modwiz.spoutcommands.annotations.AnnotatedCommandExecutorFactory;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * CommandManager
 */
public class CommandManager {
    private final Set<Command> commands = new HashSet<Command>();

    public CommandManager() {}

    /**
     * Returns a set of all commands
     *
     * @return all the commands
     */
    public Set<Command> getCommands() {
        return Collections.unmodifiableSet(commands);
    }

    /**
     * Returns the command with the specified name/alias or creates a new
     * command unless otherwise specified.
     *
     * @param cmd to create
     * @param createIfAbsent true if create if command not found
     * @return command with speified name
     */
    public Command getCommand(String cmd, boolean createIfAbsent) {
        for (Command c : commands) {
            for (String alias : c.getAliases()) {
                if (cmd.equalsIgnoreCase(alias)) {
                    return c;
                }
            }
        }

        Command command = null;
        if (createIfAbsent) {
            commands.add(command = new Command(cmd));
        }

        return command;
    }

    /**
     * Returns the command with the specified name/alias or creates a new
     * command unless otherwise specified
     *
     * @param cmd to create
     * @return command with specified name
     */
    public Command getCommand(String cmd) {
        return getCommand(cmd, true);
    }

    /**
     * Returns a preinitialized AnnotatedCommandExecutorFactory
     */
    public AnnotatedCommandExecutorFactory getFactory() {
        return AnnotatedCommandExecutorFactory.getInstance(this);
    }
}
