package com.lcodecore.tkrefreshlayout.header;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.R;

/**
 * Created by lcodecore on 2016/10/2.
 */

public class SinaRefreshView extends FrameLayout implements IHeaderView {

    private ImageView refreshArrow;
    private ImageView loadingView;
    private TextView refreshTextView;
    private View rootView;

    public SinaRefreshView(Context context) {
        this(context, null);
    }

    public SinaRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SinaRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (rootView == null) {
            rootView = View.inflate(getContext(), R.layout.view_sinaheader, null);
            refreshArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
            refreshTextView = (TextView) rootView.findViewById(R.id.tv);
            loadingView = (ImageView) rootView.findViewById(R.id.iv_loading);
            addView(rootView);
        }
    }

    public void setArrowResource(@DrawableRes int resId) {
        refreshArrow.setImageResource(resId);
    }

    public void setPullDownStr(String pullDownStr1) {
        pullDownStr = pullDownStr1;
    }

    public void setReleaseRefreshStr(String releaseRefreshStr1) {
        releaseRefreshStr = releaseRefreshStr1;
    }

    public void setRefreshingStr(String refreshingStr1) {
        refreshingStr = refreshingStr1;
    }

    private String pullDownStr = "下拉刷新";
    private String releaseRefreshStr = "释放刷新";
    private String refreshingStr = "正在刷新";

    @Override
    public View getView() {
        return this;
    }

    /***
     * 。。。。。。。。。。。
     * 也可以这么玩
     * fraction 最大值  maxHeadHeight/headHeight
     * 刚好可以做到 fraction * headHeight / maxHeadHeight * 180=180
     *
     * @param fraction      当前下拉高度与总高度的比
     * @param maxHeadHeight
     * @param headHeight
     */
    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) refreshTextView.setText(pullDownStr);
        if (fraction > 1f) refreshTextView.setText(releaseRefreshStr);
        refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);

    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) {
            refreshTextView.setText(pullDownStr);
            refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
            if (refreshArrow.getVisibility() == GONE) {
                refreshArrow.setVisibility(VISIBLE);
                loadingView.setVisibility(GONE);
            }
        }
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        refreshTextView.setText(refreshingStr);
        refreshArrow.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
    }

    /**
     * 刷新结束时执行
     */
    @Override
    public void onFinish() {

    }
}