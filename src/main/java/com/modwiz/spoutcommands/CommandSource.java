package com.modwiz.spoutcommands;

import org.bukkit.command.CommandSender;


public interface CommandSource extends CommandSender {

    /**
     * Processes a command
     */
    public void processCommand(String command, String... args);

    /**
     * Send a message to the client
     */
    public void sendMessage(String message);

    /**
     * Get the commandSender
     */
    public CommandSender getSender();
}
