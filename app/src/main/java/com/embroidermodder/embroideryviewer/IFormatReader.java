package com.embroidermodder.embroideryviewer;

import java.io.DataInputStream;

public interface IFormatReader {
    Pattern Read(DataInputStream stream);
}
