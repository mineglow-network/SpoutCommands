package com.modwiz.spoutcommands.annotations;

import com.modwiz.spoutcommands.*;
import com.modwiz.spoutcommands.Command;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Allows for method registration of commands.
 */
public class AnnotatedCommandExecutor implements Executor {
    private final Map<com.modwiz.spoutcommands.Command, Method> commandMap;
    private final Object instance;
    private final CommandManager cmdManager;

    public AnnotatedCommandExecutor(CommandManager cmdManager, Object instance, Map<com.modwiz.spoutcommands.Command, Method> commandMap) {
        this.cmdManager = cmdManager;
        this.instance = instance;
        this.commandMap = commandMap;
    }

    public void execute(CommandSource source, com.modwiz.spoutcommands.Command command, CommandArguments args) throws CommandException{
        Method method = commandMap.get(command);
        if (method != null) {
            method.setAccessible(true);
            try {
                method.invoke(instance, source, args);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause == null) {
                    throw new CommandException(e.getMessage());
                }

                if (cause instanceof CommandException) {
                    throw (CommandException) cause;
                }

                throw new CommandException(e.getMessage(), e.getCause());
            } catch (IllegalAccessException e) {
                throw new CommandException(e.getMessage(), e.getCause());
            }
        }
    }
}
