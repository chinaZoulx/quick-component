package org.quick.component.sample

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.quick.component.*

class TestActivity : AppCompatActivity() {

    val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                123 -> {
                    val bundle = msg.data
                    QuickToast.showToastDefault(String.format("收到一个ID:%s", bundle.getString("id")))
                }
            }
        }
    }
    val thread = object : Thread() {
        override fun run() {
            Thread.sleep(2000)
            val msg = Message()
            val bundle = Bundle()
            bundle.putString("id", "123")
            bundle.putString("max", "this is a max")
            msg.data = bundle
            msg.what = 123
            handler.sendMessage(msg)
            super.run()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thread.start()
        QuickASync.async({
            //这里执行
        }, 1000)
        QuickDialog.Builder(this).setLayout(R.layout.dialog_test).build().viewHolder()
        Toast.makeText(this, "这是内容", Toast.LENGTH_SHORT).show()

        val toast = Toast(this)
        toast.view = LayoutInflater.from(this).inflate(R.layout.app_toast, null)
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.view.findViewById<TextView>(R.id.toastMsgTv).text = "这是内容"
        toast.show()

        QuickToast.showToastDefault("这是内容")
        QuickToast.Builder().setDuration(Toast.LENGTH_SHORT).setGravity(Gravity.BOTTOM, 0, 100).showToast("这是内容")
        QuickToast.Builder().setDuration(Toast.LENGTH_SHORT).setGravity(Gravity.BOTTOM, 0, 100).create("这是内容")
//        QuickToast.Builder().setGravity(Gravity.CENTER).showToast("这是内容")
//                .setImg(R.id.imgIv, R.mipmap.ic_launcher)
//                .setText(R.id.userName, "这是用户名称")

        QuickNotify.notifyTempNormal(R.mipmap.ic_launcher2, "这是标题", "这是内容", Intent()) { context, intent ->


        }

        val sharedPreferences = getSharedPreferences("this is a name", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("name", "this is a name").putInt("age", 1).commit()

        sharedPreferences.getString("name", "")
        sharedPreferences.getInt("age", 0)

        QuickSPHelper.putValue("name", "this is a name").putValue("age", 1)
        QuickSPHelper.getValue("name", "")
        QuickSPHelper.getValue("age", 0)

//        val viewHolder = QuickViewHolder(LayoutInflater.from(this).inflate(R.layout.app_toast, null))
//        viewHolder.setText(R.id.toastMsgTv, "", View.OnClickListener {
//            QuickToast.showToastDefault("点击了内容")
//        }).setText(R.id.toastMsgTv, "")
//                .setImg(R.id.coverIv, R.mipmap.ic_launcher)
//                .setImgRoundRect(R.id.coverIv, 5.0F, R.mipmap.ic_launcher)
//                .setImgCircle(R.id.coverIv, R.mipmap.ic_launcher, View.OnClickListener {
//                    QuickToast.showToastDefault("点击了头像")
//                })
//                ...
//        viewHolder.getView<TextView>(R.id.toastMsgTv)
//        viewHolder.getImageView(R.id.coverIv)
    }

    var dialog: Dialog? = null
    lateinit var titleTv: TextView
    lateinit var leftTv: TextView
    lateinit var rightTv: TextView

    fun showDialog(title: String, leftTxt: String, rightTxt: String) {
        if (dialog == null) {
            val itemView = LayoutInflater.from(this).inflate(R.layout.dialog_test, null)
            titleTv = findViewById(R.id.titleTv)
            leftTv = findViewById(R.id.leftTv)
            rightTv = findViewById(R.id.rightTv)
            dialog = Dialog(this, R.style.AppTheme)
            dialog!!.setContentView(itemView)
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCanceledOnTouchOutside(false)
//            dialog!!.window?.setLayout(500, 500)
        }
        titleTv.text = title
        leftTv.text = leftTxt
        rightTv.text = rightTxt
        if (!dialog!!.isShowing)
            dialog!!.show()
    }

//    inner class ViewHolder : RecyclerView.ViewHolder {
//        lateinit var titleTv: TextView
//        lateinit var userNameTv: TextView
//        lateinit var ageTv: TextView
//        lateinit var contentTv: TextView
//        lateinit var coverIv: ImageView
//        ...
//
//        constructor(itemView: View) : super(itemView) {
//            titleTv = itemView.findViewById(R.id.titleTv)
//            userNameTv
//            ageTv
//            contentTv
//            coverIv
//            ...
//        }
//    }
}