package com.cisetech.customer.customer.Animation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Author:Yqy
 * Date:2016-08-03
 * Desc:拉伸开门View
 * Company:cisetech
 */
public class PullDoorView extends FrameLayout{
    private ImageView mImageView;
    private TextView mHintView;
    private Context mContext;
    private Scroller scroller;
    public PullDoorView(Context context) {
        this(context, null);
    }

    public PullDoorView(Context context, AttributeSet attrs) {
       this(context, attrs, 0);
    }

    public PullDoorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        setUpView();
    }

    /**
     * 初始化View
     */
    private void setUpView() {
        scroller=new Scroller(mContext,new BounceInterpolator());
        mImageView=new ImageView(mContext);
        FrameLayout.LayoutParams lp1=new FrameLayout.LayoutParams(-1,-1);
        mImageView.setLayoutParams(lp1);
        mImageView.setImageDrawable(new ColorDrawable(Color.BLUE));
        addView(mImageView);

        mHintView=new TextView(mContext);
        mHintView.setTextColor(Color.RED);
        mHintView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.5f);
        mHintView.setText(String.format("%s你好，请%s", "yqy", "上拉刷新"));
        FrameLayout.LayoutParams lp2=new FrameLayout.LayoutParams(-2,-2);
        lp2.gravity= Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
        mHintView.setGravity(Gravity.CENTER);
        mHintView.setLayoutParams(lp2);
        Animation a=new AlphaAnimation(0,1.0f);
        a.setRepeatCount(Animation.INFINITE);
        a.setRepeatMode(Animation.REVERSE);
        a.setDuration(500);
        //mHintView.startAnimation(a);
        addView(mHintView);
        setLongClickable(true);
    }
    public void pullDoorAnima(int startY,int dy,int duration){
        scroller.startScroll(0,startY,0,dy,duration);
        postInvalidate();
    }
    private float mLastY;
    private float mDelaY;
    private boolean isFling;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action=ev.getAction();
        float currX=ev.getX();
        float currY=ev.getY();
        mDelaY=mLastY-currY;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(mDelaY>0){
                    scrollTo(0, (int) mDelaY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mDelaY>=getMeasuredHeight()/2){
                    isFling=true;
                    pullDoorAnima(this.getScrollY(),getMeasuredHeight(),500);
                }else{
                    pullDoorAnima(this.getScrollY(),-this.getScrollY(),500);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        Log.e("PullDoorView", "computeScroll: " );
        if(scroller.computeScrollOffset()){//获取当前动画位置，当结束的时候返回false
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
        }else{
            if(isFling){
                this.setVisibility(View.GONE);
            }
        }
    }
}
