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
import org.bukkit.World;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A wrapper class for the arguments to make parsing easier
 */
public class CommandArguments {
    private final List<String> args;

    public CommandArguments(List<String> args) {
        this.args = args;
    }

    public CommandArguments(String... args) {
        this(Arrays.asList(args));
    }

    /**
     * Returns all the arguments
     */
    public List<String> get() {
        return Collections.unmodifiableList(args);
    }

    /**
     * Returns the length of arguments
     */
    public int length() {
        return args.size();
    }

    private String getString0(int index) {
        if (index >= args.size()) {
            return null;
        }
        return args.get(index);
    }

    /**
     * Returns the string at the specified index
     */
    public String getString(int index) throws CommandException {
        String str = getString0(index);
        if (str == null) {
            throw new CommandException("Specified index is out of bounds. (index " + index + "; size " + args.size() + ")");
        }
        return str;
    }

    private Integer getInteger0(int index) {
        try {
            return Integer.parseInt(getString0(index));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    /**
     * Parses and returns an integer at the specified index.
     */
    public int getInteger(int index) throws CommandException {
        Integer i = getInteger0(index);
        if (i == null) {
            throw new CommandException("Expected integer at index " + index);
        }
        return i;
    }

    /**
     * Returns true if the {@link String} at the specified index is an integer.
     */
    public boolean isInteger(int index) {
        return getInteger0(index) != null;
    }

    private Double getDouble0(int index) {
        try {
            return Double.parseDouble(getString0(index));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parses and returns a double at the specified index.
     */
    public double getDouble(int index) throws CommandException {
        Double d = getDouble0(index);
        if (d == null) {
            throw new CommandException("Expected floating point at index " + index);
        }
        return d;
    }

    /**
     * Returns true if the {@link String} at the specified index is a double.
     */
    public boolean isDouble(int index) {
        return getDouble0(index) != null;
    }

    private Boolean getBoolean0(int index) {
        String str = getString0(index);
        if (!str.equalsIgnoreCase("true") && !str.equalsIgnoreCase("false")) {
            return null;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * Parses and returns a boolean at the specified index.
     */
    public boolean getBoolean(int index) throws CommandException {
        Boolean b = getBoolean0(index);
        if (b == null) {
            throw new CommandException("Expected boolean value at index " + index);
        }
        return b;
    }

    /**
     * Returns true if the string at the specified index is a boolean.
     */
    public boolean isBoolean(int index) {
        return getBoolean0(index) != null;
    }

    /**
     * Returns a string including every argument from the specified index on.
     */
    public String getJoinedString(int index) {
        StringBuilder builder = new StringBuilder();
        for (int i = index; i < args.size(); i++) {
            builder.append(args.get(i));
            if (i + 1 != args.size()) {
                builder.append(' ');
            }
        }
        return builder.toString();
    }

    private Player getPlayer0(int index, boolean exact) {
        if (exact) {
            return Bukkit.getServer().getPlayerExact(getString0(index));
        } else {
            return Bukkit.getServer().getPlayer(getString0(index));
        }
    }

    /**
     * Returns a player at the specified index.
     */
    public Player getPlayer(int index, boolean exact) throws CommandException {
        Player player = getPlayer0(index, exact);
        if (player == null) {
            throw new CommandException("Player not found.");
        }
        return player;
    }

    /**
     * Returns a player at the specified index.
     */
    public Player getPlayer(int index) throws CommandException {
        return getPlayer(index, false);
    }

    /**
     * Returns true if there is an online player's name at the specified index.
     */
    public boolean isPlayer(int index, boolean exact) {
        return getPlayer0(index, exact) != null;
    }

    /**
     * Returns true if there is an online player's name at the specified index.
     */
    public boolean isPlayer(int index) {
        return isPlayer(index, false);
    }

    private World getWorld0(int index) {
        return Bukkit.getServer().getWorld(getString0(index));
    }

    /**
     * Returns the world at the specified index.
     */
    public World getWorld(int index) throws CommandException {
        World world = getWorld0(index);
        if (world == null) {
            throw new CommandException("World not found.");
        }
        return world;
    }

    /**
     * Returns true if the world at the specified index exists.
     */
    public boolean isWorld(int index, boolean exact) {
        return getWorld0(index) != null;
    }

    /**
     * Returns true if the world at the specified index exists.
     */
    public boolean isWorld(int index) {
        return isWorld(index, true);
    }

    /**
     * Returns the arguments in an array.
     */
    public String[] toArray() {
        return args.toArray(new String[args.size()]);
    }

    @Override
    public String toString() {
        return getJoinedString(0);
    }
}
