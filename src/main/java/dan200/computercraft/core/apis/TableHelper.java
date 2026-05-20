/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.core.apis;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaValues;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static dan200.computercraft.api.lua.LuaValues.getNumericType;

/**
 * Various helpers for tables.
 */
public final class TableHelper {
    private TableHelper() {
        throw new IllegalStateException("Cannot instantiate singleton " + getClass().getName());
    }

    @NotNull
    public static LuaException badKey(@NotNull String key, @NotNull String expected, @Nullable Object actual) {
        return badKey(key, expected, LuaValues.getType(actual));
    }

    @NotNull
    public static LuaException badKey(@NotNull String key, @NotNull String expected, @NotNull String actual) {
        return new LuaException("bad field '" + key + "' (" + expected + " expected, got " + actual + ")");
    }

    public static double getNumberField(@NotNull Map<?, ?> table, @NotNull String key) throws LuaException {
        Object value = table.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else {
            throw badKey(key, "number", value);
        }
    }

    public static int getIntField(@NotNull Map<?, ?> table, @NotNull String key) throws LuaException {
        Object value = table.get(key);
        if (value instanceof Number) {
            return (int) ((Number) value).longValue();
        } else {
            throw badKey(key, "number", value);
        }
    }

    public static double getRealField(@NotNull Map<?, ?> table, @NotNull String key) throws LuaException {
        return checkReal(key, getNumberField(table, key));
    }

    public static boolean getBooleanField(@NotNull Map<?, ?> table, @NotNull String key) throws LuaException {
        Object value = table.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            throw badKey(key, "boolean", value);
        }
    }

    @NotNull
    public static String getStringField(@NotNull Map<?, ?> table, @NotNull String key) throws LuaException {
        Object value = table.get(key);
        if (value instanceof String) {
            return (String) value;
        } else {
            throw badKey(key, "string", value);
        }
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static Map<Object, Object> getTableField(@NotNull Map<?, ?> table, @NotNull String key) throws LuaException {
        Object value = table.get(key);
        if (value instanceof Map) {
            return (Map<Object, Object>) value;
        } else {
            throw badKey(key, "table", value);
        }
    }

    public static double optNumberField(@NotNull Map<?, ?> table, @NotNull String key, double def) throws LuaException {
        Object value = table.get(key);
        if (value == null) {
            return def;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else {
            throw badKey(key, "number", value);
        }
    }

    public static int optIntField(@NotNull Map<?, ?> table, @NotNull String key, int def) throws LuaException {
        Object value = table.get(key);
        if (value == null) {
            return def;
        } else if (value instanceof Number) {
            return (int) ((Number) value).longValue();
        } else {
            throw badKey(key, "number", value);
        }
    }

    public static double optRealField(@NotNull Map<?, ?> table, @NotNull String key, double def) throws LuaException {
        return checkReal(key, optNumberField(table, key, def));
    }

    public static boolean optBooleanField(@NotNull Map<?, ?> table, @NotNull String key, boolean def) throws LuaException {
        Object value = table.get(key);
        if (value == null) {
            return def;
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            throw badKey(key, "boolean", value);
        }
    }

    public static String optStringField(@NotNull Map<?, ?> table, @NotNull String key, String def) throws LuaException {
        Object value = table.get(key);
        if (value == null) {
            return def;
        } else if (value instanceof String) {
            return (String) value;
        } else {
            throw badKey(key, "string", value);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<Object, Object> optTableField(@NotNull Map<?, ?> table, @NotNull String key, Map<Object, Object> def) throws LuaException {
        Object value = table.get(key);
        if (value == null) {
            return def;
        } else if (value instanceof Map) {
            return (Map<Object, Object>) value;
        } else {
            throw badKey(key, "table", value);
        }
    }

    private static double checkReal(@NotNull String key, double value) throws LuaException {
        if (!Double.isFinite(value)) throw badKey(key, "number", getNumericType(value));
        return value;
    }
}
