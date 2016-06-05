package com.embroidermodder.embroideryviewer;

import java.util.ArrayList;

public class Pattern {
    private final ArrayList<StitchBlock> _stitchBlocks;
    private final ArrayList<EmbThread> _threadList;
    private String _filename;
    private StitchBlock _currentStitchBlock;

    private Stitch _previousStitch = new Stitch(0,0);

    public Pattern() {
        _stitchBlocks = new ArrayList<>();
        _threadList = new ArrayList<>();
        _currentStitchBlock = null;
    }

    public ArrayList<StitchBlock> getStitchBlocks() {
        return _stitchBlocks;
    }

    public String getFilename() {
        return _filename;
    }

    public void setFilename(String value) {
        _filename = value;
    }

    public void addStitchAbs(double x, double y, int flags, boolean isAutoColorIndex) {
        if (this._currentStitchBlock == null) {
            if (_stitchBlocks.size() == 0) {
                this._currentStitchBlock = new StitchBlock();
                EmbThread thread;
                if (this._threadList.size() == 0) {
                    thread = new EmbThread();
                    thread.setColor(EmbColor.Random());
                } else {
                    thread = this._threadList.get(0);
                }
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
                if ((currIndex + 1) >= this._threadList.size()) {
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
    public void addStitchRel(double dx, double dy, int flags, boolean isAutoColorIndex) {
        double x = _previousStitch.x + dx;
        double y = _previousStitch.y + dy;
        _previousStitch = new Stitch(x, y);
        this.addStitchAbs(x, y, flags, isAutoColorIndex);
    }

    // ChangeColor manually changes the color index to use.
    public void changeColor(byte index) {
        //this._currentColorIndex = index;
    }

    // Flip will flip the entire pattern about the x-axis if horz is true,
    // and/or about the y-axis if vert is true.
    public Pattern getFlippedPattern(boolean horizontal, boolean vertical) {
        double xMultiplier = horizontal ? -1.0 : 1.0;
        double yMultiplier = vertical ? -1.0 : 1.0;
        Pattern newPattern = new Pattern();
        for (EmbThread thread : this._threadList) {
            newPattern._threadList.add(new EmbThread(thread));
        }
        for (StitchBlock sb : this.getStitchBlocks()) {
            StitchBlock newStitchBlock = new StitchBlock();
            newPattern.getStitchBlocks().add(newStitchBlock);
            int threadIndex = this._threadList.indexOf(sb.getThread());
            newStitchBlock.setThread(newPattern._threadList.get(threadIndex));
            ArrayList<Stitch> newStitches = newStitchBlock.getStitches();
            for (Stitch s : sb.getStitches()) {
                newStitches.add(new Stitch(s.x * xMultiplier, s.y * yMultiplier));
            }
        }
        return newPattern;
    }

    public void addThread(EmbThread thread) {
        _threadList.add(thread);
    }

    public EmbRect calculateBoundingBox() {
        double top = Double.MAX_VALUE;
        double left = Double.MAX_VALUE;
        double bottom = Double.MIN_VALUE;
        double right = Double.MIN_VALUE;
        for (StitchBlock sb : this.getStitchBlocks()) {
            for (Stitch s : sb.getStitches()) {
                top = Math.min(top, s.y);
                left = Math.min(left, s.x);
                bottom = Math.max(bottom, s.y);
                right = Math.max(right, s.x);
            }
        }
        return new EmbRect(top, left, bottom, right);
    }

    public Pattern getPositiveCoordinatePattern() {
        int moveLeft, moveTop;
        EmbRect boundingRect = this.calculateBoundingBox();
        moveLeft = (int) boundingRect.left;
        moveTop = (int) boundingRect.top;
        Pattern newPattern = new Pattern();
        for (EmbThread thread : this._threadList) {
            newPattern._threadList.add(new EmbThread(thread));
        }
        for (StitchBlock sb : this.getStitchBlocks()) {
            StitchBlock newStitchBlock = new StitchBlock();
            newPattern.getStitchBlocks().add(newStitchBlock);
            int threadIndex = this._threadList.indexOf(sb.getThread());
            newStitchBlock.setThread(newPattern._threadList.get(threadIndex));
            ArrayList<Stitch> newStitches = newStitchBlock.getStitches();
            for (Stitch s : sb.getStitches()) {
                newStitches.add(new Stitch(s.x - moveLeft, s.y - moveTop));
            }
        }
        return newPattern;
    }

    public Pattern getCenteredPattern() {
        int moveLeft, moveTop;
        EmbRect boundingRect = this.calculateBoundingBox();
        moveLeft = (int) (boundingRect.left - (boundingRect.getWidth() / 2.0));
        moveTop = (int) (boundingRect.top - (boundingRect.getHeight() / 2.0));
        Pattern newPattern = new Pattern();
        for (EmbThread thread : this._threadList) {
            newPattern._threadList.add(new EmbThread(thread));
        }
        for (StitchBlock sb : this.getStitchBlocks()) {
            StitchBlock newStitchBlock = new StitchBlock();
            newPattern.getStitchBlocks().add(newStitchBlock);
            int threadIndex = this._threadList.indexOf(sb.getThread());
            newStitchBlock.setThread(newPattern._threadList.get(threadIndex));
            ArrayList<Stitch> newStitches = newStitchBlock.getStitches();
            for (Stitch s : sb.getStitches()) {
                newStitches.add(new Stitch(s.x - moveLeft, s.y - moveTop));
            }
        }
        return newPattern;
    }

    public static IFormatReader getReaderByFilename(String filename) {
        filename = filename.toLowerCase();
        if (filename.endsWith(".exp")) {
            return new FormatExp();
        } else if (filename.endsWith(".dst")) {
            return new FormatDst();
        } else if (filename.endsWith(".pec")) {
            return new FormatPec();
        } else if (filename.endsWith(".pes")) {
            return new FormatPes();
        }
        return null;
    }
}
