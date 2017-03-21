package com.qianmo.activitydetail;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by wangjitao on 2017/3/20 0020.
 * E-Mail：543441727@qq.com
 */

public class MyTextView3 extends View {
    private final static String TAG = "MyTextView";

    //文字
    private String mText;

    //文字的颜色
    private int mTextColor;

    //文字的大小
    private float mTextSize;

    //绘制的范围
    private Rect mBound;
    private Paint mPaint;

    public MyTextView3(Context context) {
        this(context, null);
    }

    public MyTextView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化数据
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取自定义属性的值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTextView2, defStyleAttr, 0);
        mText = a.getString(R.styleable.MyTextView2_myText);
        mTextColor = a.getColor(R.styleable.MyTextView2_myTextColor, Color.BLACK);
        mTextSize = a.getDimension(R.styleable.MyTextView2_myTextSize, 30f);
        a.recycle();

        //初始化Paint数据
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);

        //获取绘制的宽高
        mBound = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mBound);
        Log.i(TAG, "Left :" + mBound.left + ",Right:" + mBound.right + ",Top:" + mBound.top + ",Bottom:" + mBound.bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制文字
        canvas.drawText(mText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
        //注意一下我们这里的getWidth()和getHeight()是获取的px
        Log.i(TAG, "getWidth():" + getWidth() + ",getHeight(): " + getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec); //获取宽的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); // 获取高的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec); //获取宽的尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高的尺寸

        Log.i(TAG, "widthMode:" + widthMode);
        Log.i(TAG, "heightMode:" + heightMode);
        Log.i(TAG, "widthSize:" + widthSize);
        Log.i(TAG, "heightSize:" + heightSize);

        //下面对wrap_content这种模式进行处理
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            //如果是wrap_content，我们要得到控件需要多大的尺寸
            //首先丈量文本的宽度
            float textWidth = mBound.width();
            //控件的宽度就是文本的宽度加上两边的内边距。内边距就是padding值，在构造方法执行完就被赋值
            width = (int) (getPaddingLeft() + textWidth + getPaddingRight());
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = widthSize;
        } else {
            //如果是wrap_content，我们要得到控件需要多大的尺寸
            //首先丈量文本的宽度
            float textHeight = mBound.height();
            //控件的宽度就是文本的宽度加上两边的内边距。内边距就是padding值，在构造方法执行完就被赋值
            height = (int) (getPaddingTop() + textHeight + getPaddingBottom());
        }

        //保存丈量结果
        setMeasuredDimension(width, height);
    }
}
