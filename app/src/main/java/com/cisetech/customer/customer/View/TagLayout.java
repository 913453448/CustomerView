package com.cisetech.customer.customer.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Yqy
 * Date:2016-07-28
 * Desc:
 * Company:cisetech
 */
public class TagLayout extends ViewGroup implements TagAdapter.OnDataChangedListener{
    public interface OnTagChoicedListener{
        void onChoiced(View v,Object tag);
        void onClick(View v,Object tag);
    }
    private OnTagChoicedListener onTagChoicedListener;

    public void setOnTagChoicedListener(OnTagChoicedListener onTagChoicedListener) {
        this.onTagChoicedListener = onTagChoicedListener;
    }

    public TagLayout(Context context) {
        this(context,null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0;
        int height = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams childLp = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + childLp.leftMargin + childLp.rightMargin;
            int childHeight = childView.getMeasuredHeight() + childLp.topMargin + childLp.bottomMargin;
            if (lineWidth + childWidth > widthSize) {//需要换行
                width = Math.max(childWidth, lineWidth);
                height += lineHeight;
                lineHeight = childHeight;
                lineWidth = childWidth;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            /*
                最后一行的时候记录宽和高
             */
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        setMeasuredDimension(
                (widthMode == MeasureSpec.EXACTLY )? widthSize : width,
                (heightMode == MeasureSpec.EXACTLY) ? heightSize : height
        );
    }
    List<List<View>>mLineViews=new ArrayList<List<View>>();
    List<Integer>mLineHeights=new ArrayList<Integer>();
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLineViews.clear();
        mLineHeights.clear();
        int childCount=getChildCount();
        int lineWidth=0;
        int lineHeight=0;
        int width=getMeasuredWidth();

        List<View>lineViews=new ArrayList<View>();
        for (int i = 0; i <childCount ; i++) {
            View childView=getChildAt(i);
            MarginLayoutParams childLp= (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + childLp.leftMargin + childLp.rightMargin;
            int childHeight = childView.getMeasuredHeight() + childLp.topMargin + childLp.bottomMargin;
            if (lineWidth + childWidth > width) {//需要换行
                mLineViews.add(lineViews);
                mLineHeights.add(lineHeight);
                lineWidth=0;
                lineViews=new ArrayList<View>();
            }
            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            lineViews.add(childView);
        }
        mLineViews.add(lineViews);
        mLineHeights.add(lineHeight);

        int left=0;
        int top=0;
        int lineNum=mLineViews.size();
        for (int i = 0; i < lineNum; i++) {
            int mLineHeight=mLineHeights.get(i);
            for (View view:mLineViews.get(i)) {
                MarginLayoutParams lp= (MarginLayoutParams) view.getLayoutParams();
                int lc=lp.leftMargin+left;
                int tc=top+lp.topMargin;
                int rc=lc+view.getMeasuredWidth();
                int bc=tc+view.getMeasuredHeight();
                view.layout(lc,tc,rc,bc);
                left+=view.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            }
            left=0;
            top+=mLineHeight;
        }
    }
    private TagAdapter mAdapter;
    public void setAdapter(TagAdapter adapter){
        this.mAdapter=adapter;
        mAdapter.setOnDataChangedListener(this);
        changeAdapter();
    }

    private void changeAdapter() {
        removeAllViews();
        for (int i = 0; i <mAdapter.getCount() ; i++) {
            final Object item = mAdapter.getItem(i);
            final CheckedTextView view= (CheckedTextView) mAdapter.getView(null,i,mAdapter.getItem(i));
            view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(onTagChoicedListener!=null){
                        onTagChoicedListener.onClick(view, item);
                    }
                    if(!view.isChecked()){
                        if(onTagChoicedListener!=null){
                            onTagChoicedListener.onChoiced(view,item);
                        }
                    }
                    view.setChecked(!view.isChecked());
                }
            });
            addView(view);
        }
    }

    @Override
    public void onChange() {
        changeAdapter();
    }
}
