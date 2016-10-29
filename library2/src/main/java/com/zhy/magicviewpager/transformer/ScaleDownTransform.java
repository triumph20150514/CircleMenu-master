package com.zhy.magicviewpager.transformer;

import android.view.View;

/**
 * 旋转切换视图
 * Created by tao on 2016/10/28.
 */

/***
 * Viewpager 先会实例化上界面
 */
public class ScaleDownTransform extends BasePageTransformer {
    public int DEFAULT_RATOTE = 45;
    public float GRIENT_CENTER = 0.5f;

    @Override
    protected void pageTransform(View page, float position) {
        if (position < -1) {  //(-1,-inita)
            page.setPivotX(page.getWidth());
            page.setPivotY(page.getHeight());
            page.setRotation(-DEFAULT_RATOTE);
        } else if (position <= 1) {
            if (position > 0) { //(0,1)
                //从右边轩主按过来的
                //position bian变化 1--0  大到小  所有
                page.setPivotX(page.getWidth() * GRIENT_CENTER * (1 - position));
                page.setPivotY(page.getHeight());
                page.setRotation(DEFAULT_RATOTE * (position));
            }
            if (position < 0) { //(-1,0)
                //从右边轩主按过来的
                page.setPivotX(page.getWidth() * (GRIENT_CENTER + GRIENT_CENTER * (-position)));
                page.setPivotY(page.getHeight());

                page.setRotation(DEFAULT_RATOTE * (position));
            }
        } else { //(1,+inivea)
            //先写最右边的
            page.setPivotX(page.getWidth() * 0);
            page.setPivotY(page.getHeight());
            page.setRotation(DEFAULT_RATOTE);
        }
    }


}

