package com.cisetech.customer.customer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Author:Yqy
 * Date:2016-08-01
 * Desc:
 * Company:cisetech
 */
public class ProgressBarWithNumber extends ProgressBar {
    private Paint mPaint;
    private int mTextColor=Color.RED;
    private int mTextSize= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,14.5f,getResources().getDisplayMetrics());
    private int mRadius= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,getResources().getDisplayMetrics());
    private int innerWidth=2;
    private int outerWidth=4;
    private int offset=0;
    private int mOuterColor=Color.BLACK;

    public ProgressBarWithNumber(Context context) {
        this(context, null);
    }

    public ProgressBarWithNumber(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarWithNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setMax(100);
        startAnim();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int result=getPaddingLeft()+getPaddingRight()+mRadius*2+Math.max(innerWidth,outerWidth)*2+offset*2;
        int width=resolveSize(result,widthMeasureSpec);
        int height=resolveSize(result,heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int width=getMeasuredWidth();
        int height=getMeasuredHeight();
        //draw circle
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0x86FFFF00);
        mPaint.setStrokeWidth(innerWidth);
        canvas.drawCircle(width / 2, height / 2, mRadius, mPaint);
        //draw outer
        mPaint.setColor(mOuterColor);
        mPaint.setStyle(Paint.Style.FILL);
        int sweepAnglg= (int) (getProgress()*1.0f/getMax()*360);
        String text;
        if(!flag){
            text=getProgress()+"%";
            canvas.drawArc(new RectF(getPaddingLeft(),getPaddingTop(),
                    getMeasuredWidth()-getPaddingRight()-innerWidth,getPaddingTop()+getMeasuredHeight()-getPaddingBottom()-innerWidth)
                    ,0,sweepAnglg,true,mPaint);
        }else{
            text= getMax()-getProgress()+"%";
            canvas.drawArc(new RectF(getPaddingLeft(),getPaddingTop(),
                    getMeasuredWidth()-getPaddingRight()-innerWidth,getPaddingTop()+getMeasuredHeight()-getPaddingBottom()-innerWidth)
                    ,sweepAnglg,360-sweepAnglg,true,mPaint);
        }
        //drawText
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(innerWidth);
        int baseX= (int) (getMeasuredWidth()/2-mPaint.measureText(text, 0, text.length())/2);
        int baseY= (int) (getMeasuredHeight()/2+(mPaint.getFontMetrics().bottom-mPaint.getFontMetrics().top)/2-
                mPaint.getFontMetrics().bottom);
        canvas.drawText(text, baseX, baseY, mPaint);
    }

    private void initView() {
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(innerWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setTextSize(mTextSize);
    }
    private boolean flag=true;
    public void startAnim(){
        new Thread(){
            @Override
            public void run() {
                int i=1;
                while(i<=100){
                    try {
                        Thread.sleep(30);
                        if(i==100){
                            i=1;
                            flag=!flag;
                        }
                        setProgress(i);
                        handler.sendEmptyMessage(0);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
       /* ValueAnimator a=ValueAnimator.ofInt(0,getMax()+1);
        a.setDuration(3000);
        a.setInterpolator(new LinearInterpolator());
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value >= getMax()) {
                    flag=!flag;
                    Log.i("ProgressBar", "onAnimationUpdate: "+value+"--flag--"+flag);
                }
                setProgress(value);
                postInvalidate();
            }
        });
        a.start();
        a.
        a.setRepeatCount(ValueAnimator.INFINITE);*/
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };
}
