package com.cisetech.customer.customer.Animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cisetech.customer.customer.R;

/**
 * Author:Yqy
 * Date:2016-08-08
 * Desc:倒计时View
 * Company:cisetech
 */
public class CountDownView extends ProgressBar {
    /**
     * 结束的位置
     */
    private int endAngle=270;
    private Paint mTextPaint;
    private Paint mBgPaint;
    /**
     * 绘画的半径
     */
    private int mRadius=dp2px(30);
    /**
     * 进度条color默认红色
     */
    private int mProgressColor = Color.RED;
    /**
     * 进度条宽度，默认2dp
     */
    private int mProgressWidth = dp2px(2);
    /**
     * 文本
     */
    private String mText="跳过";
    /**
     * 文本颜色
     */
    private int mTextColor=Color.WHITE;
    /**
     * 文本大小，默认14.5sp
     */
    private int mTextSize=sp2px(14.5f);
    /**
     * 默认中心背景色
     */
    private int centerBg=Color.GRAY;
    /**
     * 当wrapcontent的时候
     * 控件的宽高为文字的宽度加上offset
     */
    private float offset=15;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyleAttr(context, attrs, defStyleAttr);
        ////////////////测试
        setMax(100);
        startAnim();
    }

    private void startAnim() {
        ValueAnimator a=ValueAnimator.ofInt(0, getMax() + 1);
        a.setDuration(5000);
        a.setInterpolator(new LinearInterpolator());
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                setProgress(value);
                postInvalidate();
            }
        });
        a.start();
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Toast.makeText(getContext(), "完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 获取参数
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void obtainStyleAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.count_down, defStyleAttr, 0);
        int count=a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr=a.getIndex(i);
            switch (attr){
                case R.styleable.count_down_center_bg:
                    centerBg=a.getColor(attr,centerBg);
                    break;
                case R.styleable.count_down_text:
                    mText=a.getString(attr);
                    break;
                case R.styleable.count_down_progress_color:
                    mProgressColor=a.getColor(attr,mProgressColor);
                    break;
                case R.styleable.count_down_text_color:
                    mTextColor=a.getColor(attr,mTextColor);
                    break;
                case R.styleable.count_down_text_size:
                    mTextSize=a.getDimensionPixelSize(attr,mTextSize);
                    break;
                case R.styleable.count_down_progress_width:
                    mProgressWidth=a.getDimensionPixelSize(attr,mProgressWidth);
                    break;
            }
        }
        a.recycle();
        mTextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);


        mBgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setDither(true);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeCap(Paint.Cap.ROUND);
        mBgPaint.setStrokeJoin(Paint.Join.ROUND);
        mBgPaint.setStrokeWidth(mProgressWidth);
        mBgPaint.setColor(mProgressColor);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 当为wrap_content的时候，返回的大小为 （文本的长度+红色进度条的宽度*2+一个自定义的offset）
         */
        int result= (int) (getPaddingLeft()+getPaddingRight()+mTextPaint.measureText(mText)+mProgressWidth*2)+dp2px(offset);
        /**
         * 传入一个result，当widthMode为MeasureSpec.UNSPECIFIED的时候返回result，
         * 否则为设定的值
         */
        int width=resolveSize(result, widthMeasureSpec);
        int height=resolveSize(result,heightMeasureSpec);
        result=Math.min(width,height);
        setMeasuredDimension(result, result);
        mRadius=getMeasuredWidth()/2;

    }
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //画中间的圈
        mTextPaint.setColor(centerBg);
        canvas.drawCircle(mRadius, mRadius, mRadius-mProgressWidth/2, mTextPaint);
        //绘制文本
        mTextPaint.setColor(mTextColor);
        int baseX= (int) (mRadius-mTextPaint.measureText(mText)/2);
        int baseY= (int) (mRadius+(mTextPaint.getFontMetrics().bottom-mTextPaint.getFontMetrics().top)/2
                        -mTextPaint.getFontMetrics().bottom);
        canvas.drawText(mText, baseX, baseY, mTextPaint);
        /**
         * 绘制进度条，
         * 比如从startAngle从-90度的位置开始，endAngle为270位置（y的负轴位置）
         * 需要绘制的角度为
         * 270--90（开始）=360；
         * 270-0（开始）=270
         * 270-90（开始）=180
         * 所以sweepAngle=startAngle-270(endAngle)
         */
        int startAngle= (int) ((getProgress()*1.0f/getMax())*360)-90;
        canvas.drawArc(new RectF(0+mProgressWidth/2, 0+mProgressWidth/2,mRadius*2-mProgressWidth/2, mRadius*2-mProgressWidth/2), startAngle, endAngle-startAngle, false, mBgPaint);
    }
    /**
     * dp2px
     * -90 360  0 270 90 180
     * @param value
     * @return px
     */
    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    /**
     * sp2px
     *
     * @param value
     * @return px
     */
    private int sp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, getResources().getDisplayMetrics());
    }
}
