package com.zhengxyou.demo524;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhengxyou@163.com on 2018/5/24.
 */
public class BezierView extends View {
    private static final float C = 0.551915024494f;// 一个常量，用来计算绘制圆形贝塞尔曲线控制点的位置
    private float mCenterX, mCenterY;
    private Paint mPaint;
    private float mRadius = 200;
    private float mDifference = mRadius * C;

    private float[] mData = new float[8];//数据点
    private float[] mCtrl = new float[16];//控制点

    private float mDuration = 1000;
    private float mCurrent = 0;
    private float mCount = 100;
    private float mPiece = mDuration / mCount;

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        mData[0] = mRadius;
        mData[1] = 0;

        mData[2] = 0;
        mData[3] = mRadius;

        mData[4] = -mRadius;
        mData[5] = 0;

        mData[6] = 0;
        mData[7] = -mRadius;

        mCtrl[0] = mRadius;
        mCtrl[1] = mDifference;

        mCtrl[2] = mDifference;
        mCtrl[3] = mRadius;

        mCtrl[4] = -mDifference;
        mCtrl[5] = mRadius;

        mCtrl[6] = -mRadius;
        mCtrl[7] = mDifference;

        mCtrl[8] = -mRadius;
        mCtrl[9] = -mDifference;

        mCtrl[10] = -mDifference;
        mCtrl[11] = -mRadius;

        mCtrl[12] = mDifference;
        mCtrl[13] = -mRadius;

        mCtrl[14] = mRadius;
        mCtrl[15] = -mDifference;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(3);
        //画辅助坐标轴
        canvas.translate(mCenterX, mCenterY);
        canvas.drawLine(-mCenterX, 0, mCenterX, 0, mPaint);
        canvas.drawLine(0, -mCenterY, 0, mCenterY, mPaint);

        //画数据点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(16);
        for (int i = 0; i < 8; i += 2) {
            canvas.drawPoint(mData[i], mData[i + 1], mPaint);
        }

        //画控制点与线
        mPaint.setStrokeWidth(8);
        mPaint.setColor(Color.GREEN);

        for (int i = 0; i < 16; i += 2) {
            canvas.drawPoint(mCtrl[i], mCtrl[i + 1], mPaint);
        }
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(4);
        canvas.drawLine(mCtrl[0], mCtrl[1], mCtrl[14], mCtrl[15], mPaint);
        for (int i = 2; i < 14; i += 4) {
            canvas.drawLine(mCtrl[i], mCtrl[i + 1], mCtrl[i + 2], mCtrl[i + 3], mPaint);
        }

        //画圆
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
        Path path = new Path();
        path.moveTo(mData[0], mData[1]);
        path.cubicTo(mCtrl[0], mCtrl[1], mCtrl[2], mCtrl[3], mData[2], mData[3]);
        path.cubicTo(mCtrl[4], mCtrl[5], mCtrl[6], mCtrl[7], mData[4], mData[5]);
        path.cubicTo(mCtrl[8], mCtrl[9], mCtrl[10], mCtrl[11], mData[6], mData[7]);
        path.cubicTo(mCtrl[12], mCtrl[13], mCtrl[14], mCtrl[15], mData[0], mData[1]);
        canvas.drawPath(path, mPaint);

        //动画心形
        mCurrent += mPiece;
        if (mCurrent < mDuration) {
            mData[7] += 120 / mCount;

            mCtrl[2] -= 80 / mCount;
            mCtrl[4] += 80 / mCount;

            mCtrl[14] += 20 / mCount;
            mCtrl[0] -= 20 / mCount;

            mCtrl[6] += 20 / mCount;
            mCtrl[8] -= 20 / mCount;
            postInvalidateDelayed((long) mPiece);
        }
    }
}
