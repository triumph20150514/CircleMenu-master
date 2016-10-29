package com.hitomi.circlemenu.refresh.IBottomView;

import android.view.View;

/**
 * 底部控件
 * Created by tao on 2016/10/24.
 */

public interface IBottomView {

    /**
     * 过去顶部视图
     *
     * @return
     */
    public View getView();

    /**
     * 上拉准备中
     *
     * @param fration
     * @param maxBtHeight
     * @param btHeight
     */
    public void RefrshPullUp(int fration, int maxBtHeight, int btHeight);

    /**
     *
     * @param fration
     * @param maxBtHeight
     * @param btHeight
     */
    public void RefreshPullUpRealse(int fration,int maxBtHeight,int btHeight);



}
