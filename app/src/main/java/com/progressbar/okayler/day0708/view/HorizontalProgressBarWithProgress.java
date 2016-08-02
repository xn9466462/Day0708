package com.progressbar.okayler.day0708.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.progressbar.okayler.day0708.R;

/**
 * Created by Okayler on 2016/7/8.
 */
public class HorizontalProgressBarWithProgress extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_TEXT_OFFSET= 10;//dp
    private static final int DEFAULT_COLOR_UNREACH = 0xFFD3D6DA;
    private static final int DEFAULT_UNREACH_HEIGHT = 2;//dp
    private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_REACH_HEIGHT = 2;//dp


    private int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);
    private int mUnReachColor= DEFAULT_COLOR_UNREACH;
    private int mReachColor= DEFAULT_COLOR_REACH;
    private int mUnReachHeight= DEFAULT_UNREACH_HEIGHT;
    private int mReachHeight= DEFAULT_REACH_HEIGHT;

    private Paint mPaint = new Paint();
    private int mRealWidth;


    public HorizontalProgressBarWithProgress(Context context) {
        super(context, null, 0);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(attrs);

    }

    /**
     * 获取自定义属性
     * @param attrs
     */
    private void obtainStyledAttrs(AttributeSet attrs){
        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.HorizontalProgressBarWithProgress);
        mTextSize = (int)ta.getDimension(
                R.styleable.HorizontalProgressBarWithProgress_progrecess_text_size,
                mTextSize);
        mTextOffset= (int)ta.getDimension(
                R.styleable.HorizontalProgressBarWithProgress_progrecess_text_offset,
                mTextOffset);
        mTextColor = ta.getColor(
                R.styleable.HorizontalProgressBarWithProgress_progrecess_text_color,
                mTextColor);
        mUnReachColor = ta.getColor(
                R.styleable.HorizontalProgressBarWithProgress_progrecess_unreach_color,
                mUnReachColor);
        mReachColor = ta.getColor(
                R.styleable.HorizontalProgressBarWithProgress_progrecess_reach_color,
                mReachColor);

        mUnReachHeight = (int)ta.getDimension(
                R.styleable.HorizontalProgressBarWithProgress_progrecess_unreach_height,
                mUnReachHeight);
        mReachHeight = (int)ta.getDimension(
                R.styleable.HorizontalProgressBarWithProgress_progrecess_reach_height,
                mReachHeight);

        ta.recycle();
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthVal, height);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

    }

    private int measureHeight(int heightMeasureSpec){

        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY){
            result = size;
        }else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop() + getPaddingBottom()+Math.max(Math.max(mReachHeight, mUnReachHeight), Math.abs(textHeight));

            if (mode == MeasureSpec.AT_MOST){
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight()/2);
        boolean noNeedUnrech = false;
        String text = getProgress()+"%";
        int textWidth = (int)mPaint.measureText(text);
        float radio = getProgress()*1.0f/getMax();
        float progressX = radio*mRealWidth;
        if (progressX + textWidth >mRealWidth){
            progressX = mRealWidth - textWidth;
            noNeedUnrech = true;
        }

        float endX = radio*mRealWidth - mTextOffset/2;
        if (endX > 0){
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX,0, mPaint);
        }

        //draw text
        mPaint.setColor(mTextColor);
        int y = (int)(-(mPaint.descent() + mPaint.ascent())/2);
        canvas.drawText(text, progressX, y, mPaint);

        //draw unreach bar
        if (!noNeedUnrech){
            float start = progressX + mTextOffset /2 + textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }
        canvas.restore();
    }

    private int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }


}
