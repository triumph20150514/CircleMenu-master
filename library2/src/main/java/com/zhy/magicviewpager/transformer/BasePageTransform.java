package com.zhy.magicviewpager.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by tao on 2016/10/28.
 */

public abstract class BasePageTransform implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        page.setScaleX(0.999f);
        transform(page, position);
    }

    protected abstract void transform(View page, float position);
}
