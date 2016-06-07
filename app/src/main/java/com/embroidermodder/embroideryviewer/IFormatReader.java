package com.embroidermodder.embroideryviewer;

import java.io.DataInputStream;

public interface IFormatReader {
    boolean hasColor();
    boolean hasStitches();
    Pattern read(DataInputStream stream);
}
