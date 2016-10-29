package com.hitomi.circlemenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by tao on 2016/10/11.
 */

public class CircleMenu extends View {

    protected static final int MAIN_MENU_CLOSE = 1; //关闭菜单
    protected static final int MAIN_MENU_CLOSED = 2;//
    protected static final int MAIN_MENU_OPEN = 3; //打开菜单
    protected static final int MAIN_MENU_OPEND = 4;

    private int MUEN_NUM = 5;

    public int status; //記錄當前菜單的狀態

    private int centerPartSize = dip2px(20); //中心大小

    private int iconSize = centerPartSize * 4 / 5; //图标大小

    private int circleMenuRadius = centerPartSize * 3; //半径大小
    private int maxCircleRadius;

    private Paint mPaint;
    private Paint cPaint;
    private Paint rPaint;
    private RectF rectFs = new RectF();

    private Rect[] iconRect = new Rect[MUEN_NUM]; //每个图标的范围

    private Rect circleRect = new Rect(); // 圆的范围
    private int mWidth, mHeight;

    private Path circlePath = new Path();

    private PathMeasure pathMeasure;

    private float pathlength;

    private int index;

    private int clickIndex;

    private int[] iconResArray = new int[5];

    {
        iconResArray[0] = R.mipmap.icon_home;
        iconResArray[1] = R.mipmap.icon_search;
        iconResArray[2] = R.mipmap.icon_notify;
        iconResArray[3] = R.mipmap.icon_setting;
        iconResArray[4] = R.mipmap.icon_gps;
    }

    private int[] menuColors = new int[]{
            Color.parseColor("#CDCDCD"),
            Color.parseColor("#258CFF"),
            Color.parseColor("#30A400"),
            Color.parseColor("#FF4B32"),
            Color.parseColor("#8A39FF"),
            Color.parseColor("#FF6A00")
    };

    //子菜单图标
    public Drawable[] menuIcons = new Drawable[5];


    public Drawable[] mainIcons = new Drawable[]{convertDrawable(R.mipmap.icon_menu), convertDrawable(R.mipmap.icon_cancel)};
    private int centerX, centerY;

    public Drawable[] getMenuIcons() {
        return menuIcons;
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenu(Context context) {
        this(context, null);
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        status = MAIN_MENU_CLOSE; //默認菜單喂關閉狀態
        initDrawable();
        //初始化
        init();
    }

    private void initDrawable() {
        for (int i = 0; i < menuIcons.length; i++) {
            menuIcons[i] = convertDrawable(iconResArray[i]);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w / 2;
        mHeight = h / 2;

        Log.e("Tirmph mWidth", "---------------------=" + mWidth);
        Log.e("Tirmph mHeight", "---------------------=" + mHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        //移动到圆心
        canvas.translate(mWidth, mHeight);
        canvas.drawPath(circlePath, rPaint);
        Log.e("Tirmph status", "---------------------" + status);
        Log.e("Tirmph status", "-----------left----------" + circleRect.left);
        canvas.drawRect(circleRect, rPaint);

        switch (status) {
            case MAIN_MENU_CLOSE:
                //绘制主菜单
                drawMainIcon(canvas);
//                drawMenuIcons(canvas);
                break;
            case MAIN_MENU_OPEN:
                drawMainIcon(canvas);
                //先绘制一个大圆固定
                drawMenuIcons(canvas);

                break;
            case MAIN_MENU_CLOSED:
                drawMainIcon(canvas);
                drawMenuIcons(canvas);
                break;
            case MAIN_MENU_OPEND:
                //圆环路径
                drawCirclePath(canvas);
                break;
        }

    }

    /**
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取手指按下的图标
                clickIndex = getClickPosition((int) event.getX() - 540, (int) event.getY() - 849);
                Log.e("Tirmph onTouch", "---------clickIndex------------=" + event.getX() + " getY" + event.getY());

                Log.e("Tirmph onTouch", "---------clickIndex------------=" + clickIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (clickIndex == 5) {   // 等於5 時 表示點擊的是中心菜單
                    if (status == MAIN_MENU_CLOSE) {
                        status = MAIN_MENU_OPEN;
                        //手指抬起 执行动画
                        startOpenAnimation();
                        Log.e("Tirmph onTouch", "---------------------");
                    } else {
                        status = MAIN_MENU_CLOSED;
                        //隱藏菜單想
                        closeSubMenu();
                    }
                } else {     //表示點擊的是子菜單
                    //繪製圓環軌跡  和
                    status = MAIN_MENU_OPEND;
                    switch (clickIndex) {
                        case 0:
                            rotateAngle = 72 + 20;
                            break;
                        case 1:
                            rotateAngle = 0 + 20;
                            break;
                        case 2:
                            rotateAngle = -72 + 20;
                            break;
                        case 3:
                            rotateAngle = -144 + 20;
                            break;
                        case 4:
                            rotateAngle = 144 + 20;
                            break;

                    }
                    startCircleAnimation();

                }
                break;
        }
        return true;
    }


    /**
     * 绘制圆环旋转动画
     *
     * @param canvas
     */
    public void drawCirclePath(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotateAngle);
        Path des = new Path();
        pathMeasure.getSegment(0, pathlength * fraction, des, true);
        mPaint.setStrokeWidth(centerPartSize * 2);
        canvas.drawPath(des, mPaint);
        canvas.restore();
    }

    public int rotateAngle;

    /**
     * 隱藏打開的菜單
     */
    private void closeSubMenu() {
        Log.e("Tirmph onTouch", "-----------startOpenAnimation----------");
        ValueAnimator objectAnimator = ValueAnimator.ofFloat(1.0f, 100f);
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new OvershootInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = animation.getAnimatedFraction();

                fraction = (1 - fraction);
                //子菜单的圆的大小
                Log.e("Tirmph fraction", "-----------fraction----------" + fraction);
                childCircleRaduis = (int) ((fraction) * centerPartSize);
                //不断的重新绘制
                invalidate();
            }


        });

        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                status = MAIN_MENU_CLOSE;
            }
        });


        objectAnimator.start();
    }

    /**
     * 开始动画
     */
    private void startOpenAnimation() {
        Log.e("Tirmph onTouch", "-----------startOpenAnimation----------");
        ValueAnimator objectAnimator = ValueAnimator.ofFloat(1.0f, 100f);
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new OvershootInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = animation.getAnimatedFraction();

                //子菜单的圆的大小
                Log.e("Tirmph fraction", "-----------fraction----------" + fraction);
                childCircleRaduis = (int) (fraction * centerPartSize);
                //不断的重新绘制
                invalidate();
            }
        });
        objectAnimator.start();
    }

    /**
     * 开始圆环扩散动画
     */
    private void startCircleAnimation() {
        Log.e("Tirmph onTouch", "-----------startOpenAnimation----------");
        ValueAnimator objectAnimator = ValueAnimator.ofFloat(1.0f, 100f);
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(new OvershootInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = animation.getAnimatedFraction();
                //子菜单的圆的大小
                Log.e("Tirmph fraction", "-----------fraction----------" + fraction);
//                childCircleRaduis = (int) (fraction * centerPartSize);
                //不断的重新绘制
                invalidate();
            }
        });
        objectAnimator.start();
    }

    private int childCircleRaduis;
    private int childRaduis;
    private float fraction, rFraction; //不断变化的中间值

    /***
     * 获取手指按下的index
     *
     * @param x
     * @param y
     * @return
     */
    private int getClickPosition(int x, int y) {

        /**
         * 点击的是子菜单
         */
        for (int i = 0; i < iconRect.length; i++) {
            Log.e("Trimph    iconRect  getClickPosition", "top" + iconRect[i].top + " bottom" + iconRect[i].bottom + " left" + iconRect[i].left + " right" + iconRect[i].right);
            if (iconRect[i].contains(x, y)) {
                return i;
            }
        }

        /**
         * 点击的是中心的菜单
         */
        if (circleRect.contains(x, y)) {
            Log.e("Trimph    circleRect  getClickPosition  ", "top" + circleRect.top + " bottom" + circleRect.bottom + " left" + circleRect.left + " right" + circleRect.right);
            return iconRect.length;
        }

        return -1;
    }

    //圆的 中心点
    float itemX, itemY;

    /**
     * 绘制子菜单
     *
     * @param canvas
     */
    private void drawMenuIcons(Canvas canvas) {

        int angele = 360 / menuIcons.length; //度数  每一个图标所占的角度

        //绘制子菜单图标
        /**
         *  循环绘制五个圆  先找到其中心点  计算半径
         */
        for (int i = 0; i < menuIcons.length; i++) {
            //找到圆上的五个均分点 然后绘制圆 和图标  起点从圆心的正上方 沿着顺时针开始  每个圆处于一条直线上  越来越大
            //相当于半径值不断变化  然后计算坐标
            itemX = (float) (Math.sin(Math.toRadians(i * angele)) * (maxCircleRadius - maxCircleRadius / 3 * (1 - fraction)));
            itemY = (float) (Math.cos(Math.toRadians(i * angele)) * (maxCircleRadius - maxCircleRadius / 3 * (1 - fraction)));
            Log.e("Trimph", "itemX" + itemX + " itemY" + itemY);
            cPaint.setColor(menuColors[i]);
//            cPaint.setStrokeWidth(5);
            Log.e("Trimph", "childCircleRaduis" + childCircleRaduis);
            canvas.drawCircle(itemX, itemY, childCircleRaduis, cPaint);

            //每个圆的范围
            iconRect[i].set((int) itemX - centerPartSize, (int) itemY - centerPartSize, (int) itemX + centerPartSize,
                    (int) itemY + centerPartSize);
            Log.e("Trimph    circleRect  drawMenuIcons", "top" + iconRect[i].top + " bottom" + iconRect[i].bottom + " left" + iconRect[i].left + " right" + iconRect[i].right);
//            canvas.drawRect( iconRect[i],mPaint);
            //画图标
            drawSubIcon(canvas, i, itemX, itemY);
        }
        mPaint.setColor(Color.parseColor("#FF4081"));


//        mPaint.setStrokeWidth(centerPartSize * 2 + centerPartSize * .5f * fraction);
//
//        canvas.drawCircle(0, 0, centerPartSize * 5 + centerPartSize * fraction, mPaint);
    }

    private void drawSubIcon(Canvas canvas, int position, float itemX, float itemY) {

        int diff = childCircleRaduis / 2;
        if (status == MAIN_MENU_CLOSED) {
            menuIcons[position].setBounds((int) itemX - diff, (int) itemY - diff, (int) itemX + diff, (int) itemY + diff);
        } else {
            diff = centerPartSize / 2;
            menuIcons[position].setBounds((int) itemX - diff, (int) itemY - diff, (int) itemX + diff, (int) itemY + diff);
        }
        /**
         * 動畫限制圓的範圍
         */
        menuIcons[position].draw(canvas);
    }

    /**
     *
     */
    private void drawMainIcon(Canvas canvas) {
        canvas.save();
        switch (status) {
            case MAIN_MENU_CLOSE:
                resetMainIcon(canvas, mainIcons[0]);
                break;
            case MAIN_MENU_OPEN:
                //畫布旋轉
                canvas.rotate(45 * (1 - fraction));
                resetMainIcon(canvas, mainIcons[1]);
                break;
            case MAIN_MENU_CLOSED:
                //畫布旋轉
                canvas.rotate(45 * (1 - fraction));
                resetMainIcon(canvas, mainIcons[1]);
                break;
        }
//        canvas.drawCircle(0,0,centerPartSize,rPaint);
        canvas.restore();
    }

    private void resetMainIcon(Canvas canvas, Drawable drawable) {
        Log.e("Trimph:============", "width" + mWidth);
        Log.e("Trimph:============", "width" + centerPartSize);
        //固定范围
//        circleRect.set(mWidth - centerPartSize, mHeight - centerPartSize, mWidth + centerPartSize, mHeight + centerPartSize);

        drawable.setBounds(circleRect);
        drawable.draw(canvas);
    }

    /**
     * 設置子菜單
     *
     * @param resouRces
     */
    public void setMenuIcons(int[] resouRces) {
        for (int i = 0; i < menuIcons.length; i++) {
            menuIcons[i] = convertDrawable(resouRces[i]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 主菜单图标
     *
     * @param resouRces
     */
    public void setMainIcons(int[] resouRces) {
        for (int i = 0; i < mainIcons.length; i++) {
            mainIcons[i] = convertDrawable(resouRces[i]);
        }
    }

    /**
     * @param icon_cancel
     * @return
     */
    private Drawable convertDrawable(int icon_cancel) {
        return getResources().getDrawable(icon_cancel);
    }

    private void init() {
        initTool();
        //中心店
        centerX = centerPartSize * 5;
        centerY = centerX;

        //半径
        circleMenuRadius = centerPartSize * 3;

        childCircleRaduis = circleMenuRadius / 2;

        maxCircleRadius = centerPartSize * 5;

//        circlePath.addCircle(mWidth, mHeight, circleMenuRadius, Path.Direction.CW);

        circlePath.addCircle(mWidth, mHeight, maxCircleRadius, Path.Direction.CW);

        pathMeasure.setPath(circlePath, true);

        pathlength = pathMeasure.getLength();

        //固定范围
        circleRect.set(mWidth - centerPartSize, mHeight - centerPartSize, mWidth + centerPartSize, mHeight + centerPartSize);
        Log.e("Trimph    circleRect", "top" + circleRect.top + " bottom" + circleRect.bottom + " left" + circleRect.left + " right" + circleRect.right);
    }


    private void initTool() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);


        cPaint = new Paint();
        cPaint.setAntiAlias(true);
        cPaint.setStrokeWidth(15);
        cPaint.setColor(Color.parseColor("#CDCDCD"));

        rPaint = new Paint();
        rPaint.setStyle(Paint.Style.STROKE);
        cPaint.setColor(Color.parseColor("#3F51B5"));


        circlePath = new Path();
        pathMeasure = new PathMeasure();

        for (int i = 0; i < iconRect.length; i++) {
            iconRect[i] = new Rect();
        }

    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
