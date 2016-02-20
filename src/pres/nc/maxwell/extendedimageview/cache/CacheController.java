package pres.nc.maxwell.extendedimageview.cache;

import pres.nc.maxwell.extendedimageview.entity.WebImage;
import android.graphics.Bitmap;

/**
 * 缓存控制器
 */
public class CacheController {

	/**
	 * 从缓存里获取图片
	 * @param webImage 图片信息
	 * @return 内存中图片
	 */
	public static Bitmap getImage(WebImage webImage) {
		
		ImageCache imageCache = new ImageCache(webImage);
		Bitmap bitmap = imageCache.getMemoryCache();// 检查内存缓存

		if (bitmap == null) {// 检查本地缓存->自动加载到内存并读取
			bitmap = imageCache.getDiskCache();

			if (bitmap == null) {// 下载图片到本地->自动加载到内存并读取
				bitmap = imageCache.dlWebImage();
			}
		}

		return bitmap;
	}

}
