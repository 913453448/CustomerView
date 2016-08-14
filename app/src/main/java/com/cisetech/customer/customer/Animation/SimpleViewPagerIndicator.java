package com.cisetech.customer.customer.Animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.cisetech.customer.customer.View.FadeView;

/**
 * Author:Yqy
 * Date:2016-08-05
 * Desc:
 * Company:cisetech
 */
public class SimpleViewPagerIndicator extends LinearLayout {
    /**
     * 定义当点击每个FadeTextView的回调接口
     */
    public interface OnIndicatorClickListener{
        void indicatorClick(View view,int position);
    }
    private OnIndicatorClickListener onIndicatorClickListener;

    public void setOnIndicatorClickListener(OnIndicatorClickListener onIndicatorClickListener) {
        this.onIndicatorClickListener = onIndicatorClickListener;
    }

    /**
     * titles
     */
    private String[]titles;
    private Paint mPaint;
    /**
     * 线条的宽度=控件的宽度/子控件的个数
     */
    private float mLineWidth;
    /**
     * 距离底部的Padding值，可忽略
     */
    private int mLinePading= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,getResources().getDisplayMetrics());
    /**
     * 底部线条绘制的开始位置x
     */
    private float mLineStartX;

    public SimpleViewPagerIndicator(Context context) {
        this(context, null);
    }

    public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mLinePading, getResources().getDisplayMetrics()));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public String[] getTitles() {
        return titles;
    }

    /**
     * 设置titles
     */
    public void setTitles(String[] titles) {
        this.titles = titles;
        setupTitles();
    }

    public float getmLineStartX() {
        return mLineStartX;
    }

    public void setmLineStartX(float mLineStartX) {
        this.mLineStartX = mLineStartX;
        if(Looper.getMainLooper()==Looper.myLooper()){
            invalidate();
        }else{
            postInvalidate();
        }
    }

    /**
     *添加titles到父控件中，
     * 并给每个子控设置点击监听
     */
    private void setupTitles() {
        if (getChildCount() > 0)
            this.removeAllViews();
        setWeightSum(titles.length);
        for (int i = 0; i < titles.length; i++) {
            final int j=i;
            final FadeView textView=new FadeView(getContext());
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
            lp.weight=1;
            textView.setLayoutParams(lp);
            textView.setmText(titles[i]);
            textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(onIndicatorClickListener!=null){
                        onIndicatorClickListener.indicatorClick(v, j);
                    }
                }
            });
            addView(textView);
        }
    }

    /**
     * 当控件的size发生变化的时候会调用此方法
     * 一般在这获取控件的宽高
     * 我们在这获取线条的宽度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLineWidth=w/titles.length;
    }

    /**
     * 重写dispatchDraw方法绘制底部线条
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        /**
         * 通过平移canvas移动线条位置
         * mLineStartX
         */
        canvas.translate(mLineStartX,getHeight()-mLinePading);
        canvas.drawLine(0, 0, mLineWidth, 0, mPaint);
        canvas.restore();
    }

    /**
     * 通过监听ViewParger传进来当前ViewPager位置，
     * 以及偏移量（0-1）
     * 当从第0页切换到第一页的时候 position（int 0-1）offset(float 0-1)
     */
    public void setOffset(final int position, final float positionOffset) {
        /**
         * 线条开始的位置=线条的宽度*position+mLineWidth*positionOffset
         * 数学一样跟我不是很好的同学可以打个log看看值就懂了
         */
        float startX = mLineWidth * (position + positionOffset);
        setmLineStartX(startX);
        /**
         * 为什么是小于0.1呢，mPager.setCurrentItem(position,true);
         * 比如当前ViewPager位置是3，然后切换到0，跨了一个item，
         * 有点小bug，所以测试了一下当《0.1的，状态都重新切换一次就不会有bug了
         * 如果是mPager.setCurrentItem(position,false);不用动画切换的是不会有bug的
         */
        if (positionOffset < 0.1) {
            for (int k = 0; k < getChildCount(); k++) {
                FadeView fade = (FadeView) getChildAt(k);
                if (position == k) {
                    fade.setmProgress(1.0f);
                } else {
                    fade.setmProgress(0f);
                }
            }
        } else {
            FadeView left = (FadeView) getChildAt(position);
            FadeView right = (FadeView) getChildAt(position + 1);
            if (left != null) {
                left.setmDirection(FadeView.DIRECTION.RIGHT);
                left.setmProgress(1 - positionOffset);
            }
            if (right != null) {
                right.setmDirection(FadeView.DIRECTION.LEFT);
                right.setmProgress(positionOffset);
            }
        }
    }
}
