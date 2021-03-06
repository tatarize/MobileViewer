package com.embroidermodder.embroideryviewer;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BinaryReader {
    public static int readInt32BE(DataInputStream stream) throws IOException {
        byte fullInt[] = new byte[4];
        stream.read(fullInt);
        return ((fullInt[0] & 0xFF) << 24) + ((fullInt[1] & 0xFF) << 16) + ((fullInt[2] & 0xFF) << 8) + (fullInt[3] & 0xFF);
    }

    public static int readInt16BE(DataInputStream stream) throws IOException {
        byte fullInt[] = new byte[2];
        stream.read(fullInt);
        return ((fullInt[1] & 0xFF) << 8) + (fullInt[0] & 0xFF);
    }

    public static String readString(DataInputStream stream, int maxLength) throws IOException {
        ArrayList<Byte> charList = new ArrayList<Byte>();
        int i = 0;
        while(i < maxLength) {
            byte value = (byte)stream.readByte();
            if(value == '\0') {
                break;
            }
            charList.add(value);
            i++;
        }
        byte[] result = new byte[charList.size()];
        for(i = 0; i < charList.size(); i++) {
            result[i] = charList.get(i).byteValue();
        }
        return new String(result, "UTF-8");
    }
}
