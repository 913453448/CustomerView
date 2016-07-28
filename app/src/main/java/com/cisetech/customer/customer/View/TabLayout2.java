package com.cisetech.customer.customer.View;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Yqy
 * Date:2016-07-28
 * Desc:
 * Company:cisetech
 */
public class TabLayout2 extends ViewGroup implements TagAdapter.OnDataChangedListener {
    public interface OnTagClickListener{
        void onTagClick(View view,int position,TabLayout2 parent,boolean isChoice);
    }
    private OnTagClickListener onTagClickListener;

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    public TabLayout2(Context context) {
        this(context, null);
    }

    public TabLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int width=0;
        int height=0;
        int lineWidth=0;
        int lineHeight=0;
        int childCount=getChildCount();
        for (int i = 0; i <childCount ; i++) {
            View childView=getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams childLp= (MarginLayoutParams) childView.getLayoutParams();
            int childWidth=childView.getMeasuredWidth()+childLp.leftMargin+childLp.rightMargin;
            int childHeight=childView.getMeasuredHeight()+childLp.topMargin+childLp.bottomMargin;
            if(lineWidth+childWidth>widthSize){//需要换行
                height+=lineHeight;
                width=Math.max(lineWidth,childWidth);
                lineHeight=childHeight;
                lineWidth=childWidth;
            }else{
                lineHeight=Math.max(childHeight,lineHeight);
                lineWidth+=childWidth;
            }
            if(i==childCount-1){
                width=Math.max(width,lineWidth);
                height+=lineHeight;
            }
        }
        setMeasuredDimension(
                (widthMode==MeasureSpec.EXACTLY)?widthSize:width,
                (heightMode==MeasureSpec.EXACTLY)?heightSize:height
        );
    }
    private List<List<View>>mLineViews=new ArrayList<List<View>>();
    private List<Integer>mLineHeights=new ArrayList<Integer>();
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLineViews.clear();
        mLineHeights.clear();
        int width=getMeasuredWidth();
        int childCount=getChildCount();
        int lineWidth=0;
        int lineHeight=0;

        List<View>lineViews=new ArrayList<View>();
        for (int i = 0; i <childCount ; i++) {
            View childView=getChildAt(i);
            MarginLayoutParams childLp= (MarginLayoutParams) childView.getLayoutParams();
            int childWidth=childView.getMeasuredWidth()+childLp.leftMargin+childLp.rightMargin;
            int childHeight=childView.getMeasuredHeight()+childLp.topMargin+childLp.bottomMargin;
            if(lineWidth+childWidth>width){//需要换行
                mLineViews.add(lineViews);
                mLineHeights.add(lineHeight);
                lineViews=new ArrayList<View>();
                lineHeight=0;
                lineWidth=0;
            }
            lineHeight=Math.max(lineHeight,childHeight);
            lineWidth+=childWidth;
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
        TagView tagViewContainer=null;
        for (int i = 0; i <mAdapter.getCount() ; i++) {
            tagViewContainer=new TagView(getContext());
            View tagView=mAdapter.getView(this,i,mAdapter.getItem(i));
            tagView.setDuplicateParentStateEnabled(true);
            tagViewContainer.setLayoutParams(tagView.getLayoutParams());
            tagViewContainer.addView(tagView);
            addView(tagViewContainer);
        }
    }

    @Override
    public void onChange() {
        changeAdapter();
    }
    private MotionEvent mMotionEvent;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_UP){
            mMotionEvent=MotionEvent.obtain(event);
        }
        return super.onTouchEvent(event);
    }
    @Override
    public boolean performClick() {
        if(mMotionEvent!=null){
            int x= (int) mMotionEvent.getX();
            int y= (int) mMotionEvent.getY();
            mMotionEvent=null;
            TagView child=findChild(x,y);
            int pos=findPosByChild(child);
            if(child!=null){
                doSelect(child,pos);
            }
        }
        return super.performClick();
    }

    /**
     * 是否支持多选
     * 默认为false
     */
    private boolean isSupportMutiply;

    public boolean isSupportMutiply() {
        return isSupportMutiply;
    }

    public void setIsSupportMutiply(boolean isSupportMutiply) {
        this.isSupportMutiply = isSupportMutiply;
    }

    private void doSelect(TagView child, int pos) {
        if(isSupportMutiply){
            if(onTagClickListener!=null){
                onTagClickListener.onTagClick(child,pos,this,!child.isChecked());
            }
            child.setChecked(!child.isChecked());
        }else{//单选
            for (int i = 0; i <getChildCount() ; i++) {
                if(i==pos){
                    child.setChecked(true);
                    if(onTagClickListener!=null){
                        onTagClickListener.onTagClick(child,pos,this,true);
                    }
                }else{
                    TagView tag= (TagView) getChildAt(i);
                    tag.setChecked(false);
                }
            }
        }
    }

    private int findPosByChild(TagView child) {
        for (int i = 0; i <getChildCount(); i++) {
            if(child==getChildAt(i)){
                return i;
            }
        }
        return -1;
    }

    private TagView findChild(int x, int y) {
        int count=getChildCount();
        for (int i = 0; i <count ; i++) {
            TagView view= (TagView) getChildAt(i);
            int[] location=new int[2];
            view.getLocationOnScreen(location);
            Rect rect=new Rect();
            rect.left=view.getLeft();
            rect.top=view.getTop();
            rect.right=rect.left+view.getWidth();
            rect.bottom=rect.top+view.getHeight();
            if(rect.contains(x,y)){
                //Toast.makeText(getContext(),((CheckedTextView)view.getTargetView()).getText().toString(), Toast.LENGTH_SHORT).show();
                return view;
            }
        }
        return null;
    }
}
