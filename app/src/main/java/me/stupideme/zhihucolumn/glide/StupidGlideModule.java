package me.stupideme.zhihucolumn.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by StupidL on 2016/11/12.
 */

public class StupidGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //get system max mem
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //set mem cache size
        int memoryCacheSize = maxMemory / 8;
        //set mem cache
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));

        //disk cache size 30MB
        int diskCacheSize = 1024 * 1024 * 30;
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "glide", diskCacheSize));

        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
