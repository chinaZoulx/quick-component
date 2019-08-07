package org.quick.component.sample

import android.view.View

abstract class BaseAdapter<M> : QuickAdapter<M, BaseViewHolder>() {
    override fun onResultViewHolder(itemView: View): BaseViewHolder = BaseViewHolder(itemView)
}
