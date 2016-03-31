package pres.nc.maxwell.extendedimageview.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 扩展的ImageView
 */
public abstract class ExtentedImageView extends ImageView{

    /**
     * 最大放大比例
     */
    protected float maxScale = 1f;

    /**
     * 最小缩小比例
     */
    protected float minScale = 1f;

    /**
     * 当前缩放比例
     */
    protected float currentScale = 1f;

    public ExtentedImageView(Context context) {
        super(context);
    }

    public ExtentedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtentedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置缩放最大比例
     * @param maxScale 最大比例
     */
    public void setMaxScale(float maxScale) {}

    /**
     * 设置缩放最小比例
     * @param minScale 最小比例
     */
    public void setMinScale(float minScale) {}

    /**
     * 显示网络图片
     *
     * @param imageUrl 图片网址
     */
    public abstract void setWebImage(String imageUrl);

    /**
     * 显示网络图片
     *
     * @param imageUrl        图片网址
     * @param loadingResId    加载中的图像
     * @param failedLoadResId 失败时的图像
     */
    public abstract void setWebImage(final String imageUrl, final int loadingResId,
                            final int failedLoadResId);
}
