package pres.nc.maxwell.extendedimageview.utils;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 压缩bitmap工具类
 */
public class ImageCompress {

	/**
	 * 根据View宽高自动压缩图片
	 * 
	 * @param viewHeight
	 *            View的高度
	 * @param viewWidth
	 *            View的宽度
	 * @return Bitmap 压缩后的图片对象
	 */
	public static Bitmap getImage(File imageFile, int viewHeight, int viewWidth) {

		BitmapFactory.Options options = new BitmapFactory.Options();

		// 计算图片长宽
		options.inJustDecodeBounds = true;

		if (imageFile != null) {
			BitmapFactory.decodeFile(imageFile.getPath(), options);
		}

		int height = options.outHeight;
		int width = options.outWidth;

		// 默认不缩放
		int compressSampleSize = 1;

		if (height > 0 && width > 0) {
			// 根据长宽计算最佳采样大小
			if (height > viewHeight || width > viewWidth) {
				int heightRadio = height / viewHeight;
				int widthRadio = width / viewWidth;

				compressSampleSize = heightRadio > widthRadio ? heightRadio
						: widthRadio;
			}
		}

		options.inSampleSize = compressSampleSize;
		// options.inPreferredConfig = Bitmap.Config.RGB_565;

		// 真正解析Bitmap
		options.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), options);

		return bitmap;
	}

}
