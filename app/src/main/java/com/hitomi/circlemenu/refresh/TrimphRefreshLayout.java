package com.hitomi.circlemenu.refresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.hitomi.circlemenu.refresh.IBottomView.IBottomView;
import com.hitomi.circlemenu.refresh.IHander.GoogleDotView;
import com.hitomi.circlemenu.refresh.IHander.IHanderView;
import com.hitomi.circlemenu.utils.ScrollingUtil;

/**
 * 刷新控件
 * Created by tao on 2016/10/24.
 */

public class TrimphRefreshLayout extends FrameLayout {

    private static final int PULL_DOWN_REFRESH = 1;//标志当前进入的刷新模式
    private static final int PULL_UP_LOAD = 2;
    private int state = PULL_DOWN_REFRESH;

    private IHanderView handerView;
    private IBottomView bottomView;

    private FrameLayout handerLayout;
    private FrameLayout bottomLayout;

    private float handerHeight = 180;
    private int bootomHeight;

    private Context mContext;

    private View childView;

    private float downY;
    private boolean isRefreshing = false;
    private boolean isLoadmore = false;

    //
    public DecelerateInterpolator overshootInterpolator;

    //波浪高度
    public float waveHeight = 280;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public TrimphRefreshLayout(Context context) {
        this(context, null);

    }

    public TrimphRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrimphRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        overshootInterpolator = new DecelerateInterpolator(10);

        //初始化
        setRefreshListener(new SimapleListener());
    }

    public int mTouSlip = 8;
    //手势识别
    private GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isRefreshing && distanceY >= mTouSlip) finishRefreshing();
            if (isLoadmore && distanceY <= -mTouSlip) finishLoadMore();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }


        /**
         *
         * @param e1 第一次按下(down)开始滑动(fling)的移动事件
         * @param e2
         * @param velocityX  X轴方向滑动速度
         * @param velocityY Y轴方向滑动速度
         * @return
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e("Trimph", "velocityY:" + velocityY);
            mVelocityY = velocityY;

            //既不是abslistview 也不是RecycleView
            if (!(childView instanceof AbsListView || childView instanceof RecyclerView)) {
                if (mHandler != null) {

                }
            }
            Log.e("Trimph", "velocityY:  super.onFling(e1, e2, velocityX, velocityY)" + super.onFling(e1, e2, velocityX, velocityY));
            return false;
        }
    });

    private float mVelocityY;

    /**
     * 結束下拉刷新呢
     */
    private void finishRefreshing() {
        isRefreshing = false;
        backChildHeight(0);
    }

    /**
     * 結束下拉加載
     */
    private void finishLoadMore() {

    }


    /**
     * 事件拦截
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (ev.getY() - downY);
                //下拉刷新
                if (dy > 0 && !canChildScrollUp()) {
                    state = PULL_DOWN_REFRESH;
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isRefreshing) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //拦截事件传递到这里
                float dy = event.getY() - downY;

                //下拉刷新
                if (state == PULL_DOWN_REFRESH) {
                    dy = Math.min(dy, waveHeight * 2);
                    dy = Math.max(0, dy);
                    Log.e("Trimph", "dy" + dy);
                    Log.e("Trimph", "dy  tttt:" + overshootInterpolator.getInterpolation(1));
                    if (childView != null) {
                        float offsetY = overshootInterpolator.getInterpolation(dy / waveHeight / 2) * dy / 2;
                        Log.e("Trimph", "offsetY" + offsetY);
                        childView.setTranslationY(offsetY);

                        handerLayout.getLayoutParams().height = (int) offsetY;
                        handerLayout.requestLayout();

                        if (refreshListener != null) {
                            refreshListener.pullDowning(this, (int) (offsetY / handerHeight));
                        }

                    }

                }

                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                if (childView != null) {
                    if (state == PULL_DOWN_REFRESH) {
                        Log.e("Trimph", "childView TranslationY" + childView.getTranslationY());
                        if (childView.getTranslationY() >= handerHeight - 8) {
                            isRefreshing = true;
                            backChildHeight(handerHeight);
                            if (refreshListener != null) {
                                refreshListener.onRefresh(TrimphRefreshLayout.this);
                            }
                        } else {
                            //若下拉距離 不大於頭部的 高度  就隱藏它
                            backChildHeight(0);
                        }
                    }
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void backChildHeight(float height) {
        Log.e("Trimph", "TranslationY backChildHeight " + childView.getTranslationY());
        Log.e("Trimph", "TranslationY backChildHeight handerHeight" + height);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(childView, "translationY", childView.getTranslationY(), height);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) childView.getTranslationY();
                height = Math.abs(height);
                if (state == PULL_DOWN_REFRESH) {
                    Log.e("Trimph", "height" + height);
                    handerLayout.getLayoutParams().height = height;
                    handerLayout.requestLayout();
                    if (refreshListener != null) {
//                        refreshListener.onPullDownReleasing(TrimphRefreshLayout.this, height / handerHeight);
                    }
                }
            }
        });
        objectAnimator.start();
    }


    /**
     * 当列表第一个可见时（即屏幕的第一条数据是列表的第一条） 用户下拉时这个时候 返回false 表示不可滚动 用户可以根据它来判断刷新控件出现的时机，
     * 判斷是否可以滾動
     *
     * @return
     */
    public boolean canChildScrollUp() {
        if (childView == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 14) {
            if (childView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) childView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(childView, -1) || childView.getScrollY() > 0;
            }
        } else {
            Log.e("Trimph", "canScrollVertically" + ViewCompat.canScrollVertically(childView, -1));
            //-1 表示下拉的方向  尜尜
            return ViewCompat.canScrollVertically(childView, -1);
        }
    }

    /**
     * 当第一次添加到window时创建
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //添加头部视图
        if (handerLayout == null) {
            FrameLayout mHanderLayout = new FrameLayout(mContext);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            layoutParams.gravity = Gravity.TOP;
            mHanderLayout.setLayoutParams(layoutParams);
            handerLayout = mHanderLayout;

            this.addView(handerLayout);

            if (handerView == null) {
                setHanderView(new GoogleDotView(mContext));
            }
        }

        if (bottomLayout == null) {
            FrameLayout frameLayout = new FrameLayout(mContext);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            layoutParams.gravity = Gravity.BOTTOM;
            frameLayout.setLayoutParams(layoutParams);
            bottomLayout = frameLayout;
            this.addView(bottomLayout);
        }

        childView = getChildAt(0);

        /**
         * 子控件事件
         */
        if (childView != null) {
            childView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

            if (childView instanceof AbsListView) {
                ((AbsListView) childView).setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (!isRefreshing && !isLoadmore && firstVisibleItem == 0 || ((AbsListView) childView).getLastVisiblePosition() == totalItemCount - 1) {
                            if (mVelocityY >= 5000 && ScrollingUtil.isAbsListViewToTop((AbsListView) childView)) {
                                animOverScrollTop();
                            }

                            if (mVelocityY <= -5000 && ScrollingUtil.isAbsListViewToBottom((AbsListView) childView)) {
                                animOverScrollBottom();
                            }
                        }
                    }
                });
            } else if (childView instanceof RecyclerView) {
                ((RecyclerView) childView).setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);


                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });
            }


        }

        childView.animate().setInterpolator(new DecelerateInterpolator()); //设置速率为递增

    }

    /**
     * 滚动到底部
     * 正在刷新时 速度大于 -5000 滚动得到最底部
     */
    private void animOverScrollBottom() {

    }

    /**
     * 当加载跟多是 滑动速度大于
     * 滚动到顶部
     */
    private void animOverScrollTop() {

        //加载动画隐藏 然后滚动到顶部
        childView.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 150);

    }

    /**
     * 设置头部视图
     *
     * @param handerView
     */
    private void setHanderView(IHanderView handerView) {
        if (handerView != null) {
            handerLayout.removeAllViews();
            handerLayout.addView(handerView.getView());
        }
    }

    /***
     * window销毁时执行
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private RefreshListener refreshListener;


    public RefreshListener getRefreshListener() {
        return refreshListener;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }


    public OnReFreshListener onReFreshListener;

    public OnReFreshListener getOnReFreshListener() {
        return onReFreshListener;
    }

    public void setOnReFreshListener(OnReFreshListener onReFreshListener) {
        this.onReFreshListener = onReFreshListener;
    }

    public class OnReFreshListener implements RefreshListener {

        @Override
        public void pullDowning(TrimphRefreshLayout trimphRefreshLayout, int offset) {

        }

        @Override
        public void onPullDownReleasing(TrimphRefreshLayout trimphRefreshLayout, int offset) {

        }

        @Override
        public void onRefresh(TrimphRefreshLayout refreshLayout) {

        }
    }

    /**
     *
     */
    public class SimapleListener implements RefreshListener {

        @Override
        public void pullDowning(TrimphRefreshLayout trimphRefreshLayout, int offset) {
            if (onReFreshListener != null) {
                onReFreshListener.pullDowning(trimphRefreshLayout, offset);
            }
        }

        @Override
        public void onPullDownReleasing(TrimphRefreshLayout trimphRefreshLayout, int offset) {
            if (onReFreshListener != null) {
                onReFreshListener.onPullDownReleasing(trimphRefreshLayout, offset);
            }
        }

        @Override
        public void onRefresh(TrimphRefreshLayout refreshLayout) {
            if (onReFreshListener != null) {
                onReFreshListener.onRefresh(refreshLayout);
            }
        }
    }

    /**
     * 监听借口
     */
    public interface RefreshListener {
        /**
         * 下拉中
         */
        public void pullDowning(TrimphRefreshLayout trimphRefreshLayout, int offset);

        /**
         * 下拉释放
         */
        public void onPullDownReleasing(TrimphRefreshLayout trimphRefreshLayout, int offset);

        /*
         刷新中
         */
        void onRefresh(TrimphRefreshLayout refreshLayout);
    }


}
