package pres.nc.maxwell.extendedimageview;

import pres.nc.maxwell.extendedimageview.entity.WebImage;
import pres.nc.maxwell.extendedimageview.thread.WorkTask;
import pres.nc.maxwell.extendedimageview.thread.WorkTask.ResultHandler;
import pres.nc.maxwell.extendedimageview.utils.MD5Utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * 扩展的ImageView
 */
public class ExImageView extends ImageView {

	//TODO： 添加支持本地图片
	//TODO： 考虑第一次ImageView有可能还没测量到宽高
	
	private Context context;
	private WorkTask workTask;
	private Thread thread;
	private WebImage webImage;

	public ExImageView(Context context) {
		super(context);
		this.context = context;
	}

	public ExImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public ExImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	/**
	 * 显示网络图片
	 * @param imageUrl 图片网址
	 */
	public void setWebImage(String imageUrl) {
		setWebImage(imageUrl, -1, -1);
	}

	/**
	 * 显示网络图片
	 * @param imageUrl 图片网址
	 * @param loadingResId 加载中的图像
	 * @param failedLoadResId 失败时的图像
	 */
	public void setWebImage(final String imageUrl, final int loadingResId,
			final int failedLoadResId) {

		//清空显示
		setImageBitmap(null);
		
		if (loadingResId != -1) {
			setImageResource(loadingResId);
		}

		// 第一次则加载数据
		if (webImage == null) {
			webImage = new WebImage(context);
			getSize();
		}

		webImage.imageUrl = imageUrl;
		webImage.md5Code = MD5Utils.getMD5String(imageUrl);

		// 取消上次的加载
		if (workTask != null) {
			workTask.cancel();
			thread.interrupt();
			workTask = null;
			thread = null;
		}

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

				int viewHeight = getMeasuredHeight();
				int viewWidth = getMeasuredWidth();

				webImage.viewHeight = viewHeight;
				webImage.viewWidth = viewWidth;

				// 防止多次调用
				removeOnLayoutChangeListener(this);
			}
		});
	}

}
