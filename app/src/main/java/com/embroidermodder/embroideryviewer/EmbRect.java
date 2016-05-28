package com.embroidermodder.embroideryviewer;

public class EmbRect {
    public final double top;
    public final double left;
    public final double bottom;
    public final double right;

    public EmbRect(double top, double left, double bottom, double right){
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public double getWidth(){
        return right - left;
    }

    public double getHeight(){
        return bottom - top;
    }
}
