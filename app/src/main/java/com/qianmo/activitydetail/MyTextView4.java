package com.qianmo.activitydetail;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wangjitao on 2017/3/20 0020.
 * E-Mail：543441727@qq.com
 */

public class MyTextView4 extends View {
    private final static String TAG = "MyTextView";

    //文字
    private String mText;
    private ArrayList<String> mTextList;

    //文字的颜色
    private int mTextColor;

    //文字的大小
    private float mTextSize;

    //绘制的范围
    private Rect mBound;
    private Paint mPaint;

    public MyTextView4(Context context) {
        this(context, null);
    }

    public MyTextView4(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mTextList = new ArrayList<>();
        mPaint.getTextBounds(mText, 0, mText.length(), mBound);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制文字
        for (int i = 0; i < mTextList.size(); i++) {
            mPaint.getTextBounds(mTextList.get(i), 0, mTextList.get(i).length(), mBound);
            Log.v(TAG, "在X:" + (getWidth() / 2 - mBound.width() / 2) + "  Y:" + (getPaddingTop() + (mBound.height() * i)) + "  绘制：" + mTextList.get(i));
            canvas.drawText(mTextList.get(i), (getWidth() / 2 - mBound.width() / 2), (getPaddingTop() + (mBound.height() * (i + 1))), mPaint);
            Log.i(TAG, "getWidth() :" + getWidth() + ", mBound.width():" + mBound.width() + ",getHeight:" + getHeight() + ",mBound.height() *i:" + mBound.height() * i);
        }
    }

    boolean isOneLine = true;
    float lineNum;
    float splineNum;

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

        float textWidth = mBound.width();   //文本的宽度
        if (mTextList.size() == 0) {
            //将文本分段
            int padding = getPaddingLeft() + getPaddingRight();
            int specWidth = widthSize - padding; //剩余能够显示文本的最宽度
            if (textWidth <= specWidth) {
                //可以显示一行
                lineNum = 1;
                mTextList.add(mText);
            } else {
                //超过一行
                isOneLine = false;
                splineNum = textWidth / specWidth;
                //如果有小数的话则进1
                if ((splineNum + "").contains(".")) {
                    lineNum = (int) (splineNum + 0.5);
//                    lineNum = Integer.parseInt((splineNum + "").substring(0, (splineNum + "").indexOf("."))) + 1;
                } else {
                    lineNum = splineNum;
                }
                int lineLength = (int) (mText.length() / splineNum);
                for (int i = 0; i < lineNum; i++) {
                    String lineStr;
                    //判断是否可以一行展示
                    if (mText.length() < lineLength) {
                        lineStr = mText.substring(0, mText.length());
                    } else {
                        lineStr = mText.substring(0, lineLength);
                    }
                    mTextList.add(lineStr);
                    if (!TextUtils.isEmpty(mText)) {
                        if (mText.length() < lineLength) {
                            mText = mText.substring(0, mText.length());
                        } else {
                            mText = mText.substring(lineLength, mText.length());
                        }
                    } else {
                        break;
                    }
                }

            }
        }

        //下面对wrap_content这种模式进行处理
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            //如果是wrap_content，我们要得到控件需要多大的尺寸
            if (isOneLine) {
                //控件的宽度就是文本的宽度加上两边的内边距。内边距就是padding值，在构造方法执行完就被赋值
                width = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            } else {
                //如果是多行，说明控件宽度应该填充父窗体
                width = widthSize;
            }

        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            //如果是wrap_content，我们要得到控件需要多大的尺寸
            //首先丈量文本的宽度
            float textHeight = mBound.height();
            if (isOneLine) {
                //控件的宽度就是文本的宽度加上两边的内边距。内边距就是padding值，在构造方法执行完就被赋值
                height = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            } else {
                //如果是多行
                height = (int) (getPaddingTop() + textHeight * lineNum + getPaddingBottom());
            }
        }

        //保存丈量结果
        setMeasuredDimension(width, height);
    }
}
