package com.cisetech.customer.customer.extend;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.ScrollView;

import com.cisetech.customer.customer.R;

/**
 * Author:Yqy
 * Date:2016-08-09
 * Desc:
 * Company:cisetech
 */
public class StickyNavLayout extends LinearLayout{
    /**
     * 顶部headerview
     */
    private View mTopView;
    /**
     * 需要悬停的view
     */
    private View mNavigateView;
    /**
     * 布局中的viewpager
     */
    private ViewPager mViewPager;
    /**
     * 顶部view的高度
     */
    private int mTopViewHeight;
    /**
     * ViewPager里面的ListView
     */
    private ViewGroup mInnerScrollView;
    private boolean isTopHidden;
    private OverScroller mScorller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;//滑动最小距离
    private int mMaximumVelocity,mMinimumVelocity;
    private float mLastY;
    private boolean mDragging;
    private boolean isInControl;
    public StickyNavLayout(Context context) {
        this(context,null);
    }

    public StickyNavLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StickyNavLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("StickyNavLayout", "----Construct---");
        mScorller=new OverScroller(context);
        /**
         *获取系统的一些常量值
         */
        ViewConfiguration viewConfig = ViewConfiguration.get(context);
        mTouchSlop=viewConfig.getScaledTouchSlop();
        mMaximumVelocity=viewConfig.getScaledMaximumFlingVelocity();
        mMinimumVelocity=viewConfig.getScaledMinimumFlingVelocity();
    }

    /**
     * 加载完布局文件就可以findViewById了，
     * 也可以做一些初始化的处理
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("StickyNavLayout", "----onFinishInflate---");
        mTopView=findViewById(R.id.id_stickylayout_header);
        mNavigateView=findViewById(R.id.id_stickylayout_nav);
        mViewPager= (ViewPager) findViewById(R.id.id_stickylayout_viewpager);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("StickyNavLayout", "----onMeasure---");
        /**
         * 因为ViewGroup有很多子类，所以要调用多次onMeasure方法，
         * 第一次调用的时候给params.height设置值了，
         * 下次再调用的时候就可以拿到height。
         */
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight()-mNavigateView.getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("StickyNavLayout", "----onMeasure---");
        mTopViewHeight = mTopView.getMeasuredHeight();
    }
    /**
     *事件处理最开始的方法
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action=ev.getAction();
        float y=ev.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy=y-mLastY;//移动的距离
                getCurrentScrollView();//获取当前Viewpager里面的ListView
                if(mInnerScrollView instanceof ScrollView){
                    /**
                     * 当top隐藏+下滑+不在控制中+scrollview滑到顶部了
                     * 取消此次ev改为down，iscontrol置为true
                     */
                    if(mInnerScrollView.getScrollY()==0&&isTopHidden&&dy>0&&!isInControl){
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }else if(mInnerScrollView instanceof ListView){
                        ListView lv = (ListView) mInnerScrollView;
                        View c = lv.getChildAt(lv.getFirstVisiblePosition());
                        if (!isInControl && c != null && c.getTop() == 0 && isTopHidden
                                && dy > 0) {
                            isInControl = true;
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            MotionEvent ev2 = MotionEvent.obtain(ev);
                            dispatchTouchEvent(ev);
                            ev2.setAction(MotionEvent.ACTION_DOWN);
                            return dispatchTouchEvent(ev2);
                        }
                    }
                }
        }
        return super.dispatchTouchEvent(ev);
    }
    /**
     * 处理事件拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action=ev.getAction();
        float y=ev.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy=y-mLastY;
                getCurrentScrollView();
                if(Math.abs(dy)>mTouchSlop){//大于系统最小滑动距离才开始处理
                    /**
                     * 如果topview没有隐藏
                     * 或者sc的scrollY = 0 && topView隐藏 && 下拉，则拦截
                     */
                    if(mInnerScrollView instanceof ScrollView){
                        if((!isTopHidden)||
                                (mInnerScrollView.getScrollY()==0&&isTopHidden&&dy>0)){
                            //初始化速度跟踪器
                            initVelocityTrackerIfNotExists();
                            mVelocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    }else if(mInnerScrollView instanceof ListView){
                        ListView lv= (ListView) mInnerScrollView;
                        View child = lv.getChildAt(lv.getFirstVisiblePosition());
                        if((!isTopHidden)||
                                (child!=null&&child.getTop()==0&&isTopHidden&&dy>0)){
                            //初始化速度跟踪器
                            initVelocityTrackerIfNotExists();
                            mVelocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int action=event.getAction();
        float y=event.getY();
        float dy=y-mLastY;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(!mScorller.isFinished()){
                    mScorller.abortAnimation();
                }
                mLastY=y;
                return true;
            case MotionEvent.ACTION_MOVE:
                if(!mDragging&&Math.abs(dy)>mTouchSlop){
                    mDragging=true;
                }
                if(mDragging){
                    scrollBy(0,(int)-dy);
                    // 如果topView隐藏，且上滑动时，则改变当前事件为ACTION_DOWN
                    if (getScrollY() == mTopViewHeight && dy < 0) {
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                        isInControl = false;
                    }
                }
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                recycleVelocityTracker();
                if (!mScorller.isFinished()) {
                    mScorller.abortAnimation();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if(mScorller.computeScrollOffset()){
            scrollTo(mScorller.getCurrX(),mScorller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if(y>mTopViewHeight){
            y=mTopViewHeight;
        }
        if(y<0){
            y=0;
        }
        if(y!=getScrollY()){
            super.scrollTo(x,y);
        }
        isTopHidden=getScrollY()==mTopViewHeight;
    }

    /**
     * 当Y轴上的速度（每秒滑动的距离）>系统最小速度
     * 自动滑动
     */
    private void fling(int velocity) {
        mScorller.fling(0,getScrollY(),0,velocity,0,0,0,mTopViewHeight);
        invalidate();
    }

    /**
     * 初始化速度跟踪器
     */
    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }
    /**
     * 释放速度跟踪器
     */
    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 获取当前ViewPager里面的ScrollView
     */
    private void getCurrentScrollView() {
        int currentItem=mViewPager.getCurrentItem();
        PagerAdapter adapter=mViewPager.getAdapter();
        Fragment item= (Fragment) adapter.instantiateItem(mViewPager,currentItem);
        mInnerScrollView= (ViewGroup) item.getView().findViewById(R.id.id_stickylayout_innerscrollview);
    }
}
