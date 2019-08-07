package org.quick.viewHolder

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build

object Utils {

    fun decodeSampledBitmapFromResource(res: Resources, resId: Int): Bitmap {
        return decodeSampledBitmapFromResource(res, resId, 0, 0)
    }

    /**
     * 获取指定大小的位图
     *
     * @param res
     * @param resId
     * @param reqWidth  希望取得的宽度
     * @param reqHeight 希望取得的高度
     * @return 按指定大小压缩之后的图片
     * @source http://www.android-doc.com/training/displaying-bitmaps/load-bitmap
     * .html#read-bitmap
     */
    fun decodeSampledBitmapFromResource(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {

        if (reqWidth == 0 || reqHeight == 0) {
            return BitmapFactory.decodeResource(res, resId)
                ?: drawableToBitmap(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        res.getDrawable(resId, null)
                    else res.getDrawable(resId)
                )
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    /**
     * 计算与指定位图的大小比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return 缩放的比例
     * @source http://www.android-doc.com/training/displaying-bitmaps/load-bitmap.html #read-bitmap
     */
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            // 取高宽中更大一边，进行同比例缩放，这样才不会变形。
            inSampleSize = if (width > height) Math.round(height.toFloat() / reqHeight.toFloat()) else Math.round(width.toFloat() / reqWidth.toFloat())
        }
        return inSampleSize
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        // 取 drawable 的长宽
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight

        // 取 drawable 的颜色格式
        val config = if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        // 建立对应 bitmap
        val bitmap = Bitmap.createBitmap(w, h, config)
        // 建立对应 bitmap 的画布
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        // 把 drawable 内容画到画布中
        drawable.draw(canvas)
        return bitmap
    }
    /**
     * 裁剪为正圆形
     */
    fun cropCircle(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        val bitmapCorp: Bitmap

        val paint = Paint()
        paint.isAntiAlias = true

        val left: Int
        val top: Int
        val right: Int
        val bottom: Int

        val leftDst: Int
        val topDst: Int
        val rightDst: Int
        val bottomDst: Int

        val radii: Float/*半径*/

        if (width <= height) {
            radii = width / 2.0F
            left = 0
            top = (height - width) / 2
            right = width
            bottom = width

            leftDst = 0
            topDst = 0
            rightDst = width
            bottomDst = width
            height = width
        } else {
            radii = height / 2.0F
            left = Math.abs(Math.round((height - width) / 2.0F))
            top = 0
            right = width - Math.abs(Math.round((height - width) / 2.0F))
            bottom = height

            leftDst = Math.abs(Math.round((height - width) / 2.0F))
            topDst = 0
            rightDst = width - Math.abs(Math.round((height - width) / 2.0F))
            bottomDst = height
            width = height
        }

        val src = Rect(left, top, right, bottom)/*控制显示哪部分*/
        val dst = Rect(leftDst, topDst, rightDst, bottomDst)/*从哪展示的位置*/
        return try {
            bitmapCorp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmapCorp)
            canvas.drawARGB(0, 0, 0, 0)
            canvas.drawCircle(radii, radii, radii, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, src, dst, paint)
            bitmapCorp
        } catch (O_O: OutOfMemoryError) {
            O_O.printStackTrace()
            bitmap
        }

    }

    /**
     * 裁剪为带圆角矩形
     * @param radius 圆角
     */
    fun cropRoundRect(bitmap: Bitmap, radius: Float): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        val bitmapCorp: Bitmap

        val paint = Paint()
        paint.isAntiAlias = true

        val left = 0
        val top = 0
        val right = bitmap.width
        val bottom = bitmap.height

        val src = Rect(left, top, right, bottom)/*控制显示哪部分*/
        val dst = Rect(left, top, right, bottom)/*从哪展示的位置*/
        return try {
            bitmapCorp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmapCorp)
            canvas.drawARGB(0, 0, 0, 0)
            canvas.drawRoundRect(RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat()), radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, src, dst, paint)
            bitmapCorp
        } catch (O_O: OutOfMemoryError) {
            O_O.printStackTrace()
            bitmap
        }
    }
}