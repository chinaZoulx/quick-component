package org.quick.component.sample

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_rv_list.*
import org.quick.component.*

class RvListActivity : Activity() {

    var tempTime = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var adapter = Adapter()
        var dataList = mutableListOf<Int>()
        dataList.add(R.mipmap.ic_launcher)
        dataList.add(R.mipmap.ic_launcher)
        dataList.add(R.mipmap.ic_launcher_round)
        dataList.add(R.mipmap.ic_launcher_round)
        dataList.add(R.mipmap.ic_launcher)
        dataList.add(R.mipmap.ic_launcher)
        recyclerView.adapter = adapter
        adapter.setDataList(dataList)
//        adapter.add(item)
//        adapter.addDataList(dataList)
//        adapter.remove(1)
//        adapter.remove("")
//        adapter.removeAll()

        adapter.setOnItemClickListener { view, viewHolder, position, itemData ->

        }
        adapter.setOnItemLongClickListener { view, viewHolder, position, itemData ->

            true
        }

//        adapter.setOnClickListener({ view, viewHolder, position, itemData ->
//
//        }, R.id.checkBox1, R.id.checkBox2, R.id.checkBox3)
//        adapter.setOnCheckedChangedListener({ view, viewHolder, isChecked, position, itemData ->
//
//        }, R.id.btn1, R.id.btn2, R.id.btn3)

        titleTv.setOnClickListener {
            QuickASync.async(object : QuickASync.OnIntervalListener<Int> {
                override fun onNext(value: Int) {
                    Log2.e(value.toString())
                    titleTv.text = "这是标题:$value"
                }

                override fun onAccept(value: Int) {
                    QuickNotify.notifyTempProgressEnd(1, R.mipmap.ic_launcher, "这是标题", "这是内容", null, null)
                }
            }, 500, 100)
        }
    }

    /*class Adapter2 : RecyclerView.Adapter<Adapter2.ViewHolder>() {
        private val dataList = mutableListOf<String>()
        private var onClickListener: ((position: Int, holder: ViewHolder) -> Unit?)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dialog_test, null))
        }

        override fun getItemCount(): Int = dataList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.titleTv.text = dataList[position]
            *//*设置点击事件*//*
            holder.itemView.setOnClickListener {
                if (onClickListener != null) onClickListener?.invoke(position, holder)
            }
        }

        fun setOnItemClickListener(onClickListener: (position: Int, holder: ViewHolder) -> Unit?) {
            this.onClickListener = onClickListener
        }

        inner class ViewHolder : RecyclerView.ViewHolder {
            lateinit var titleTv: TextView
            lateinit var userNameTv: TextView
            lateinit var ageTv: TextView
            lateinit var contentTv: TextView
            lateinit var coverIv: ImageView
            ...

            constructor(itemView: View) : super(itemView) {
                titleTv = itemView.findViewById(R.id.titleTv)
                userNameTv
                ageTv
                contentTv
                coverIv
                ...
            }
        }

    }*/

    class Adapter : BaseAdapter<Int>() {

        override fun onBindData(holder: BaseViewHolder, position: Int, itemData: Int, viewType: Int) {
            when (viewType) {
                1 -> {//绑定书的数据
                    holder.setImgRoundRect(R.id.coverIv, 10f, itemData)
                }
                2 -> {//绑定作者的数据

                }
            }
        }

        override fun getItemViewType(position: Int): Int = when (position) {
            123 -> {//展示书
                1
            }
            2,4 -> {//展示作者
                2
            }
            else -> 1
        }


        override fun onResultLayoutResId(viewType: Int): Int = when (viewType) {
            1 -> {//绑定书的Layout
                R.layout.item_rv_list
            }
            2 -> {//绑定作者的Layout
                R.layout.dialog_test
            }
            else -> {//绑定书的Layout
                R.layout.item_rv_list
            }
        }
    }
}