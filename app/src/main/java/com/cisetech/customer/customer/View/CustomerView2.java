package com.cisetech.customer.customer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/**
 * Author:Yqy
 * Date:2016-07-26
 * Desc:
 * Company:cisetech
 */
public class CustomerView2 extends View{
    private Paint mPaint;
    private Path mPath;
    public CustomerView2(Context context) {
        this(context, null);
    }

    public CustomerView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPath=new Path();
        setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath,mPaint);
    }
    private float preX,preY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action =event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                preX=event.getX();
                preY=event.getY();
                mPath.moveTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.quadTo(preX,preY,event.getX(),event.getY());
                preX=event.getX();
                preY=event.getY();
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }
}
