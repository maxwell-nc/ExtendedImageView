package pres.nc.maxwell.extendedimageview.entity;

import android.content.Context;

/**
 * 网络图片信息
 */
public class WebImage {

	/**
	 * 上下文
	 */
	public Context context;
	
	/**
	 * 网络图片地址
	 */
	public String imageUrl;
	
	/**
	 * 网络图片网址的MD5码
	 */
	public String md5Code;
	
	/**
	 * 控件高度
	 */
	public int viewHeight;
	
	/**
	 * 控件宽度
	 */
	public int viewWidth;

	public WebImage(Context context) {
		this.context = context;
	}
}
