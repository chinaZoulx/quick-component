package org.quick.component.sample

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
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
        adapter.setOnItemClickListener { view, viewHolder, position, itemData ->

        }
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

    class Adapter : QuickAdapter<Int,QuickViewHolder>() {
        override fun onBindData(holder: QuickViewHolder, position: Int, itemData: Int) {
            holder.setImgCircle(R.id.iv, itemData)
        }

        override fun onResultLayoutResId(): Int = R.layout.item_rv_list
    }
}