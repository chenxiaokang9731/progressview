package joke.bright.com.train1011;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by chenxiaokang on 2016/10/11.
 */
public class HorizontalProgress extends ProgressBar{

    private static final int DEFAULT_TEXT_SIZE = 15;
    private static final int DEFAULT_TEXT_COLOR = 0xffaaaaaa;
    private static final int DEFAULT_COLOR_UNREACH = 0xffaaaaaa;
    private static final int DEFAULT_HEIGHT_UNREACH = 2;
    private static final int DEFAULT_COLOR_REACH = 0xffaaaaaa;
    private static final int DEFAULT_HEIGHT_REACH = 2;
    private static final int DEFAULT_TEXT_OFFSET = 10;

    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mUnreachHeight = dp2px(DEFAULT_HEIGHT_UNREACH);
    protected int mUnreachColor = DEFAULT_COLOR_UNREACH;
    protected int mReachHeight = dp2px(DEFAULT_HEIGHT_REACH);
    protected int mReachColor = DEFAULT_COLOR_REACH;
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    private int mRealWidth;

    protected Paint mPaint = new Paint();

    public HorizontalProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyleAttrs(attrs);

        mPaint.setTextSize(mTextSize);
    }

    private void obtainStyleAttrs(AttributeSet attrs) {

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgress);

        mTextSize = (int) ta.getDimension(R.styleable.HorizontalProgress_progress_text_size, mTextSize);
        mTextColor = ta.getColor(R.styleable.HorizontalProgress_progress_text_color, mTextColor);
        mTextOffset = (int) ta.getDimension(R.styleable.HorizontalProgress_progress_text_offset, mTextOffset);
        mReachColor = ta.getColor(R.styleable.HorizontalProgress_progress_reach_color, mReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgress_progress_reach_height, mReachHeight);
        mUnreachColor = ta.getColor(R.styleable.HorizontalProgress_progress_unreach_color, mReachColor);
        mUnreachHeight = (int) ta.getDimension(R.styleable.HorizontalProgress_progress_unreach_height, mUnreachHeight);

        ta.recycle();

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal, height);  //测量

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int measureHeight(int heightMeasureSpec) {

        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if(mode == MeasureSpec.EXACTLY){
            result = size;
        }else {
            int textHeight = (int) (mPaint.descent()-mPaint.ascent());
            result = getPaddingTop()+getPaddingBottom()+Math.max(Math.max(mUnreachHeight, mReachHeight), textHeight);
            if(mode == MeasureSpec.AT_MOST){
                result = Math.min(result, size);
            }
        }

        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight()/2);

        boolean NeedUnreach = true;
        float radio = getProgress() * 1.0f / getMax();

        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        float progressX = radio*mRealWidth-textWidth;
        if(progressX+textWidth>mRealWidth){
            progressX = mRealWidth-textWidth;
            NeedUnreach = false;
        }

        float endX = progressX-mTextOffset;
        if(endX > 0){
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }

        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent())/2);
        canvas.drawText(text, progressX, y, mPaint);

        if(NeedUnreach){
            float start = progressX+textWidth+mTextOffset;
            mPaint.setColor(mUnreachColor);
            mPaint.setStrokeWidth(mUnreachHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }

        canvas.restore();
    }



    protected int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }
}
