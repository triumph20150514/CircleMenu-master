package com.zhy.magicviewpager.transformer;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;


/**
 * 从注释中可以看到,
 * position 有一下几个区间:
 * [-∞ , -1)  :
 * 表示左边 的View 且已经看不到了
 * [-1 ,   0]  :
 * 表示左边的 View ,且可以看见
 * ( 0 ,   1]  :
 * 表示右边的VIew , 且可以看见了
 * ( 1 , -∞)  :
 * 表示右边的 View 且已经看不见了
 * <p>
 * 上面讲的有些模糊,  举个例子
 * <p>
 * a 是第一页
 * b 是第二页
 * 当前页为 a, 当  a  向左滑动,  直到滑到 b 时:
 * a 的position变化是  [-1 ,   0]   由  0  慢慢变到 -1
 * b 的position变化是  ( 0 ,   1]   由  1  慢慢变到  0
 * <p>
 * 当前页为b,  当 b 向右滑动, 直到滑到a 时:
 * a 的position变化是  [-1 ,   0]   由  -1  慢慢变到 0
 * b 的position变化是  ( 0 ,   1]   由   0   慢慢变到 1
 * <p>
 * 了解了这些后, 那么其他的事情 就是在更具position 来写不同的动画了
 */
public class RotateDownPageTransformer extends BasePageTransformer {
    private static final float DEFAULT_MAX_ROTATE = 45.0f;
    private float mMaxRotate = DEFAULT_MAX_ROTATE;

    public RotateDownPageTransformer() {
    }

    public RotateDownPageTransformer(float maxRotate) {
        this(maxRotate, NonPageTransformer.INSTANCE);
    }

    public RotateDownPageTransformer(ViewPager.PageTransformer pageTransformer) {
        this(DEFAULT_MAX_ROTATE, pageTransformer);
    }

    public RotateDownPageTransformer(float maxRotate, ViewPager.PageTransformer pageTransformer) {
        mPageTransformer = pageTransformer;
        mMaxRotate = maxRotate;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void pageTransform(View view, float position) {
        Log.e("Trimph", "position" + position);
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            Log.e("position [-Infinity,-1]", "left:" + view.getLeft() + "height:" + view.getHeight() + "width" + view.getWidth() + "view.getY()：" + view.getY() + "TOp::" + view.getTop());
            view.setRotation(mMaxRotate * -1);
            view.setPivotX(view.getWidth());
            view.setPivotY(view.getHeight());

        } else if (position <= 1) { // [-1,1]

            if (position < 0)//[0，-1]
            {
                //旋转中心点不断的变化
                Log.e("position [0，-1]", "left:" + view.getLeft() + "height:" + view.getHeight() + "width" + view.getWidth() + "view.getY()：" + view.getY() + "TOp::" + view.getTop());
                Log.e("Trimph", "position" + "从那边滑动过啦的" + position);
                view.setPivotX(view.getWidth() * (DEFAULT_CENTER + DEFAULT_CENTER * (-position)));
                view.setPivotY(view.getHeight());
                view.setRotation(mMaxRotate * position);
            } else//[0,1]
            {
                Log.e("position [1,0]", "left:" + view.getLeft() + "height:" + view.getHeight() + "width" + view.getWidth() + "view.getY()：" + view.getY() + "TOp::" + view.getTop());
                view.setPivotX(view.getWidth() * DEFAULT_CENTER * (1 - position));
                view.setPivotY(view.getHeight());
                view.setRotation(mMaxRotate * position);
            }
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.  
            view.setRotation(mMaxRotate);
            Log.e("position (1,+infinity)", "left:" + view.getLeft() + "height:" + view.getHeight() + "view.getY()：" + view.getY() + "TOp::" + view.getTop());
            view.setPivotX(view.getWidth() * 0);
            view.setPivotY(view.getHeight());
        }
    }

}  