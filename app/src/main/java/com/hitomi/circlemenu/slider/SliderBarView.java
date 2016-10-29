package com.hitomi.circlemenu.slider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 快速索引栏
 * Created by tao on 2016/10/28.
 */

public class SliderBarView extends View {

    public List<String> list = new ArrayList<>();
    public List<Rect> rectList = new ArrayList<>();
    public int height, width;
    public int chiose = -1;

    public SliderBarView(Context context) {
        super(context);
    }

    public SliderBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SliderBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = (int) (h * 0.8);
    }

    public Paint mPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (list == null) {
            return;
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        for (int i = 0; i < list.size(); i++) {

            if (i == chiose) {
                mPaint.setColor(Color.RED);
            }
            //确定范围
            mPaint.getTextBounds(list.get(i), 0, list.get(i).length(), rectList.get(i));

            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();

            int tWidth = rectList.get(i).width();

            
//            mPaint.getTextWidths(list.get(i), new float[]{});

//            canvas.drawText(list.get(i), );

        }


    }
}
