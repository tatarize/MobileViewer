package com.embroidermodder.embroideryviewer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class DrawView extends View {
    private final int _height;
    private final int _width;

    Paint _paint = new Paint();
    Pattern pattern;

    public DrawView(Context context, Pattern pattern) {
        super(context);
        this.pattern = pattern.getPositiveCoordinatePattern();
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        _height = metrics.heightPixels;
        _width = metrics.widthPixels;
    }

    @Override
    public void onDraw(Canvas canvas) {
        _paint.setColor(Color.WHITE);
        _paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(0, 0, _width, _height), _paint);
        _paint.setStyle(Paint.Style.STROKE);
        EmbRect embRect = pattern.calculateBoundingBox();
        float scale = (float)Math.min(_height/embRect.getHeight(), _width/embRect.getWidth());
        _paint.setStrokeWidth(scale/9.0f);
        for(StitchBlock stitchBlock : pattern.getStitchBlocks()){
            int color = stitchBlock.getThread().getColor().getAndroidColor();
            _paint.setColor(color);
            Path path = new Path();
            final ArrayList<Stitch> stitches = stitchBlock.getStitches();
            boolean isFirst = true;
            for(Stitch stitch : stitches) {
                if (isFirst == true) {
                    path.moveTo((float) stitch.x * scale, (float) stitch.y * scale);
                    isFirst =false;
                    continue;
                }
                path.lineTo((float) stitch.x * scale, (float) stitch.y * scale);
            }
            canvas.drawPath(path, _paint);
        }
    }
}
