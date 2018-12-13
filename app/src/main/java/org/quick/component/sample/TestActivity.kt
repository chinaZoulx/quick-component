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
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import org.quick.component.*
import org.quick.component.http.HttpService
import org.quick.component.http.callback.OnRequestListener
import org.quick.component.sample.callback.TestBean
import org.quick.component.utils.DateUtils
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


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
        QuickAsync.asyncDelay({

        }, 100L)

        val task = QuickAsync.asyncLoop({ steps ->
            //这里执行
        }, 1000L)
        task.cancel(true)
        QuickDialog.Builder(this, R.layout.dialog_test).createViewHolder()
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
        sharedPreferences.edit().putString("name", "this is a name").putInt("age", 1).apply()

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

        val date = Date()
        val date2 = Date()
        val date3 = Date()
        val date4 = Date()
        /*获取最大的时间*/
        DateUtils.compareAfter(date, date2, date3, date4)
        /*获取最小的时间*/
        DateUtils.compareBefore(date, date2, date3, date4)

        /*date>date2*/
        DateUtils.after(date, date2)
        /*date<date2*/
        DateUtils.before(date, date2)
        /*比较时间戳*/
        DateUtils.after(System.currentTimeMillis(), 121321324548L)
        /*使用时间字符串比较*/
        DateUtils.after("2018-08-22 10:13", "2018-09-01 10:13")
        /*还可以指定格式*/
        DateUtils.after("2018-08-22 10:13", "2018-09-01 10:13", "yyyy-MM-dd HH:mm:ss")

        /*格式化为时间*/
        DateUtils.formatToDate(System.currentTimeMillis())
        /*也可以指定格式*/
        DateUtils.formatToDate("2018-08-22 10:13", "yyyy-MM-dd HH:mm:ss")

        /*可以输出为多种格式*/
        DateUtils.formatToLong(date)
        DateUtils.formatToLong("2018-08-22 10:13")
        DateUtils.formatToLong("2018-08-22 10:13", "yyyy-MM-dd HH:mm:ss")

        DateUtils.formatToStr(date, "yyyy-MM-dd HH:mm:ss")
        DateUtils.formatToStr(System.currentTimeMillis())

        /*格式化为Date*/
        val resultDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse("2018-08-22 10:13")
        /*格式化为Long*/
        val resultLong = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse("2018-08-22 10:13").time
        /*格式化为String*/
        val resultString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date)

        formatDateDifference(date.time - date2.time, "还有：", "就可以采摘啦")
        DateUtils.formatDateStopwatch(DateUtils.MILLISECOND * 50)
    }

    var dialog: Dialog? = null
    lateinit var titleTv: TextView
    lateinit var leftTv: TextView
    lateinit var rightTv: TextView

    /**
     * 格式化时间差
     *
     * @param timestamp
     * @param postfix 前缀
     * @param postfix 后缀
     */
    fun formatDateDifference(timestamp: Long, prefix: String, postfix: String): String {

        val day = timestamp / DateUtils.DAY
        val hour = (timestamp - DateUtils.DAY * day) / DateUtils.HOURS
        val minute = (timestamp - DateUtils.DAY * day - DateUtils.HOURS * hour) / DateUtils.MINUTE
        val second = (timestamp - DateUtils.DAY * day - DateUtils.HOURS * hour - DateUtils.MINUTE * minute) / DateUtils.SECOND

        return when {
            day > 0 -> String.format("$prefix%s天%s时%s分%s秒$postfix", day, hour, minute, second)
            hour > 0 -> String.format("$prefix%s时%s分%s秒$postfix", hour, minute, second)
            minute > 0 -> String.format("$prefix%s分%s秒$postfix", minute, second)
            else -> String.format("$prefix%s秒$postfix", second)
        }
    }

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