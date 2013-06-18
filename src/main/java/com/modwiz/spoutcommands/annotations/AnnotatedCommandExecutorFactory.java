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
                    cmd.initializePluginCommand(pluginCommand);
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
