package com.embroidermodder.embroideryviewer;

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
                EmbThread thread = new EmbThread();
                thread.setColor(EmbColor.Random());
                this._currentStitchBlock.setThread(thread);
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
            int threadIndex = 0;
            int currIndex = this._threadList.indexOf(this._currentStitchBlock.getThread());
            if (isAutoColorIndex) {
                if((currIndex + 1) >= this._threadList.size()) {
                    EmbThread newThread = new EmbThread();
                    newThread.setColor(EmbColor.Random());
                    this._threadList.add(newThread);
                }
                threadIndex = currIndex + 1;
            }
            StitchBlock sb = new StitchBlock();
            this._currentStitchBlock = sb;
            sb.setThread(this._threadList.get(threadIndex));
            this.getStitchBlocks().add(sb);
        }
        Stitch s = new Stitch(x, y);
        this._currentStitchBlock.getStitches().add(s);
    }

    // AddStitchRel adds a stitch to the pattern at the relative position (dx, dy)
    // to the previous stitch. Positive y is up. Units are in millimeters.
    public void AddStitchRel(double dx, double dy, int flags, boolean isAutoColorIndex) {
        double x, y;
        if(this._currentStitchBlock == null){
            if(_stitchBlocks.size() == 0){
                this._currentStitchBlock = new StitchBlock();
                EmbThread thread = new EmbThread();
                thread.setColor(EmbColor.Random());
                this._currentStitchBlock.setThread(thread);
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
    public Pattern getFlippedPattern(boolean horizontal, boolean vertical) {
        double xMultiplier = horizontal ? -1.0 : 1.0;
        double yMultiplier = vertical ? -1.0 : 1.0;
        Pattern newPattern = new Pattern();
        for(EmbThread thread : this._threadList){
            newPattern._threadList.add(new EmbThread(thread));
        }
        for(StitchBlock sb : this.getStitchBlocks()){
            StitchBlock newStitchBlock = new StitchBlock();
            newPattern.getStitchBlocks().add(newStitchBlock);
            int threadIndex = this._threadList.indexOf(sb.getThread());
            newStitchBlock.setThread(newPattern._threadList.get(threadIndex));
            ArrayList<Stitch> newStitches = newStitchBlock.getStitches();
            for(Stitch s : sb.getStitches()) {
                newStitches.add(new Stitch(s.x *xMultiplier, s.y * yMultiplier));
            }
        }
        return newPattern;
    }

    public void addThread(EmbThread thread) {
        _threadList.add(thread);
    }

    public EmbRect calculateBoundingBox(){
        double top = Double.MAX_VALUE;
        double left = Double.MAX_VALUE;
        double bottom = Double.MIN_VALUE;
        double right = Double.MIN_VALUE;
        for(StitchBlock sb : this.getStitchBlocks()){
            for(Stitch s : sb.getStitches()) {
                top = Math.min(top, s.y);
                left = Math.min(left, s.x);
                bottom = Math.min(bottom, s.y);
                right = Math.min(right, s.x);
            }
        }
        return new EmbRect(top, left, bottom, right);
    }

    public Pattern getPositiveCoordinatePattern() {
        int moveLeft, moveTop;
        EmbRect boundingRect = this.calculateBoundingBox();
        moveLeft = (int)(boundingRect.left - (boundingRect.getWidth() / 2.0));
        moveTop = (int)(boundingRect.top - (boundingRect.getHeight() / 2.0));
        Pattern newPattern = new Pattern();
        for(EmbThread thread : this._threadList){
            newPattern._threadList.add(new EmbThread(thread));
        }
        for(StitchBlock sb : this.getStitchBlocks()){
            StitchBlock newStitchBlock = new StitchBlock();
            newPattern.getStitchBlocks().add(newStitchBlock);
            int threadIndex = this._threadList.indexOf(sb.getThread());
            newStitchBlock.setThread(newPattern._threadList.get(threadIndex));
            ArrayList<Stitch> newStitches = newStitchBlock.getStitches();
            for(Stitch s : sb.getStitches()) {
                newStitches.add(new Stitch(s.x - moveLeft, s.y - moveTop));
            }
        }
        return newPattern;
    }
}
