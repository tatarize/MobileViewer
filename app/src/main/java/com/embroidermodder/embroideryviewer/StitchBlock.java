package com.embroidermodder.embroideryviewer;

import java.util.ArrayList;

public class StitchBlock {
    private EmbThread _thread;
    private ArrayList<Stitch> _stitches;

    public StitchBlock(){
        _thread = new EmbThread();
        _stitches = new ArrayList();
    }

    public EmbThread getThread(){
        return _thread;
    }

    public void setThread(EmbThread value){
        _thread = value;
    }

    public ArrayList<Stitch> getStitches(){
        return _stitches;
    }
}
