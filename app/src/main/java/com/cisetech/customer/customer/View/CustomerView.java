package com.cisetech.customer.customer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
/**
 * Author:Yqy
 * Date:2016-07-26
 * Desc:
 * Company:cisetech
 */
public class CustomerView extends View{
    private static final String mText="Welcom to !!";
    private Paint mPaint;
    public CustomerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public CustomerView(Context context) {
        this(context, null);

    }
    private void initView() {
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14.5f, getResources().getDisplayMetrics()));
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width= (int) (getPaddingLeft()+getPaddingRight()+mPaint.measureText(mText, 0, mText.length()));
        int height= (int) (mPaint.getFontMetrics().bottom-mPaint.getFontMetrics().top);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
        canvas.drawLine(0, getMeasuredHeight() / 2, getWidth(), getMeasuredHeight() / 2, mPaint);
        mPaint.setColor(Color.GREEN);
        int centerX=getMeasuredWidth()/2;
        int centerY=getMeasuredHeight()/2;
        int baseY= (int) (centerY+(mPaint.getFontMetrics().bottom-mPaint.getFontMetrics().top)/2-mPaint.getFontMetrics().bottom);
        canvas.drawText(mText, centerX - mPaint.measureText(mText) / 2, baseY, mPaint);
        //画最小矩形
        Rect minRect = new Rect();
        mPaint.getTextBounds(mText,0,mText.length(),minRect);
        minRect.top = baseY + minRect.top;
        minRect.bottom = baseY + minRect.bottom;
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(minRect,mPaint);
    }
}
