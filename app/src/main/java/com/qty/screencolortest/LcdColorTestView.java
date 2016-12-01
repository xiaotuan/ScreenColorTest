package com.qty.screencolortest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class LcdColorTestView extends View {

    private static final String TAG = "LcdTestView";

    private static final int BLACK_TEST = 0;
    private static final int WHITE_TEST = 1;
    private static final int RED_TEST = 2;
    private static final int GREEN_TEST = 3;
    private static final int BLUE_TEST = 4;
    private static final int TRICOLOR_TEST = 5;
    private static final int WHITE_AND_BLACK_TEST = 6;

    private Paint mPaint;
    private Paint mWhitePaint;
    private Paint mBlackPaint;
    private Resources mResources;
    private OnStateChangedListener mListener;
    private ArrayList<RectItem> mRects;

    private int mBlackColor;
    private int mWhiteColor;
    private int mRedColor;
    private int mGreenColor;
    private int mBlueColor;
    private int mTextColor;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mRectWidth;
    private int mRectHeight;
    private int mTextSize;
    private int mProgress;

    public LcdColorTestView(Context context) {
        this(context, null);
    }

    public LcdColorTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LcdColorTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mResources = getResources();
        mRectWidth = mResources.getInteger(R.integer.lcd_test_rect_width);
        mRectHeight = mResources.getInteger(R.integer.lcd_test_rect_height);
        mTextSize = mResources.getDimensionPixelSize(R.dimen.lcd_test_text_size);
        mBlackColor = mResources.getColor(R.color.black);
        mWhiteColor = mResources.getColor(R.color.white);
        mRedColor = mResources.getColor(R.color.red);
        mGreenColor = mResources.getColor(R.color.green);
        mBlueColor = mResources.getColor(R.color.blue);
        mTextColor = mResources.getColor(R.color.tip_text_color);
        mProgress = 0;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.FILL);
        mWhitePaint = new Paint(mPaint);
        mWhitePaint.setColor(mWhiteColor);
        mBlackPaint = new Paint(mPaint);
        mBlackPaint.setColor(mBlackColor);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 获取屏幕尺寸信息
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm  = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        Log.d(TAG, "onFinishInflate=>screenWidth: " + mScreenWidth + " screenHeight: " + mScreenHeight);
        Log.d(TAG, "onFinishInflate=>width: " + getWidth() + " height: " + getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure=>width: " + width + " height: " + height);
        // 设置view的尺寸为最大尺寸
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化矩形坐标
        initRects();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mProgress) {
            case BLACK_TEST:
                mPaint.setColor(mBlackColor);
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;

            case WHITE_TEST:
                mPaint.setColor(mWhiteColor);
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;

            case RED_TEST:
                mPaint.setColor(mRedColor);
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;

            case GREEN_TEST:
                mPaint.setColor(mGreenColor);
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;

            case BLUE_TEST:
                mPaint.setColor(mBlueColor);
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;

            case TRICOLOR_TEST:
                // 将屏幕切分为三等分，分别使用红色，绿色和蓝色填充矩形区域
                mPaint.setColor(mRedColor);
                int firstHeight = getHeight() / 3;
                canvas.drawRect(0, 0, getWidth(), firstHeight, mPaint);
                mPaint.setColor(mGreenColor);
                canvas.drawRect(0, firstHeight, getWidth(), firstHeight * 2, mPaint);
                mPaint.setColor(mBlueColor);
                canvas.drawRect(0, firstHeight * 2, getWidth(), getHeight(), mPaint);
                break;

            case WHITE_AND_BLACK_TEST:
                int lastRow = 0;
                RectItem item;
                // position 记录每个矩形在所在行中的位置
                for (int i = 0, position = 0; i < mRects.size(); i++, position++) {
                    item = mRects.get(i);
                    // 如果是不同行数，则重置位置为0
                    if (item.row != lastRow) {
                        lastRow = item.row;
                        position = 0;
                    }
                    Paint paint = getPaint(position, item);
                    canvas.drawRect(item.rect, paint);
                }
                break;
        }
        // 在屏幕居中绘画提示信息
        String testTip = mResources.getString(R.string.lcd_test_tip);
        String completedTip = mResources.getString(R.string.lcd_test_completed_tip);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        if (mProgress < WHITE_AND_BLACK_TEST) {
            canvas.drawText(testTip, getWidth() / 2 - mPaint.measureText(testTip) / 2, getHeight() / 2 - (fm.bottom - fm.top) / 2, mPaint);
        } else {
            canvas.drawText(completedTip, getWidth() / 2 - mPaint.measureText(completedTip) / 2, getHeight() / 2 - (fm.descent - fm.ascent) / 2, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                // 手指抬起后切换测试类型
                if (mProgress < WHITE_AND_BLACK_TEST) {
                    mProgress++;
                    if (mListener != null) {
                        mListener.onTestProgressChanged(mProgress);
                        if (mProgress >= WHITE_AND_BLACK_TEST) {
                            mListener.onTestCompleted(true);
                        }
                    }
                    postInvalidate();
                }
                break;
        }
        return true;
    }

    public int getProgress() {
        return mProgress;
    }

    /**
     * 初始化矩形方块坐标
     * 首先根据设定的矩形长度和高度获取每行矩形
     * 每获取一个矩形，长度加上单个矩形的长度
     * 获取完一行后，高度加上单个矩形的高度
     */
    public void initRects() {
        int width = getWidth();
        int height = getHeight();
        mRects = new ArrayList<RectItem>();
        if (width != 0 && height != 0) {
            for (int row = 0, i = 0; i <= height; i += mRectHeight, row++) {
                for (int j = 0; j <= width; j += mRectWidth) {
                    RectItem item = new RectItem(j, i, j + mRectWidth, i + mRectHeight, row);
                    mRects.add(item);
                }
            }
        }
    }

    /**
     * 获取矩形方块的画笔，比如：
     * 通过对矩形所在的行数{@RectItem$row}进行求余获取每行第一个矩形的画笔
     * 通过对位置{@position}进行求余获取下一个矩形的画笔
     *
     * @param position 在一行中的位置
     * @param item 矩形信息
     * @return 画笔
     */
    private Paint getPaint(int position, RectItem item) {
        // 初始化第一行第一个矩形的画笔startPaint
        Paint startPaint = mBlackPaint;
        Paint secontPaint = mWhitePaint;
        if (((item.row + 1) % 2) == 0) {
            startPaint = mWhitePaint;
            secontPaint = mBlackPaint;
        }
        // 在一行中交替变换画笔
        Paint paint = startPaint;
        if (((position + 1) % 2) == 0) {
            paint = secontPaint;
        }
        return paint;
    }

    /**
     * 设置监听器
     * @param listener 监听器
     */
    public void setOnStateChangedListener(OnStateChangedListener listener) {
        mListener = listener;
        if (mListener != null) {
            mListener.onTestProgressChanged(mProgress);
        }
    }

    public interface OnStateChangedListener {
        void onTestProgressChanged(int progress);
        void onTestCompleted(boolean result);
    }

    /**
     * 添加所在行信息，方便交替绘制黑白方块时选择画笔颜色
     */
    class RectItem {
        // 矩形信息
        Rect rect;
        // 所在的行数
        int row;

        public RectItem(int left, int top, int right, int bottom, int row) {
            this.rect = new Rect(left, top, right, bottom);
            this.row = row;
        }

        @Override
        public String toString() {
            return "row: " + row + " [" + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + "]";
        }
    }
}
