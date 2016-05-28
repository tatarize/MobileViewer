package com.embroidermodder.embroideryviewer;

import android.util.Log;

import java.util.ArrayList;

public class Pattern {
    private final ArrayList<StitchBlock> _stitchBlocks;
    private final ArrayList<EmbThread> _threadList;
    private String _filename;
    private StitchBlock _currentStitchBlock;

    public Pattern(){
        _stitchBlocks = new ArrayList<>();
        _threadList = new ArrayList<>();
        _currentStitchBlock = null;
    }

    public static Pattern getSampleData1(){
        Pattern p = new Pattern();
        p.setFilename("SampleData1.dst");
        ArrayList<StitchBlock> stitchBlocks = p.getStitchBlocks();
        // Color 1
        StitchBlock block = new StitchBlock();
        EmbThread thread = new EmbThread();
        thread.setColor(new EmbColor((byte)0,(byte)0,(byte)255));
        thread.setDescription("Blue");
        thread.setCatalogNumber("101");
        block.setThread(thread);
        ArrayList<Stitch> stitches = block.getStitches();
        stitches.add(new Stitch(0.0, 0.0));
        stitches.add(new Stitch(30.0, 30.0));
        stitchBlocks.add(block);
        // Color 2
        block = new StitchBlock();
        thread = new EmbThread();
        thread.setColor(new EmbColor((byte)255,(byte)0,(byte)0));
        thread.setDescription("Red");
        thread.setCatalogNumber("102");
        block.setThread(thread);
        stitches = block.getStitches();
        stitches.add(new Stitch(30.0, 0.0));
        stitches.add(new Stitch(0.0, 30.0));
        stitchBlocks.add(block);
        return p;
    }

    public ArrayList<StitchBlock> getStitchBlocks(){
        return _stitchBlocks;
    }

    public String getFilename(){
        return _filename;
    }

    public void setFilename(String value){
        _filename = value;
    }

    public void AddStitchAbs(double x, double y, int flags, boolean isAutoColorIndex) {
        if(this._currentStitchBlock == null){
            if(_stitchBlocks.size() == 0){
                this._currentStitchBlock = new StitchBlock();
                this._currentStitchBlock.setThread(new EmbThread());
                this._threadList.add(this._currentStitchBlock.getThread());
                _stitchBlocks.add(this._currentStitchBlock);
            } else {
                this._currentStitchBlock = this._stitchBlocks.get(0);
            }
        }
        ArrayList<Stitch> stitches = this._currentStitchBlock.getStitches();
        if ((flags & StitchType.END) != 0) {
            if (stitches.size() == 0) {
                return;
            }
            //pattern.FixColorCount();
        }

        if ((flags & StitchType.STOP) > 0) {
            if ((this._currentStitchBlock.getStitches().size()) == 0) {
                return;
            }
            if (isAutoColorIndex) {
                int currIndex = this._threadList.indexOf(this._currentStitchBlock);
                if(currIndex > this._threadList.size()) {
                    this._threadList.add(new EmbThread());
                }
                this._threadList.get(currIndex + 1);
            }
        }
        Stitch s = new Stitch(x, y);
        this._currentStitchBlock.getStitches().add(s);
        Log.d("asdf", s.x + "," + s.y + "\n");

    }

    // AddStitchRel adds a stitch to the pattern at the relative position (dx, dy)
// to the previous stitch. Positive y is up. Units are in millimeters.
    public void AddStitchRel(double dx, double dy, int flags, boolean isAutoColorIndex) {
        double x, y;
        if(this._currentStitchBlock == null){
            if(_stitchBlocks.size() == 0){
                this._currentStitchBlock = new StitchBlock();
                this._currentStitchBlock.setThread(new EmbThread());
                this._threadList.add(this._currentStitchBlock.getThread());
                _stitchBlocks.add(this._currentStitchBlock);
            } else {
                this._currentStitchBlock = this._stitchBlocks.get(0);
            }
        }
        ArrayList<Stitch> stitches = this._currentStitchBlock.getStitches();
        if (stitches.size() > 0) {
            Stitch lastStitch = stitches.get(stitches.size()-1);
            x = lastStitch.x + dx;
            y = lastStitch.y + dy;
        } else {
		/* NOTE: The stitchList is empty, so add it to the HOME position. The embStitchList_create function will ensure the first coordinate is at the HOME position. */
            Stitch home = new Stitch(0.0, 0.0);
            x = home.x + dx;
            y = home.y + dy;
        }
        this.AddStitchAbs(x, y, flags, isAutoColorIndex);
    }

    // ChangeColor manually changes the color index to use.
    public void ChangeColor(byte index) {
        //this._currentColorIndex = index;
    }

    // Flip will flip the entire pattern about the x-axis if horz is true,
// and/or about the y-axis if vert is true.
    public void Flip(boolean horz, boolean vert) {
        for(StitchBlock stitchBlock : this._stitchBlocks) {
            for(Stitch stitch: stitchBlock.getStitches()) {
                if (horz) {
                    //stitch.x = -stitch.x;
                }
                if (vert) {
                    //stitch.y = -stitch.y;
                }
            }
        }
    }

    public void AddThread(EmbThread thread) {
        //this.ThreadList = append(pattern.ThreadList, thread);
    }
}
