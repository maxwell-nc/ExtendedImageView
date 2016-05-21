# 说明
ExtendedImageView是Android自带控件ImageView的扩展版，支持加载网络图片，拥有三级缓存图片管理，居中缩放拖动效果。
我建议看看：https://github.com/maxwell-nc/ImageLoader
以后这个带缓存的将会被分出为缓存Branch，一般不更新
主干将会去掉加载网络图片，主要提供ImageView图片扩展功能

##特点
- 支持HTTP和HTTPS地址的图片
- 支持三级缓存管理（网络、本地、内存）
- 图片压缩显示，减少内存使用
- 提供居中缩放拖动效果

##备注
- 待添加支持本地图片
- 修复失败时和加载中图片可以放大缩小的问题

##更新
2016年3月31日
- 改为链式调用
- 添加居中缩放拖动效果

# 快速使用

1.布局

若不需要缩放效果，可以使用pres.nc.maxwell.extendedimageview.ui.WebImageView
```xml
    <pres.nc.maxwell.extendedimageview.ui.ScaleImageView
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:id="@+id/siv_test"/>
```

2.代码
```java
        ImageViewExpander
                .extend()
                .activity(this)
                .viewId(R.id.siv_test)//控件id
                .url("https://avatars3.githubusercontent.com/u/14196813?v=3&s=460")//图片地址
                .loadingResId(R.mipmap.ic_launcher)//加载时显示的图片
                .failedLoadResId(R.mipmap.ic_launcher)//失败时显示的图片
                .minScale(0.5f)//最小缩放比例
                .maxScale(3f)//最大缩放比例
                .build()
                .execute();
```
