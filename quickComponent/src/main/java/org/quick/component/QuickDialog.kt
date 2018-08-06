package org.quick.component

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager

/**
 * @describe 快速使用自定义Dialog
 * @author ChrisZou
 * @date 2018/7/9-9:36
 * @from https://github.com/SpringSmell/quick.library
 * @email chrisSpringSmell@gmail.com
 */
open class QuickDialog private constructor() {

    private var builder: Builder? = null
    private var dialog: Dialog? = null
    private var viewHolder: QuickViewHolder? = null

    fun setupQuickDialog(builder: Builder): QuickDialog {
        this.builder = builder
        return this
    }

    fun viewHolder(): QuickViewHolder {
        if (viewHolder == null || viewHolder!!.itemView != builder?.layoutView)
            viewHolder = QuickViewHolder(builder?.layoutView!!)
        return viewHolder!!
    }

    fun getDialog(): Dialog? {
        if (builder != null && (dialog == null || viewHolder == null || viewHolder?.itemView != builder!!.layoutView || builder!!.isRewrite)) {
            dialog = Dialog(builder?.context, builder!!.style)
            dialog?.setContentView(viewHolder().itemView)
            dialog?.window?.setGravity(builder!!.gravity)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.setCanceledOnTouchOutside(builder!!.canceledOnTouchOutside)
            dialog?.window?.setLayout(builder!!.width, builder!!.height)
            dialog?.window?.decorView?.setPadding(builder!!.paddingLeft, builder!!.paddingTop, builder!!.paddingRight, builder!!.paddingBottom)
            dialog?.setCanceledOnTouchOutside(builder!!.canceledOnTouchOutside)
        }
        return dialog
    }

    fun dimiss() {
        dialog?.dismiss()
    }

    fun show(): QuickViewHolder {
        getDialog()?.show()
        return viewHolder()
    }

    fun resetInternal() {
        builder = null
        dialog = null
        viewHolder = null
    }

    companion object {
        fun dismiss() {
            ClassHolder.INSTANCE.dimiss()
        }

        fun resetInternal() {
            ClassHolder.INSTANCE.resetInternal()
        }
    }

    private object ClassHolder {
        val INSTANCE = QuickDialog()
    }

    class Builder(var context: Context) {
        internal var width = WindowManager.LayoutParams.MATCH_PARENT
        internal var height = WindowManager.LayoutParams.WRAP_CONTENT
        internal var style = 0
        internal var gravity = Gravity.CENTER
        internal var canceledOnTouchOutside = false
        internal var isRewrite = false/*是否每次都重新创建dialog*/
        internal var paddingLeft = 0
        internal var paddingRight = 0
        internal var paddingTop = 0
        internal var paddingBottom = 0
        internal lateinit var layoutView: View

        fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean): Builder {
            this.canceledOnTouchOutside = canceledOnTouchOutside
            return this
        }

        fun setWindowPadding(left: Int, top: Int, right: Int, bottom: Int): Builder {
            this.paddingLeft = left
            this.paddingTop = top
            this.paddingRight = right
            this.paddingBottom = bottom
            return this
        }

        fun setStyle(@StyleRes style: Int): Builder {
            this.style = style
            return this
        }

        fun setLayout(@LayoutRes res: Int): Builder {
            return setLayout(LayoutInflater.from(context).inflate(res, null))
        }

        fun setLayout(@LayoutRes res: View): Builder {
            this.layoutView = res
            return this
        }

        fun setSize(width: Int, height: Int): Builder {
            this.width = width
            this.height = height
            return this
        }

        fun setGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        fun setRewrite(isRewrite: Boolean): Builder {
            this.isRewrite = isRewrite
            return this
        }

        fun build() = ClassHolder.INSTANCE.setupQuickDialog(this)

        fun create(): Dialog = ClassHolder.INSTANCE.setupQuickDialog(this).getDialog()!!

        fun show(): QuickViewHolder {
            return ClassHolder.INSTANCE.setupQuickDialog(this).show()
        }
    }
}