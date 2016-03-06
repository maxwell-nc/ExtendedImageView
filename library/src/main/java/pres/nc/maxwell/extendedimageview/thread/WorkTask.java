package pres.nc.maxwell.extendedimageview.thread;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import pres.nc.maxwell.extendedimageview.cache.CacheController;
import pres.nc.maxwell.extendedimageview.entity.WebImage;

/**
 * 工作任务，执行下载读取操作
 */
public class WorkTask implements Runnable {
	
	private WebImage webImage;
	private ResultHandler handler;

	/**
	 * 标记是否取消任务
	 */
	private boolean isCancelled = false;

	public WorkTask(WebImage webImage) {
		this.webImage = webImage;
	}

	public static class ResultHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			Bitmap bitmap = (Bitmap) msg.obj;
			onGetBitmap(bitmap);
		}

		/**
		 * 成功读取图片后的操作
		 * @param bitmap 图片
		 */
		public void onGetBitmap(Bitmap bitmap) {
		}

	}

	public void setResultHandler(ResultHandler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		Bitmap bitmap = CacheController.getImage(webImage);
		if (handler != null && !isCancelled) {
			Message msg = Message.obtain();
			msg.obj = bitmap;
			handler.sendMessage(msg);
		}
	}

	/**
	 * 取消任务
	 */
	public void cancel() {
		isCancelled = true;
	}
}
