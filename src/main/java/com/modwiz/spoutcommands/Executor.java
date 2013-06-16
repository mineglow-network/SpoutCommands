package com.modwiz.spoutcommands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Represents something that handles Command execution.
 */
public interface Executor {
    /**
     * Executes the command as per the implementation's specifications.
     * @param source of the command
     * @param command to handle
     * @param args arguments of the command
     */
    public void execute(CommandSource source, Command command, CommandArguments args);
}
