package de.minestar.craftz.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.server.v1_4_6.NBTBase;
import net.minecraft.server.v1_4_6.NBTTagCompound;

public class CompressedStreamTools {

    public static NBTTagCompound loadGzippedCompoundFromOutputStream(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(inputStream)));
        try {
            NBTTagCompound nbtTagCompound = read(dataInputStream);
            return nbtTagCompound;
        } finally {
            dataInputStream.close();
        }
    }

    public static void writeGzippedCompoundToOutputStream(NBTTagCompound nbtTag, OutputStream outputStream) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(outputStream));
        try {
            writeTo(nbtTag, dataOutputStream);
        } finally {
            dataOutputStream.flush();
            dataOutputStream.close();
        }
    }

    public static NBTTagCompound loadMapFromByteArray(byte byteArray[]) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(byteArray))));
        try {
            NBTTagCompound nbtTagCompound = read(dataInputStream);
            return nbtTagCompound;
        } finally {
            dataInputStream.close();
        }
    }

    public static byte[] writeMapToByteArray(NBTTagCompound nbtTag) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(byteArrayOutputStream));
        try {
            writeTo(nbtTag, dataOutputStream);
        } finally {
            dataOutputStream.close();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static NBTTagCompound read(DataInput inputStream) throws IOException {
        NBTBase nbtBase = NBTBase.b(inputStream);
        if (nbtBase instanceof NBTTagCompound) {
            return (NBTTagCompound) nbtBase;
        } else {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    public static void writeTo(NBTTagCompound nbtTag, DataOutput outputStream) throws IOException {
        NBTBase.a(nbtTag, outputStream);
    }
}
