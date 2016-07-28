package com.cisetech.customer.customer.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Author:Yqy
 * Date:2016-07-26
 * Desc:
 * Company:cisetech
 */
public class GuaGuaView extends  View {
    private static final String mText="一等奖";
    private Paint mPaint;
    private int screenW,screenH;
    private Path mPath;
    public GuaGuaView(Context context) {
        this(context, null);
    }
    public GuaGuaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaGuaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14.5f, getResources().getDisplayMetrics()));
        mPaint.setColor(Color.RED);
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenW=outMetrics.widthPixels;
        screenH=outMetrics.heightPixels;
        dstBitmap=Bitmap.createBitmap(screenW,screenH, Bitmap.Config.ARGB_8888);
        mPath=new Path();
        srcBitmap=Bitmap.createBitmap(screenW,screenH,Bitmap.Config.ARGB_8888);
        setClickable(true);
    }
    private Bitmap dstBitmap;
    private Bitmap srcBitmap;
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        int baseX= (int) (getWidth()/2-mPaint.measureText(mText)/2);
        int baseY= (int) (getHeight()/2+(mPaint.getFontMetrics().bottom-mPaint.getFontMetrics().top)/2
                        -mPaint.getFontMetrics().bottom);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(mText, baseX, baseY, mPaint);
        if(!isComplete){
            mPaint.setStyle(Paint.Style.STROKE);
            int layerId=canvas.saveLayer(0,0,getWidth(),getHeight(),mPaint,Canvas.ALL_SAVE_FLAG);
            Canvas desCavas=new Canvas(dstBitmap);
            mPaint.setStrokeWidth(10);
            desCavas.drawPath(mPath, mPaint);
            canvas.drawBitmap(dstBitmap, 0, 0, mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            Canvas srcCanvas=new Canvas(srcBitmap);
            srcCanvas.drawColor(Color.GRAY);
            canvas.drawBitmap(srcBitmap, 0, 0, mPaint);
            mPaint.setXfermode(null);
            canvas.restoreToCount(layerId);

           // countSwipeArea();
        }
    }
    float preX,preY;
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
            case MotionEvent.ACTION_UP:
                new Thread(){
                    @Override
                    public void run() {
                        countSwipeArea();
                    }
                }.start();
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }
    private boolean isComplete;
    public void countSwipeArea(){
        int width=getWidth();
        int height=getHeight();
        int totalArea=width*height;
        Bitmap mBitmap=dstBitmap;
        int[] pixels=new int[totalArea];
        mBitmap.getPixels(pixels,0,width,0,0,width,height);
        int swipeArea=0;
        for (int i = 0; i <width ; i++) {
            for (int j = 0; j <height ; j++) {
                int index=i+j*width;
                if(pixels[index]==0){
                    swipeArea++;
                }
            }
        }
        if(swipeArea!=0&&totalArea!=0){
            double rate=(totalArea-swipeArea)*1.0d/totalArea;
            isComplete=(rate>=0.5);
            if(Looper.getMainLooper()==Looper.myLooper()){
                invalidate();
            }else{
                postInvalidate();
            }
        }
    }
}
