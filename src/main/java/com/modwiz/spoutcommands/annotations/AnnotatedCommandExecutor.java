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
import org.bukkit.command.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Allows for method registration of commands.
 */
public class AnnotatedCommandExecutor implements Executor {
    private final Map<com.modwiz.spoutcommands.Command, Method> commandMap;
    private final Object instance;

    public AnnotatedCommandExecutor(Object instance, Map<com.modwiz.spoutcommands.Command, Method> commandMap) {
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
