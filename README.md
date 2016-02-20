# 说明
ExtendedImageView是Android自带控件ImageView的扩展版，支持加载网络图片，拥有三级缓存图片管理。

##特点
- 支持HTTP和HTTPS地址的图片
- 支持三级缓存管理（网络、本地、内存）
- 图片压缩显示，减少内存使用

##备注
- 待添加支持本地图片
- 考虑第一次ImageView有可能还没测量到宽高

# 快速使用

1.布局

```xml
<pres.nc.maxwell.extendedimageview.ExImageView
        android:id="@+id/exiv_test"
        android:layout_width="match_parent"
        android:layout_height="100dp" />
```

2.代码
```java
ExImageView exImageView = (ExImageView) findViewById(R.id.exiv_test);

exImageView.setWebImage(图片地址);
exImageView.setWebImage(图片地址,加载中显示的图片,失败时显示的图片);
```