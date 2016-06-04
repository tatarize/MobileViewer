package com.embroidermodder.embroideryviewer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class DrawView extends View {
    private final float _height;
    private final float _width;

    Paint paint = new Paint();
    Pattern pattern;


    public DrawView(Context context, Pattern pattern) {
        super(context);
        this.pattern = pattern.getPositiveCoordinatePattern();
        paint.setStyle(Paint.Style.STROKE);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        _height = metrics.heightPixels;
        _width = metrics.widthPixels;
    }

    @Override
    public void onDraw(Canvas canvas) {

        EmbRect embRect = pattern.calculateBoundingBox();


        float scale = (float)Math.min(_height/embRect.getHeight(), _width/embRect.getWidth());
        paint.setStrokeWidth(scale/10);

        for(StitchBlock stitchBlock : pattern.getStitchBlocks()){

            int color = stitchBlock.getThread().getColor().getAndroidColor();
            paint.setColor(color);
            Path path = new Path();

            final ArrayList<Stitch> stitches = stitchBlock.getStitches();
            Stitch firstStich = stitches.get(0);
            path.moveTo((float)firstStich.x * scale, (float)firstStich.y * scale);
            stitches.remove(0);

            for(Stitch stitch : stitches) {
                float x = (float) stitch.x;
                float y = (float) stitch.y;
                path.lineTo(x * scale, y*scale);
            }
            canvas.drawPath(path, paint);
        }
    }

}
