package com.hitomi.circlemenu.transform;

import android.os.ParcelUuid;
import android.view.View;

/**
 * 旋转切换视图
 * Created by tao on 2016/10/28.
 */

/***
 * Viewpager 先会实例化上界面
 */
public class ScaleDownTransform extends BasePageTransform {
    public int DEFAULT_RATOTE = 45;
    public int GRIENT_CENTER = (int) 0.5;

    @Override
    protected void transform(View page, float position) {

        if (position <= -1) {  //(-1,-inita)
            page.setPivotX(page.getWidth());
            page.setPivotY(page.getHeight());
            page.setRotation(-DEFAULT_RATOTE);
        } else if (position <= 1) {
            if (position > 0) { //(0,1)
                //从右边轩主按过来的
                page.setPivotX(page.getWidth() * GRIENT_CENTER * (-position));
                page.setPivotY(page.getHeight());
                page.setRotation(DEFAULT_RATOTE * (-position));
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

