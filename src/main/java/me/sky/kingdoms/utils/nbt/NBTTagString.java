package me.sky.kingdoms.utils.nbt;

import net.minecraft.server.v1_15_R1.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class NBTTagString implements NBTBase {
    public static final NBTTagType<NBTTagString> a = new NBTTagType<NBTTagString>() {
        public NBTTagString b(DataInput var0, int var1, NBTReadLimiter var2) throws IOException {
            var2.a(288L);
            String var3 = var0.readUTF();
            var2.a((long)(16 * var3.length()));
            return NBTTagString.a(var3);
        }

        public String a() {
            return "STRING";
        }

        public String b() {
            return "TAG_String";
        }

        public boolean c() {
            return true;
        }
    };
    private static final NBTTagString b = new NBTTagString("");
    private final String data;

    public NBTTagString(String var0) {
        Objects.requireNonNull(var0, "Null string not allowed");
        this.data = var0;
    }

    public static NBTTagString a(String var0) {
        return var0.isEmpty() ? b : new NBTTagString(var0);
    }

    public void write(DataOutput var0) throws IOException {
        var0.writeUTF(this.data);
    }

    public byte getTypeId() {
        return 8;
    }

    public NBTTagType<NBTTagString> b() {
        return a;
    }

    public String toString() {
        return b(this.data);
    }

    public NBTTagString clone() {
        return this;
    }

    public boolean equals(Object var0) {
        if (this == var0) {
            return true;
        } else {
            return var0 instanceof NBTTagString && Objects.equals(this.data, ((NBTTagString)var0).data);
        }
    }

    public int hashCode() {
        return this.data.hashCode();
    }

    public String asString() {
        return this.data;
    }

    public IChatBaseComponent a(String var0, int var1) {
        String var2 = b(this.data);
        String var3 = var2.substring(0, 1);
        IChatBaseComponent var4 = (new ChatComponentText(var2.substring(1, var2.length() - 1))).a(e);
        return (new ChatComponentText(var3)).addSibling(var4).a(var3);
    }

    public static String b(String var0) {
        StringBuilder var1 = new StringBuilder(" ");
        char var2 = 0;

        for(int var3 = 0; var3 < var0.length(); ++var3) {
            char var4 = var0.charAt(var3);
            if (var4 == '\\') {
                var1.append('\\');
            } else if (var4 == '"' || var4 == '\'') {
                if (var2 == 0) {
                    var2 = (char) (var4 == '"' ? 39 : 34);
                }

                if (var2 == var4) {
                    var1.append('\\');
                }
            }

            var1.append(var4);
        }

        if (var2 == 0) {
            var2 = 34;
        }

        var1.setCharAt(0, (char)var2);
        var1.append((char)var2);
        return var1.toString();
    }
}

