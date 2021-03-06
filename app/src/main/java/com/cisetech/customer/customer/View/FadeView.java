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
    /**
     * 需要绘制的文本
     */
    private String mText="测试";
    /**
     * 绘制的画笔
     */
    private Paint mPaint;
    /**
     * 文本size
     */
    private float mTextSize= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,14.5f,getResources().getDisplayMetrics());
    /**
     * 未变的text的颜色
     */
    private int originTextColor= Color.BLACK;
    /**
     * 变换text的颜色
     */
    private int changeTextColor=Color.RED;
    /**
     * text颜色变换的方向，是从左到右还是反之
     */
    private DIRECTION mDirection=DIRECTION.LEFT;
    /**
     * 文本绘制的开始位置
     */
    private int mTextStartX;
    /**
     * 颜色变换的进度
     */
    private float mProgress;
    /**
     * 文字的宽度
     */
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
        /////测试
        //startAnima();
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
        mTextWidth= (int) mPaint.measureText(mText,0,mText.length());
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
     * 初始化
     */
    private void initView() {
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);
        mPaint.setTextSize(mTextSize);
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
        mTextWidth= (int) mPaint.measureText(mText,0,mText.length());
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mTextWidth= (int) mPaint.measureText(mText,0,mText.length());
        int width=measureWidth(widthMeasureSpec);
        int height=measureHeight(heightMeasureSpec);
        mTextStartX=width/2-mTextWidth/2;
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
        /**
         * 当为确定值的时候就取确定值，
         * 当不确定的时候大小就用文本的高度
         * (int) (mPaint.getFontMetrics().bottom-mPaint.getFontMetrics().top);
         * 方法可以获取文本宽度
         */
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
        /**
         * 当为确定值的时候就取确定值，
         * 当不确定的时候大小就用文本的大小
         * mPaint.measureText方法可以获取文本宽度
         */
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
        /**
         * 判断绘制变化的方向，
         */
        if(mDirection==DIRECTION.LEFT){
            drawChangeLeft(canvas);
            drawOriginLeft(canvas);
        }else{
            drawOriginRight(canvas);
            drawChangeRight(canvas);
        }
    }

    /**
     * 当绘制位置为DIRECTION.RIGHT的时候
     * 绘制变换的部分，也就是红色部分
     * 裁剪的起始位置为：mTextStartX + ((1-mProgress )* mTextWidth)。
     * 终止位置为：mTextWidth + mTextStartX
     */
    private void drawChangeRight(Canvas canvas) {
        drawText(canvas,changeTextColor,(int) (mTextStartX + ((1-mProgress )* mTextWidth)),mTextWidth + mTextStartX);
    }
    /**
     * 当绘制位置为DIRECTION.RIGHT的时候
     * 绘制未变换的部分，也就是黑色部分
     * 裁剪的起始位置为：mTextStartX。
     * 终止位置为：mTextStartX + ((1-mProgress )* mTextWidth)
     */
    private void drawOriginRight(Canvas canvas) {
        drawText(canvas, originTextColor, mTextStartX, (int) (mTextStartX + ((1 - mProgress) * mTextWidth)));
    }

    private void drawOriginLeft(Canvas canvas) {
        drawText(canvas, originTextColor, (int) (mTextStartX + (mProgress * mTextWidth)), mTextWidth + mTextStartX);
    }

    private void drawChangeLeft(Canvas canvas) {
        drawText(canvas, changeTextColor, mTextStartX, (int) (mTextStartX + (mProgress * mTextWidth)));
        //drawText(canvas,changeTextColor, mTextStartX, (int) (mTextStartX + (mProgress * mTe)));
    }

    /**
     * 从startX裁剪到endX范围绘制文本
     */
    private void drawText(Canvas canvas,int color, int startX, int endX) {
        mPaint.setColor(color);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());
        /**
         * 绘制文本的y坐标，看不懂的话可以看我师傅的神作，博客有给出链接
         */
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

    /**
     * 枚举类，变换颜色的方向
     */
    public enum DIRECTION{
        LEFT,RIGHT
    }

}
