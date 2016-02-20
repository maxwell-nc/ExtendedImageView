package pres.nc.maxwell.extendedimageview.cache;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;

import pres.nc.maxwell.extendedimageview.conn.ConnController;
import pres.nc.maxwell.extendedimageview.entity.WebImage;
import pres.nc.maxwell.extendedimageview.io.IOUtils;
import pres.nc.maxwell.extendedimageview.utils.ImageCompress;
import android.graphics.Bitmap;

/**
 * 三级缓存存取
 */
public class ImageCache {

	private WebImage webImage;

	public ImageCache(WebImage webImage) {
		this.webImage = webImage;
	}

	/**
	 * 下载网络图片到本地，自动加载到内存
	 * @return 图片
	 */
	public Bitmap dlWebImage() {

		try {

			HttpURLConnection conn = ConnController
					.getURLConnection(webImage.imageUrl);

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {// 成功接收

				InputStream inputStream = conn.getInputStream();

				// 下载的图片存到本地
				setDiskCache(inputStream);

				// 从本地缓存中读取（实际先读取到内存缓存中）
				return getDiskCache();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 设置本地缓存
	 * @param inputStream
	 */
	private void setDiskCache(InputStream inputStream) {
		IOUtils.writeStreamToFile(inputStream,
				IOUtils.getDiskCacheFile(webImage.md5Code, webImage.context));
	}

	/**
	 * 获取本地缓存，自动加载到内存
	 * @return 图片
	 */
	public Bitmap getDiskCache() {

		File cacheFile = IOUtils.getDiskCacheFile(webImage.md5Code,
				webImage.context);

		if (cacheFile.exists()) {// 本地缓存存在
			// 设置内存缓存
			setMemoryCache(cacheFile);

			// 从内存中返回
			return getMemoryCache();
		} else {
			return null;
		}

	}

	/**
	 * 设置内存缓存，自动压缩图片
	 * @param imageFile 图片文件
	 */
	private void setMemoryCache(File imageFile) {

		// 解析File对象
		Bitmap bitmapCache = ImageCompress.getImage(imageFile,
				webImage.viewHeight, webImage.viewWidth);

		if (bitmapCache != null) {
			// 加入内存缓存
			LruCacheDispatcher.getInstance().getMemoryCache()
					.put(webImage.md5Code, bitmapCache);
		}

	}

	/**
	 * 获取内存缓存
	 * @return 图片
	 */
	public Bitmap getMemoryCache() {

		Bitmap bitmapCache = LruCacheDispatcher.getInstance().getMemoryCache()
				.get(webImage.md5Code);
		return bitmapCache;

	}

}
