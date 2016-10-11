package joke.bright.com.train1011;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by chenxiaokang on 2016/10/11.
 */
public class RoundProgress extends HorizontalProgress {

    private int mRadius = dp2px(100);
    private int mMaxPaintWidth;

    public RoundProgress(Context context) {
        this(context, null);
    }

    public RoundProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        mUnreachHeight = dp2px(5);
        mReachHeight = (int) (mUnreachHeight*2.5f);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundProgress);
        mRadius = (int) ta.getDimension(R.styleable.RoundProgress_radius, mRadius);
        ta.recycle();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth = Math.max(mReachHeight, mUnreachHeight);
        int except = mRadius*2+mMaxPaintWidth+getPaddingLeft()+getPaddingRight();

        int width = resolveSize(except, widthMeasureSpec);
        int height = resolveSize(except, heightMeasureSpec);

        int realWidth = Math.min(width, height);
        setMeasuredDimension(realWidth, realWidth);

        mRadius = (realWidth-getPaddingLeft()-getPaddingRight()-mMaxPaintWidth)/2;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent()+mPaint.ascent())/2;

        canvas.save();
        canvas.translate(getPaddingLeft()+mMaxPaintWidth/2, getPaddingTop()+mMaxPaintWidth/2);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mUnreachColor);
        mPaint.setStrokeWidth(mUnreachHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        float sweepAngle = getProgress()*1.0f/getMax()*360;
        canvas.drawArc(new RectF(0,0,mRadius*2, mRadius*2), 0, sweepAngle, false, mPaint);

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, mRadius-textWidth/2, mRadius-textHeight, mPaint);

        canvas.restore();
    }
}
