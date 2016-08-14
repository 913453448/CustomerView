package com.cisetech.customer.customer.Animation;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Author:Yqy
 * Date:2016-08-04
 * Desc:
 * Company:cisetech
 */
public class PinnedHeaderListView  extends ListView{
    public interface PinnedHeaderAdapter{
        int PINNED_HEADER_GONE=0;
        int PINNED_HEADER_VISIBLE=1;
        int PINNED_HEADER_PUSHED_UP=2;

        /**
         * 获取当前header的状态
         * @param position
         * @return
         */
        int getPinnedHeaderState(int position);

        /**
         * 当需要变换header的时候调用
         * @param header
         * @param position
         */
        void configurePinnerHeader(View header,int position);
    }
    private static final int MAX_ALPHA=255;
    private PinnedHeaderAdapter mAdapter;
    /**当前HeadView*/
    private View mHeaderView;
    private boolean mHeaderViewVisible;//headerView是否可见
    private int mHeaderViewWidth;//headView的宽度
    private int mHeaderViewHeight;//headView的高度

    @Override
       public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (PinnedHeaderAdapter) adapter;
    }

    public void setmHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
        if (mHeaderView != null) {
            setFadingEdgeLength(0);
        }
    }

    public void setmHeaderViewVisible(boolean mHeaderViewVisible) {
        this.mHeaderViewVisible = mHeaderViewVisible;
    }

    public PinnedHeaderListView(Context context) {
        this(context, null);
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取HeaderView的高度
        if(mHeaderView!=null){
            measureChild(mHeaderView,widthMeasureSpec,heightMeasureSpec);
            mHeaderViewWidth=mHeaderView.getMeasuredWidth();
            mHeaderViewHeight=mHeaderView.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mHeaderView != null) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            configureHeaderView(getFirstVisiblePosition());
            Log.i("TAG", "layout");
        }
    }

    public void configureHeaderView(int position) {
        if (mHeaderView == null) {
            return;
        }
        //调用Adapter的getPinnedHeaderState获取我们提前定义好的state，
        int state = mAdapter.getPinnedHeaderState(position);
        switch (state) {
            case PinnedHeaderAdapter.PINNED_HEADER_GONE: {
                mHeaderView.setVisibility(View.GONE);
                break;
            }
            case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE: {
                mHeaderView.setVisibility(View.VISIBLE);
                break;
            }
            /**
             *关键代码，当返回的state为滑动时，开始计算
             */
            case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);//获取ListView的第一个view
                int bottom = firstView.getBottom();//获取底部高度
                int headerHeight = mHeaderView.getHeight();//获取mHeaderView的高度
                /**
                 * 当第一个view的bottom<headerHeight的时候也就证明此时的headerView应该更新为当前position的
                 * 内容，当mHeaderView.getTop() != y的时候
                 * 开始上移mHeaderView直到写一个view替换当前的headerView,
                 * 如果实在不懂，可以像我一样打个Log运行看一遍结果就知道了^_^!
                 */
                int y;
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                } else {
                    y = 0;
                }
                mAdapter.configurePinnerHeader(mHeaderView, position);
                Log.e("PinnerListView", "bottom---->" + bottom + " headerHeight--->"
                        + headerHeight + " top-->" + mHeaderView.getTop()+" y-->"+y);
                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight
                            + y);
                }
                mHeaderViewVisible = true;
                break;
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(mHeaderViewVisible){
            drawChild(canvas, mHeaderView, getDrawingTime());
          //  Log.e("PinnerListView", "dispatchDraw: childCount---->"+getChildCount() );
        }
    }
}
