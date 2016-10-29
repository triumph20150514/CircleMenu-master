package com.hitomi.circlemenu.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.hitomi.circlemenu.refresh.IHander.IHanderView;

/**
 *
 * Created by tao on 2016/10/24.
 */

public class GoogleDotView extends View implements IHanderView {
    public GoogleDotView(Context context) {
        super(context);
    }

    public GoogleDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoogleDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public void HanderPullDown(int fration, int maxHeadHeight, int headHeight) {

    }

    @Override
    public void HanderPullRealse(int fration, int maxHeadHeight, int headHeight) {

    }
}
