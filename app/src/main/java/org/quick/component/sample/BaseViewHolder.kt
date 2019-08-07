package org.quick.component.sample

import android.content.Context
import android.view.View
import android.widget.ImageView

class BaseViewHolder(itemView: View) : QuickViewHolder(itemView) {

    override fun bindImg(context: Context, url: String, imageView: ImageView?): QuickViewHolder {
        //在这里绑定普通图片
        return super.bindImg(context, url, imageView)
    }

    override fun bindImgRoundRect(context: Context, url: String, radius: Float, imageView: ImageView?): QuickViewHolder {
        //在这里绑定圆角图片
        return super.bindImgRoundRect(context, url, radius, imageView)
    }

    override fun bindImgCircle(context: Context, url: String, imageView: ImageView?): BaseViewHolder {
        //在这里绑定圆形图片
        return this
    }
}