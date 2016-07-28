package com.cisetech.customer.customer.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.cisetech.customer.customer.R;

/**
 * Author:Yqy
 * Date:2016-07-27
 * Desc:渐变view
 * Company:cisetech
 */
public class FadeView extends View{
    private String mText="你好吗？？？";
    private Paint mPaint;
    private float mTextSize= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,14.5f,getResources().getDisplayMetrics());
    private int originTextColor= Color.BLACK;
    private int changeTextColor=Color.RED;
    private int mRealWidth;
    private int mRealHeiht;
    private DIRECTION mDirection=DIRECTION.LEFT;
    private int mTextStartX;
    private float mProgress=0.0f;
    private int mTextWidth;
    public FadeView(Context context) {
        this(context, null);
    }

    public FadeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FadeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttr(context, attrs, defStyleAttr);
        initView();
        //startAnima();
    }

    public float getmProgress() {
        return mProgress;
    }

    public synchronized  void setmProgress(float mProgress) {
        this.mProgress = mProgress;
        flushView();
    }

    public DIRECTION getmDirection() {
        return mDirection;
    }

    public void setmDirection(DIRECTION mDirection) {
        this.mDirection = mDirection;
    }

    private void flushView() {
        if(Looper.getMainLooper()==Looper.myLooper()){
            invalidate();
        }else{
            postInvalidate();
        }
    }

    private void obtainAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.fadeView, defStyleAttr, 0);
        for (int i = 0; i <a.getIndexCount() ; i++) {
            int attr=a.getIndex(i);
            switch (attr){
                case R.styleable.fadeView_fadeText:
                    mText=a.getString(attr);
                    break;
                case R.styleable.fadeView_changeColor:
                    changeTextColor=a.getColor(attr, changeTextColor);
                    break;
                case R.styleable.fadeView_originColor:
                    originTextColor=a.getColor(attr, originTextColor);
                    break;
                case R.styleable.fadeView_textSize:
                    mTextSize=a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14.5f, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.fadeView_direction:
                    int flag = a.getInt(attr, 1);
                    if(flag==1){
                        mDirection=DIRECTION.LEFT;
                    }else{
                        mDirection=DIRECTION.RIGHT;
                    }
                    break;
            }
        }
        a.recycle();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);
        mPaint.setTextSize(mTextSize);
        mTextWidth= (int) mPaint.measureText(mText,0,mText.length());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=measureWidth(widthMeasureSpec);
        int height=measureHeight(heightMeasureSpec);
        mRealWidth=width-(getPaddingLeft()+getPaddingRight());
        mRealHeiht=height-(getPaddingTop()+getPaddingBottom());
        mTextStartX=mRealWidth/2-mTextWidth/2;
        setMeasuredDimension(width,height);
    }

    /**
     *
     * @param heightMeasureSpec
     * @return 测量过后的height
     */
    private int measureHeight(int heightMeasureSpec) {
        int result;
        int mode=MeasureSpec.getMode(heightMeasureSpec);
        int size=MeasureSpec.getSize(heightMeasureSpec);
        if(mode==MeasureSpec.EXACTLY){
            result=size;
        }else{
            result= (int) (mPaint.getFontMetrics().bottom-mPaint.getFontMetrics().top);
        }
        result=mode==MeasureSpec.AT_MOST?Math.min(result,size):result;
        return result+getPaddingTop()+getPaddingBottom();
    }

    /**
     *
     * @param widthMeasureSpec
     * @return 测量过后的width
     */
    private int measureWidth(int widthMeasureSpec) {
        int result;
        int mode=MeasureSpec.getMode(widthMeasureSpec);
        int size=MeasureSpec.getSize(widthMeasureSpec);
        if(mode==MeasureSpec.EXACTLY){
            result=size;
        }else{
            result= (int) mPaint.measureText(mText,0,mText.length());
        }
        result=mode==MeasureSpec.AT_MOST?Math.min(result,size):result;
        return result+getPaddingLeft()+getPaddingRight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mDirection==DIRECTION.LEFT){
            drawChangeLeft(canvas);
            drawOriginLeft(canvas);
        }else{
            drawOriginRight(canvas);
            drawChangeRight(canvas);
        }
    }

    private void drawChangeRight(Canvas canvas) {
        drawText(canvas,changeTextColor,(int) (mTextStartX + ((1-mProgress )* mRealWidth)),mTextWidth + mTextStartX);
    }

    private void drawOriginRight(Canvas canvas) {
        drawText(canvas,originTextColor,mTextStartX,(int) (mTextStartX + ((1-mProgress )* mRealWidth)));
    }

    private void drawOriginLeft(Canvas canvas) {
        drawText(canvas, originTextColor, (int) (mTextStartX + (mProgress * mRealWidth)), mTextWidth + mTextStartX);
    }

    private void drawChangeLeft(Canvas canvas) {
        drawText(canvas,changeTextColor, mTextStartX, (int) (mTextStartX + (mProgress * mRealWidth)));
    }

    private void drawText(Canvas canvas,int color, int startX, int endX) {
        mPaint.setColor(color);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());
        int baseY= (int) (getMeasuredHeight()/2+(mPaint.getFontMetrics().bottom-mPaint
                .getFontMetrics().top)/2-mPaint.getFontMetrics().bottom);
        canvas.drawText(mText,mTextStartX,baseY,mPaint);
        canvas.restore();
    }
    public void startAnima(){
        ValueAnimator animator=ValueAnimator.ofFloat(0,1.0f);
        animator.setDuration(1500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
    public enum DIRECTION{
        LEFT,RIGHT;
    }

}
