package com.hitomi.circlemenu.refresh.IHander;

import android.view.View;

/**
 * 头部监听方法
 * Created by tao on 2016/10/24.
 */

public interface IHanderView {

    /**
     * 获取头部视图
     *
     * @return
     */
    public View getView();


    /**
     * 正在下拉
     *
     * @param fration
     * @param maxHeadHeight
     * @param headHeight
     */
    public void HanderPullDown(int fration, int maxHeadHeight, int headHeight);


    /**
     * 释放时
     *
     * @param fration
     * @param maxHeadHeight
     * @param headHeight
     */
    public void HanderPullRealse(int fration, int maxHeadHeight, int headHeight);


}
