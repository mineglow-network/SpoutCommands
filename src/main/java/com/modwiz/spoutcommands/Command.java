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

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.*;

/**
 * Created by starb_000 on 6/13/13.
 */
public class Command {
    private final String name;
    private final List<String> aliases = new ArrayList<String>();
    private final Set<Command> children = new HashSet<Command>();
    private String help, usage, permission;
    private int minArgs = 0, maxArgs = -1;
    private Executor executor;

    protected Command(String name, String... names) {
        this.name = name;
        aliases.addAll(Arrays.asList(names));
        aliases.add(name);
    }

    /**
     * Executes the command in the executor it is currently set to.
     */
    public void execute(CommandSource source, String... args) throws CommandException {
        execute(source, new CommandArguments(args));
    }

    /**
     * Executes the command in the executor it is currently set to.
     */
    public void execute(CommandSource source, CommandArguments args) throws CommandException {
        if (executor == null) {
            throw new CommandException("Command exists but no executor has been set.");
        }

        if (permission != null && !source.hasPermission(permission)) {
            throw new CommandException("You do not have permission to execute this command.");
        }

        // check arguments count
        int len = args.length();
        if (len < minArgs) {
            source.sendMessage("Not enough arguments. (minimum " + minArgs + ")");
            throw new CommandException(getUsage());
        } else if (maxArgs >= 0 && len > maxArgs) { // Less than 0 is considered infinite
            source.sendMessage("Too many arguments. (maximum " + maxArgs + ")");
            throw new CommandException(getUsage());
        }

        // execute a child if applicable
        if (args.length() > 0) {
            String childRoot = args.getString(0);
            List<String> childArgs = new ArrayList<String>(args.get());
            childArgs.remove(0);
            for (Command child : children) {
                for (String alias : child.getAliases()) {
                    if (alias.equalsIgnoreCase(childRoot)) {
                        child.execute(source, new CommandArguments(childArgs));
                        return;
                    }
                }
            }
        }

        // no child found, try to execute.
        executor.execute(source, this, args);
    }

    /**
     * Returns the {@link Executor} associated with this command.
     */
    public Executor getExecutor() {
        return executor;
    }

    /**
     * Sets the {@link Executor} associated with this command.
     */
    public Command setExecutor(Executor executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Returns a set of all the command's children
     */
    public Set<Command> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    /**
     * Returns a child command with the specified name. Will try to create a
     * new command unless otherwise specified.
     */
    public Command getChild(String name, boolean createIfAbsent) {
        for (Command child : children) {
            for (String alias : child.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return child;
                }
            }
        }

        Command command = null;

        if (createIfAbsent) {
            children.add(command = new Command(name));
        }

        return command;
    }
    /**
     * Returns a new child command with the specified name. Will try to create a
     * new command unless otherwise specified
     */
    public Command getChild(String name) {
        return getChild(name, true);
    }

    /**
     * Returns all the names that this command is recognized under.
     */
    public List<String> getAliases() {
        return Collections.unmodifiableList(aliases);
    }

    /**
     * Adds a name under which this command is recognized
     */
    public Command addAlias(String... alias) {
        aliases.addAll(Arrays.asList(alias));
        return this;
    }

    /**
     * Remove a name under which this command is is recognized
     */
    public Command removeAlias(String... alias) {
        aliases.removeAll(Arrays.asList(alias));
        return this;
    }

    /**
     * Returns the help information for this command.
     */
    public String getHelp() {
        return help;
    }

    /**
     * Set the help information for this command.
     */
    public Command setHelp(String help) {
        this.help = help;
        return this;
    }

    /**
     * Get the correct usage for this command.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Set the correct usage for this command.
     */
    public Command setUsage(String usage) {
        this.usage = usage;
        return this;
    }

    /**
     * Returns the permission node required to execute this command.
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Set the permission node required to execute this command.
     */
    public Command setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * Set the maximum and minimum arguments for this command
     */
    public Command setArgumentBounds(int maxArgs, int minArgs) {
        this.maxArgs = maxArgs;
        this.minArgs = minArgs;
        return this;
    }

    /**
     * Get the maximum arguments for this command
     */
    public int getMaxArguments() {
        return maxArgs;
    }

    /**
     * Get the minimum arguments for this command
     */
    public int getMinArguments() {
        return minArgs;
    }

    /**
     * Get the primary alias for this command
     */
    public String getName() {
        return name;
    }

    /**
     * Applies info to an {@link org.bukkit.command.PluginCommand} object.
     */
    public void initializePluginCommand(PluginCommand cmd) {
        cmd.setUsage(getUsage());
        cmd.setDescription(getHelp());
        cmd.setPermission(getPermission());
        cmd.setAliases(getAliases());
        cmd.setExecutor(new CommandExecutor() {

			public boolean onCommand(CommandSender sender,
					org.bukkit.command.Command command, String label,
					String[] args) {
				execute(new WrappedCommandSource(sender), args);
				return true;
			}
		});
    }
}
