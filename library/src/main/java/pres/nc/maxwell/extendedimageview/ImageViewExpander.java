package pres.nc.maxwell.extendedimageview;


import android.app.Activity;

import pres.nc.maxwell.extendedimageview.ui.ExtentedImageView;

/**
 * ImageView扩展器
 */
public class ImageViewExpander {

    /**
     * 构建器
     */
    private Builder mBuilder;

    private ImageViewExpander(Builder builder) {
        this.mBuilder = builder;
    }

    /**
     * 扩展
     * @return
     */
    public static Builder extend() {
        return new Builder();
    }

    /**
     * 执行扩展
     */
    public void execute() {

        if (mBuilder.viewId == -1) {
            return;
        }

        ExtentedImageView imageView = (ExtentedImageView) mBuilder.activity.findViewById(mBuilder.viewId);
        imageView.setMinScale(mBuilder.minScale);
        imageView.setMaxScale(mBuilder.maxScale);

        if (mBuilder.loadingResId == -1 || mBuilder.failedLoadResId == -1) {
            imageView.setWebImage(mBuilder.imgUrl);
        } else {
            imageView.setWebImage(mBuilder.imgUrl, mBuilder.loadingResId, mBuilder.failedLoadResId);
        }

    }

    /**
     * 构建器
     */
    public static class Builder {

        Activity activity;
        int viewId = -1;
        int loadingResId = -1;
        int failedLoadResId = -1;
        String imgUrl;
        float maxScale;
        float minScale;

        /**
         * 设置ImageView所在的Activity
         */
        public Builder activity(Activity activity) {
            this.activity = activity;
            return this;
        }

        /**
         * 设置要显示的图片地址
         */
        public Builder url(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        /**
         * 设置ImageView的布局中的id
         */
        public Builder viewId(int viewId) {
            this.viewId = viewId;
            return this;
        }

        /**
         * 设置加载图片时显示的资源id
         */
        public Builder loadingResId(int loadingResId) {
            this.loadingResId = loadingResId;
            return this;
        }

        /**
         * 设置加载图片失败时显示的资源id
         */
        public Builder failedLoadResId(int failedLoadResId) {
            this.failedLoadResId = failedLoadResId;
            return this;
        }

        /**
         * 设置最大支持缩放比例
         */
        public Builder maxScale(float maxScale) {
            this.maxScale = maxScale;
            return this;
        }

        /**
         * 设置最小支持缩放比例
         */
        public Builder minScale(float minScale) {
            this.minScale = minScale;
            return this;
        }

        /**
         * 构建
         * @return 扩展器
         */
        public ImageViewExpander build() {
            return new ImageViewExpander(this);
        }
    }

}
