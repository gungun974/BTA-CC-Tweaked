/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import com.mojang.nbt.NbtIo;
import com.mojang.nbt.tags.*;
import dan200.computercraft.ComputerCraft;
import org.apache.commons.codec.binary.Hex;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class NBTUtil {
    public static final int TAG_END = 0;
    public static final int TAG_BYTE = 1;
    public static final int TAG_SHORT = 2;
    public static final int TAG_INT = 3;
    public static final int TAG_LONG = 4;
    public static final int TAG_FLOAT = 5;
    public static final int TAG_DOUBLE = 6;
    public static final int TAG_BYTE_ARRAY = 7;
    public static final int TAG_STRING = 8;
    public static final int TAG_LIST = 9;
    public static final int TAG_COMPOUND = 10;
    public static final int TAG_SHORT_ARRAY = 11;
    public static final int TAG_DOUBLE_ARRAY = 12;
    public static final int TAG_LONG_ARRAY = 13;

    private NBTUtil() {
    }

    private static Tag toNBTTag(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Boolean) {
            return new ByteTag((byte) ((boolean) (Boolean) object ? 1 : 0));
        }
        if (object instanceof Number) {
            return new DoubleTag(((Number) object).doubleValue());
        }
        if (object instanceof String) {
            return new StringTag(object.toString());
        }
        if (object instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) object;
            CompoundTag nbt = new CompoundTag();
            int i = 0;
            for (Map.Entry<?, ?> entry : m.entrySet()) {
                Tag key = toNBTTag(entry.getKey());
                Tag value = toNBTTag(entry.getKey());
                if (key != null && value != null) {
                    nbt.put("k" + i, key);
                    nbt.put("v" + i, value);
                    i++;
                }
            }
            nbt.putInt("len", m.size());
            return nbt;
        }

        return null;
    }

    public static CompoundTag encodeObjects(Object[] objects) {
        if (objects == null || objects.length <= 0) {
            return null;
        }

        CompoundTag nbt = new CompoundTag();
        nbt.putInt("len", objects.length);
        for (int i = 0; i < objects.length; i++) {
            Tag child = toNBTTag(objects[i]);
            if (child != null) {
                nbt.put(Integer.toString(i), child);
            }
        }
        return nbt;
    }

    private static Object fromNBTTag(Tag tag) {
        if (tag == null) {
            return null;
        }
        switch (tag.getId()) {
            case TAG_BYTE:
                return ((ByteTag) tag).getValue() > 0;
            case TAG_DOUBLE:
                return ((DoubleTag) tag).getValue();
            default:
            case TAG_STRING:
                return tag.getValue();
            case TAG_COMPOUND:
                CompoundTag c = (CompoundTag) tag;
                int len = c.getInteger("len");
                Map<Object, Object> map = new HashMap<>(len);
                for (int i = 0; i < len; i++) {
                    Object key = fromNBTTag(c.getTag("k" + i));
                    Object value = fromNBTTag(c.getTag("v" + i));
                    if (key != null && value != null) {
                        map.put(key, value);
                    }
                }
                return map;
        }
    }

    public static Object toLua(Tag tag) {
        if (tag == null) {
            return null;
        }

        byte typeID = tag.getId();
        switch (typeID) {
            case TAG_BYTE:
            case TAG_SHORT:
            case TAG_INT:
            case TAG_LONG:
                return ((LongTag) tag).getValue();
            case TAG_FLOAT:
            case TAG_DOUBLE:
                return ((DoubleTag) tag).getValue();
            case TAG_STRING: // String
                return ((StringTag) tag).getValue();
            case TAG_COMPOUND: // Compound
            {
                CompoundTag compound = (CompoundTag) tag;
                Map<String, Object> map = new HashMap<>(compound.getValue().size());
                for (String key : compound.getValue().keySet()) {
                    Object value = toLua(compound.getTag(key));
                    if (value != null) {
                        map.put(key, value);
                    }
                }
                return map;
            }
            case TAG_LIST: {
                ListTag list = (ListTag) tag;
                Map<Integer, Object> map = new HashMap<>(list.getValue().size());
                for (int i = 0; i < list.getValue().size(); i++) {
                    map.put(i, toLua(list.getValue().get(i)));
                }
                return map;
            }
            case TAG_BYTE_ARRAY: {
                byte[] array = ((ByteArrayTag) tag).getValue();
                Map<Integer, Byte> map = new HashMap<>(array.length);
                for (int i = 0; i < array.length; i++) {
                    map.put(i + 1, array[i]);
                }
                return map;
            }
            case TAG_LONG_ARRAY:
                int[] array = Arrays.stream(((LongArrayTag) tag).getValue()).mapToInt(i -> (int) i).toArray();
                Map<Integer, Integer> map = new HashMap<>(array.length);
                for (int i = 0; i < array.length; i++) {
                    map.put(i + 1, array[i]);
                }
                return map;

            default:
                return null;
        }
    }

    public static Object[] decodeObjects(CompoundTag tag) {
        int len = tag.getInteger("len");
        if (len <= 0) {
            return null;
        }

        Object[] objects = new Object[len];
        for (int i = 0; i < len; i++) {
            String key = Integer.toString(i);
            if (tag.containsKey(key)) {
                objects[i] = fromNBTTag(tag.getTag(key));
            }
        }
        return objects;
    }

    @Nullable
    public static String getNBTHash(@Nullable CompoundTag tag) {
        if (tag == null) {
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            DataOutput output = new DataOutputStream(new DigestOutputStream(digest));
            NbtIo.write(tag, output);
            byte[] hash = digest.digest();
            return new String(Hex.encodeHex(hash));
        } catch (NoSuchAlgorithmException | IOException e) {
            ComputerCraft.log.error("Cannot hash NBT", e);
            return null;
        }
    }

    private static final class DigestOutputStream extends OutputStream {
        private final MessageDigest digest;

        DigestOutputStream(MessageDigest digest) {
            this.digest = digest;
        }

        @Override
        public void write(int b) {
            digest.update((byte) b);
        }

        @Override
        public void write(@Nonnull byte[] b, int off, int len) {
            digest.update(b, off, len);
        }
    }
}
