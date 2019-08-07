package org.quick.viewHolder

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NonNull

interface QuickVHService {
    fun <T : View> getView(@IdRes id: Int): T? 

    fun setText(@IdRes id: Int, content: CharSequence?, onClickListener: ((view: View, VHService: QuickVHService) -> Unit)? = null): QuickVHService

    /**
     * 原样本地图片
     *
     * @param id
     * @param iconId
     * @return
     */
    fun setImg(@IdRes id: Int, @DrawableRes iconId: Int, onClickListener: ((view: View, VHService: QuickVHService) -> Unit)? = null): QuickVHService

    /**
     * 原样网络图片
     *
     * @param id
     * @param url
     * @return
     */
    fun setImg(@IdRes id: Int, url: CharSequence, onClickListener: ((view: View, VHService: QuickVHService) -> Unit)? = null): QuickVHService


    /**
     * 圆角-本地图片
     *
     * @param id
     * @param radius
     * @param iconId
     * @return
     */
    fun setImgRoundRect(@IdRes id: Int, radius: Float, @DrawableRes iconId: Int, onClickListener: ((view: View, VHService: QuickVHService) -> Unit)? = null): QuickVHService

    /**
     * 圆角-网络图片
     *
     * @param id
     * @param radius
     * @param url
     * @return
     */
    fun setImgRoundRect(@IdRes id: Int, radius: Float, url: CharSequence, onClickListener: ((view: View, VHService: QuickVHService) -> Unit)? = null): QuickVHService


    /**
     * 圆形-网络图片
     *
     * @param id
     * @param url
     * @param onClickListener
     * @return
     */
    fun setImgCircle(@IdRes id: Int, url: CharSequence, onClickListener: ((view: View, VHService: QuickVHService) -> Unit)? = null): QuickVHService

    /**
     * 圆形-本地图片
     *
     */
    fun setImgCircle(@IdRes id: Int, @DrawableRes imgRes: Int, onClickListener: ((view: View, VHService: QuickVHService) -> Unit)? = null): QuickVHService


    fun bindImgCircle(context: Context, url: String, imageView: ImageView?): QuickVHService

    fun bindImg(context: Context, url: String, imageView: ImageView?): QuickVHService

    fun bindImgRoundRect(context: Context, url: String, radius: Float, imageView: ImageView?): QuickVHService

    fun setOnClickListener(onClickListener: (view: View, VHService: QuickVHService) -> Unit, @IdRes vararg ids: Int): QuickVHService

    fun setOnClickListener(onClickListener: (view: View, VHService: QuickVHService) -> Unit, @IdRes id: Int): QuickVHService

    fun setProgress(@IdRes id: Int, value: Int): QuickVHService

    fun setCheck(@IdRes id: Int, isChecked: Boolean): QuickVHService

    fun setBackgroundResource(@IdRes id: Int, bgResId: Int): QuickVHService

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setBackground(@IdRes id: Int, background: Drawable): QuickVHService

    fun setBackgroundColor(@IdRes id: Int, background: Int): QuickVHService

    fun setVisibility(visibility: Int, @NonNull @IdRes vararg resIds: Int): QuickVHService

    fun getTextView(@IdRes id: Int): TextView? 
    fun getButton(@IdRes id: Int): Button? 

    fun getImageView(@IdRes id: Int): ImageView? 

    fun getLinearLayout(@IdRes id: Int): LinearLayout?

    fun getRelativeLayout(@IdRes id: Int): RelativeLayout?

    fun getFramLayout(@IdRes id: Int): FrameLayout? 

    fun getCheckBox(@IdRes id: Int): CheckBox?

    fun getEditText(@IdRes id: Int): EditText?
}
