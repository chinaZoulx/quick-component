package org.quick.component

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import org.quick.component.utils.FormatUtils
import org.quick.component.utils.ViewUtils

/**
 * Created by chris Zou on 2016/6/12.
 * 基础适配器,继承便能快速生成adapter
 * 如需分割线，请参照quick-library中的XRecyclerViewLine
 * @author chris Zou
 * @Date 2016/6/12
 */
abstract class QuickAdapter<M, H : QuickViewHolder> : RecyclerView.Adapter<H>() {
    private var dataList = mutableListOf<M>()
    lateinit var context: Context

    var mOnItemClickListener: ((view: View, viewHolder: H, position: Int, itemData: M) -> Unit)? = null
    var mOnItemLongClickListener: ((view: View, viewHolder: H, position: Int, itemData: M) -> Boolean)? = null
    var mOnClickListener: ((view: View, viewHolder: H, position: Int, itemData: M) -> Unit)? = null
    var mOnCheckedChangedListener: ((view: View, viewHolder: H, isChecked: Boolean, position: Int, itemData: M) -> Unit)? = null
    var clickResId: IntArray = intArrayOf()
    var checkedChangedResId = intArrayOf()

    /**
     * 布局文件
     *
     * @return
     */
    abstract fun onResultLayoutResId(): Int

    abstract fun onBindData(holder: H, position: Int, itemData: M)

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 上下左右的padding
     *
     * @return
     */
    open fun onResultItemMargin(): Float {
        return 0.0f
    }

    /**
     * 上下左右的padding
     *
     * @return
     */
    open fun onResultItemMarginTop(position: Int): Float {
        return if (onResultItemMargin() > 0) onResultItemMargin() / 2 else onResultItemMargin()
    }

    /**
     * 上下左右的padding
     *
     * @return
     */
    open fun onResultItemMarginBottom(position: Int): Float {
        return if (onResultItemMargin() > 0) onResultItemMargin() / 2 else onResultItemMargin()
    }

    /**
     * 上下左右的padding
     *
     * @return
     */
    open fun onResultItemMarginLeft(position: Int): Float {
        return onResultItemMargin()
    }

    /**
     * 上下左右的padding
     *
     * @return
     */
    open fun onResultItemMarginRight(position: Int): Float {
        return onResultItemMargin()
    }

    open fun onResultItemPadding(): Float {
        return 0.0f
    }

    /**
     * 上下左右的padding
     *
     * @return
     */
    open fun onResultItemPaddingTop(position: Int): Float {
        return if (onResultItemPadding() > 0) onResultItemPadding() / 2 else onResultItemPadding()
    }

    /**
     * 上下左右的padding
     *
     * @return
     */
    open fun onResultItemPaddingBottom(position: Int): Float {
        return if (onResultItemPadding() > 0) onResultItemPadding() / 2 else onResultItemPadding()
    }

    /**
     * 上下左右的padding
     *
     * @return
     */
    open fun onResultItemPaddingLeft(position: Int): Float {
        return onResultItemPadding()
    }

    /**
     * 上下左右的padding
     *
     * @return
     */
    open fun onResultItemPaddingRight(position: Int): Float {
        return onResultItemPadding()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(onResultLayoutResId(), parent, false)
        return setupLayout(view)
    }

    fun setupLayout(itemView: View): H {
        if (itemView is CardView) {
            if (itemView.foreground == null)
                itemView.foreground = ContextCompat.getDrawable(context, ViewUtils.getSystemAttrTypeValue(context, R.attr.selectableItemBackgroundBorderless).resourceId)
        } else {
            if (itemView.background == null)
                itemView.setBackgroundResource(ViewUtils.getSystemAttrTypeValue(context, R.attr.selectableItemBackground).resourceId)
        }
        return onResultViewHolder(itemView)
    }

    open fun onResultViewHolder(itemView: View): H = QuickViewHolder(itemView) as H

    override fun onBindViewHolder(holder: H, position: Int) {
        setupListener(holder, position)
        setupLayout(holder, position)
        onBindData(holder, position, getDataList()[position])
    }

    /**
     * 设置各种监听
     *
     * @param holder
     * @param position
     */
    private fun setupListener(holder: H, position: Int) {
        /*单击事件*/
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(object : org.quick.component.callback.OnClickListener2() {
                override fun onClick2(view: View) {
                    mOnItemClickListener?.invoke(view, holder, position, getItem(position))
                }

            })
        }
        /*长按事件*/
        if (mOnItemLongClickListener != null) holder.itemView.setOnLongClickListener { v -> mOnItemLongClickListener!!.invoke(v, holder, position, getItem(position)) }
        /*选择事件*/
        if (mOnCheckedChangedListener != null && checkedChangedResId.isNotEmpty()) {
            for (resId in checkedChangedResId) {
                val compoundButton = holder.getView<View>(resId)
                if (compoundButton is CompoundButton)
                    compoundButton.setOnCheckedChangeListener { buttonView, isChecked -> mOnCheckedChangedListener?.invoke(buttonView, holder, isChecked, position, getItem(position)) }
                else
                    Log2.e("列表选择事件错误：", String.format("from%s id:%d类型不正确，无法设置OnCheckedChangedListener", context.javaClass.simpleName, resId))
            }
        }
        /*item项内View的独立点击事件，与OnItemClickListner不冲突*/
        if (mOnClickListener != null && clickResId.isNotEmpty()) {
            holder.setOnClickListener(object : org.quick.component.callback.OnClickListener2() {
                override fun onClick2(view: View) {
                    mOnClickListener?.invoke(view, holder, position, getItem(position))
                }
            }, *clickResId)
        }
    }

    /**
     * 设置布局
     *
     * @param holder
     * @param position
     */
    private fun setupLayout(holder: H, position: Int) {
        if (onResultItemMargin() > 0) {
            val margin = FormatUtils.formatPx2Dip(onResultItemMargin()).toInt()
            val left = FormatUtils.formatPx2Dip(onResultItemMarginLeft(position)).toInt()
            val top = FormatUtils.formatPx2Dip(onResultItemMarginTop(position)).toInt()
            val right = FormatUtils.formatPx2Dip(onResultItemMarginRight(position)).toInt()
            var bottom = FormatUtils.formatPx2Dip(onResultItemMarginBottom(position)).toInt()

            if (position == itemCount - 1)
                bottom = margin
            val itemLayoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
            itemLayoutParams.setMargins(left, top, right, bottom)
        }
        if (onResultItemPadding() > 0) {
            val padding = FormatUtils.formatPx2Dip(onResultItemPadding()).toInt()
            val left = FormatUtils.formatPx2Dip(onResultItemPaddingLeft(position)).toInt()
            val top = FormatUtils.formatPx2Dip(onResultItemPaddingTop(position)).toInt()
            val right = FormatUtils.formatPx2Dip(onResultItemPaddingRight(position)).toInt()
            var bottom = FormatUtils.formatPx2Dip(onResultItemPaddingBottom(position)).toInt()

            if (position == itemCount - 1)
                bottom = padding
            holder.itemView.setPadding(left, top, right, bottom)
        }
    }

    fun setDataList(dataList: MutableList<M>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun addDataList(dataList: List<M>) {
        if (dataList.isNotEmpty()) {
            getDataList().addAll(dataList)
            notifyDataSetChanged()
        }
    }

    fun remove(position: Int) {
        remove(getItem(position))
    }

    fun remove(m: M) {
        getDataList().remove(m)
        notifyDataSetChanged()
    }

    fun removeAll() {
        getDataList().clear()
        notifyDataSetChanged()
    }

    fun add(m: M) {
        getDataList().add(m)
        notifyDataSetChanged()
    }

    fun getDataList(): MutableList<M> {
        return dataList
    }

    fun getItem(position: Int): M {
        return getDataList()[position]
    }

    fun setOnClickListener(onClickListener: ((view: View, viewHolder: H, position: Int, itemData: M) -> Unit), vararg params: Int) {
        this.clickResId = params
        this.mOnClickListener = onClickListener
    }

    fun setOnCheckedChangedListener(onCheckedChangedListener: ((view: View, viewHolder: H, isChecked: Boolean, position: Int, itemData: M) -> Unit), vararg checkedChangedResId: Int) {
        this.checkedChangedResId = checkedChangedResId
        this.mOnCheckedChangedListener = onCheckedChangedListener
    }

    fun setOnItemClickListener(onItemClickListener: ((view: View, viewHolder: H, position: Int, itemData: M) -> Unit)) {

        this.mOnItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: ((view: View, viewHolder: H, position: Int, itemData: M) -> Boolean)) {
        this.mOnItemLongClickListener = onItemLongClickListener
    }
}
