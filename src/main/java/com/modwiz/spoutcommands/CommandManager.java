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
