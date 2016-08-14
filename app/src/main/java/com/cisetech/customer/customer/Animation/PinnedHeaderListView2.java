package com.cisetech.customer.customer.Animation;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Author:Yqy
 * Date:2016-08-04
 * Desc:动态替换顶部view
 * Company:cisetech
 */
public class PinnedHeaderListView2 extends ListView{
    public interface OnHeaderViewChangeListener{
        int PINNED_HEADER_GONE=0;
        int PINNED_HEADER_VISIBLE=1;
        int PINNED_HEADER_PUSHED_UP=2;
        void onHeaderChange(View headerView,int position);
        int getChangeState();
    }
    private View mHeaderView;
    private OnHeaderViewChangeListener onHeaderViewChangeListener;
    private boolean isShowHeader;
    private int mHeaderWidth;
    private int mHeaderHeight;


    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        this.onHeaderViewChangeListener= (OnHeaderViewChangeListener) adapter;
    }

    public View getmHeaderView() {
        return mHeaderView;
    }

    public void setmHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
        setFadingEdgeLength(0);
    }



    public boolean isShowHeader() {
        return isShowHeader;
    }

    public void setIsShowHeader(boolean isShowHeader) {
        this.isShowHeader = isShowHeader;
    }

    public PinnedHeaderListView2(Context context) {
        this(context, null);
    }

    public PinnedHeaderListView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinnedHeaderListView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mHeaderView!=null){
            measureChild(mHeaderView,widthMeasureSpec,heightMeasureSpec);
            mHeaderWidth=mHeaderView.getMeasuredWidth();
            mHeaderHeight=mHeaderView.getMeasuredHeight();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(isShowHeader){
            drawChild(canvas,mHeaderView,getDrawingTime());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(mHeaderView!=null){
            mHeaderView.layout(0,0,mHeaderWidth,mHeaderHeight);
            configuraPinner(getFirstVisiblePosition());
        }
    }

    public void configuraPinner(int position) {
        if(mHeaderView==null){
            return;
        }
        int state=onHeaderViewChangeListener.getChangeState();
        switch (state){
            case OnHeaderViewChangeListener.PINNED_HEADER_GONE:
                mHeaderView.setVisibility(View.GONE);
                break;
            case OnHeaderViewChangeListener.PINNED_HEADER_VISIBLE:
                mHeaderView.setVisibility(View.VISIBLE);
                onHeaderViewChangeListener.onHeaderChange(mHeaderView,position);
                break;
            case OnHeaderViewChangeListener.PINNED_HEADER_PUSHED_UP:
                View childView=getChildAt(0);
                int childBottom=childView.getBottom();
                int duration=0;
                if(childBottom<mHeaderHeight){
                    duration=childBottom-mHeaderHeight;
                }else{
                    duration=0;
                }
                onHeaderViewChangeListener.onHeaderChange(mHeaderView,position);
                if(mHeaderView.getTop()!=duration){
                    mHeaderView.layout(0,duration,mHeaderWidth,duration+mHeaderHeight);
                }
                break;
        }
    }
}
