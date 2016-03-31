package pres.nc.maxwell.extendedimageview.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import pres.nc.maxwell.extendedimageview.entity.WebImage;
import pres.nc.maxwell.extendedimageview.thread.WorkTask;
import pres.nc.maxwell.extendedimageview.thread.WorkTask.ResultHandler;
import pres.nc.maxwell.extendedimageview.utils.MD5Utils;

/**
 * 支持加载网络图片的ImageView
 */
public class WebImageView extends ExtentedImageView{

    //TODO： 添加支持本地图片

    private Context context;
    private WorkTask workTask;
    private Thread thread;
    private WebImage webImage;

    private int failedLoadResId;

    public WebImageView(Context context) {
        super(context);
        this.context = context;
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public WebImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public void setWebImage(String imageUrl) {
        setWebImage(imageUrl, -1, -1);
    }

    @Override
    public void setWebImage(final String imageUrl, final int loadingResId,
                            final int failedLoadResId) {

        this.failedLoadResId = failedLoadResId;

        //清空显示
        setImageBitmap(null);

        if (loadingResId != -1) {
            setImageResource(loadingResId);
        }

        if (webImage == null) {
            webImage = new WebImage(context);
        }

        webImage.imageUrl = imageUrl;
        webImage.md5Code = MD5Utils.getMD5String(imageUrl);

        if (workTask == null) { // 第一次则加载数据
            getSize();//等待获取控件size后自动启动任务
        } else {// 非第一次加载，取消上次的加载
            workTask.cancel();
            thread.interrupt();
            workTask = null;
            thread = null;
            startDownloadTask();
        }

    }

    /**
     * 启动下载图片任务并显示结果
     */
    private void startDownloadTask() {
        workTask = new WorkTask(webImage);
        thread = new Thread(workTask);

        workTask.setResultHandler(new ResultHandler() {

            @Override
            public void onGetBitmap(Bitmap bitmap) {
                if (bitmap != null) {
                    setImageBitmap(bitmap);
                } else {// 图片获取失败
                    if (failedLoadResId != -1) {
                        setImageResource(failedLoadResId);
                    }
                }
            }

        });

        thread.start();
    }

    /**
     * 获取控件大小
     */
    private void getSize() {
        addOnLayoutChangeListener(new OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight,
                                       int oldBottom) {
                webImage.viewHeight = getMeasuredHeight();
                webImage.viewWidth = getMeasuredWidth();

                startDownloadTask();

                // 防止多次调用
                removeOnLayoutChangeListener(this);
            }
        });
    }

}
