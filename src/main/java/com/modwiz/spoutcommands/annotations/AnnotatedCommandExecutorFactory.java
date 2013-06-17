package com.modwiz.spoutcommands.annotations;

import com.modwiz.spoutcommands.*;
import com.modwiz.spoutcommands.Command;
import com.modwiz.spoutcommands.annotations.AnnotatedCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by starb_000 on 6/13/13.
 */
public final class AnnotatedCommandExecutorFactory {
    private final CommandManager cmdManager;

    private AnnotatedCommandExecutorFactory(CommandManager cmdManager) {
        this.cmdManager = cmdManager;
    }

    /**
     * Create an instance of the factory from the CommandManager
     */
    public static AnnotatedCommandExecutorFactory getInstance(CommandManager cmdManager) {
        return new AnnotatedCommandExecutorFactory(cmdManager);
    }

    /**
     * Register all the defined commands by the method in this class.
     *
     * @param parent to register commands under.
     */
    public AnnotatedCommandExecutor create(Object instance, Command parent) {
        Map<Command, Method> commandMap = new HashMap<Command, Method>();
        for (Method method : instance.getClass().getMethods()) {
            method.setAccessible(true);
            // check if the method is valid
            if (!isValidMethod(method)) {
                continue;
            }

            // create the command
            Server server = Bukkit.getServer();
            com.modwiz.spoutcommands.annotations.Command a = method.getAnnotation(com.modwiz.spoutcommands.annotations.Command.class);
            Command command;
            if (parent != null) { // parent specified? create child
                command = parent.getChild(a.aliases()[0]);
            } else { // no parent specified? create normal command
                command = cmdManager.getCommand(a.aliases()[0]);
            }

            // set annotation data
            command.addAlias(a.aliases());
            command.setHelp(a.desc());
            command.setUsage(a.usage());
            command.setArgumentBounds(a.max(), a.min());

            // put the command in our map
            commandMap.put(command, method);
        }

        // set the executor of our commands
        AnnotatedCommandExecutor exe = new AnnotatedCommandExecutor(cmdManager, instance, commandMap);
        for (Command cmd : commandMap.keySet()) {
            cmd.setExecutor(exe);

            PluginCommand pluginCommand = Bukkit.getPluginCommand(cmd.getName());
            if (pluginCommand != null) {
                if (parent == null) {
                    pluginCommand.setExecutor(exe);
                }
            }
        }

        return exe;
    }

    private static boolean isValidMethod(Method method) {
        return hasValidModifiers(method) && hasValidArguments(method) && hasCommandAnnotation(method);
    }

    public static boolean hasValidModifiers(Method method) {
        int mod = method.getModifiers();
        return !Modifier.isAbstract(mod) && !Modifier.isPrivate(mod) && !Modifier.isProtected(mod)
                && !Modifier.isStatic(mod) && Modifier.isPublic(mod);
    }

    public static boolean hasValidArguments(Method method) {
        Class<?>[] params = method.getParameterTypes();
        return params.length == 2 && CommandSource.class.equals(params[0]) && CommandArguments.class.equals(params[1]);
    }

    public static boolean hasCommandAnnotation(Method method) {
        return method.isAnnotationPresent(com.modwiz.spoutcommands.annotations.Command.class);
    }
}
