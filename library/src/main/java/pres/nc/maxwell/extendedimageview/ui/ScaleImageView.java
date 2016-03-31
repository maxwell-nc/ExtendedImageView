package pres.nc.maxwell.extendedimageview.ui;


import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * 支持图片手势放大缩小的ImageView
 */
public class ScaleImageView extends WebImageView{

    private static final int STATE_NORMAL = 1;
    private static final int STATE_DRAG = 2;
    private static final int STATE_ZOOM = 3;

    /**
     * 当前状态
     *
     * @see #STATE_NORMAL
     * @see #STATE_DRAG
     * @see #STATE_ZOOM
     */
    private int currentState;

    private Matrix matrix = new Matrix();
    private PointF last = new PointF();
    private float[] finalTransformation = new float[9];

    private int viewWidth;
    private int viewHeight;
    private float afterScaleWidth;
    private float afterScaleHeight;

    private ScaleGestureDetector scaleDetector;
    private GestureDetector doubleTapDetector;

    public ScaleImageView(Context context) {
        super(context);
        init();
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }

    @Override
    public void setMinScale(float minScale) {
        this.minScale = minScale;
    }


    /**
     * 初始化ImageView
     */
    private void init() {
        super.setClickable(false);
        setScaleType(ScaleType.MATRIX);
        currentState = STATE_NORMAL;

        //双击检测器
        doubleTapDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                //双击切换放大和还原
                if (currentScale != 1f) {
                    centerScale();
                    currentScale = 1f;
                    currentState = STATE_NORMAL;
                } else {
                    scale(e.getX(), e.getY(), maxScale);
                }
                return true;
            }

        });

        //缩放检测器
        scaleDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                currentState = STATE_ZOOM;
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scale(detector.getFocusX(), detector.getFocusY(), detector.getScaleFactor());
                return true;
            }

        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        centerScale();
    }

    /**
     * 居中缩放显示
     */
    private void centerScale() {
        //判断是否有图片显示
        if (getDrawable() != null) {

            int picWidth = getDrawable().getIntrinsicWidth();
            int picHeight = getDrawable().getIntrinsicHeight();

            if (picWidth == 0 || picHeight == 0) {
                return;
            }

            // 缩放图片
            float scaleX = (float) viewWidth / (float) picWidth;
            float scaleY = (float) viewHeight / (float) picHeight;
            float scale = Math.min(scaleX, scaleY);//适应屏幕，显示全部图片
            matrix.setScale(scale, scale);

            // 居中图片
            float centerX = ((float) viewWidth - (scale * picWidth)) / 2;
            float centerY = ((float) viewHeight - (scale * picHeight)) / 2;
            matrix.postTranslate(centerX, centerY);

            afterScaleWidth = scale * picWidth;
            afterScaleHeight = scale * picHeight;

            setImageMatrix(matrix);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        doubleTapDetector.onTouchEvent(event);
        PointF current = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last.set(current);
                currentState = STATE_DRAG;
                break;

            case MotionEvent.ACTION_MOVE:
                if (currentState == STATE_DRAG) {
                    drag(current);
                    last.set(current);
                }
                break;

            case MotionEvent.ACTION_UP:
                currentState = STATE_NORMAL;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                currentState = STATE_NORMAL;
                break;
        }

        setImageMatrix(matrix);
        invalidate();
        return true;
    }


    /**
     * 拖放
     *
     * @param current 当前坐标
     */
    private void drag(PointF current) {
        float deltaX = getMoveDraggingDelta(current.x - last.x, viewWidth, afterScaleWidth * currentScale);
        float deltaY = getMoveDraggingDelta(current.y - last.y, viewHeight, afterScaleHeight * currentScale);
        matrix.postTranslate(deltaX, deltaY);
        limitDrag();
    }

    /**
     * 缩放方法
     */
    private void scale(float focusX, float focusY, float scaleFactor) {
        float lastScale = currentScale;
        float newScale = lastScale * scaleFactor;

        // 检查是否超出缩放限制比例，计算出新的比例
        if (newScale > maxScale) {
            currentScale = maxScale;
            scaleFactor = maxScale / lastScale;
        } else if (newScale < minScale) {
            currentScale = minScale;
            scaleFactor = minScale / lastScale;
        } else {
            currentScale = newScale;
        }

        // 缩放
        if (afterScaleWidth * currentScale <= (float) viewWidth ||
                afterScaleHeight * currentScale <= (float) viewHeight) {//判断是否需要居中缩放
            matrix.postScale(scaleFactor, scaleFactor, (float) viewWidth / 2, (float) viewHeight / 2);
        } else
            matrix.postScale(scaleFactor, scaleFactor, focusX, focusY);

        limitDrag();
    }

    /**
     * 限制拖动超出屏幕
     */
    private void limitDrag() {
        matrix.getValues(finalTransformation);
        float finalXTransformation = finalTransformation[Matrix.MTRANS_X];
        float finalYTransformation = finalTransformation[Matrix.MTRANS_Y];

        float deltaX = getScaleDraggingDelta(finalXTransformation, viewWidth, afterScaleWidth * currentScale);
        float deltaY = getScaleDraggingDelta(finalYTransformation, viewHeight, afterScaleHeight * currentScale);

        matrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 获得缩放拖动距离
     */
    private float getScaleDraggingDelta(float delta, float viewSize, float contentSize) {
        float minTrans = 0;
        float maxTrans = 0;

        if (contentSize <= viewSize) {
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
        }

        if (delta < minTrans)
            return minTrans - delta;
        else if (delta > maxTrans)
            return maxTrans - delta;
        else
            return 0;
    }

    /**
     * 检测拖动是否可用，如果可以返回距离，否则返回0
     */
    private float getMoveDraggingDelta(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

}

