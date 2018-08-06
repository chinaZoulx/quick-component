package org.quick.component.sample

import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.chris.quick.tools.AnimUtils
import org.quick.component.*
import org.quick.component.utils.ViewUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contentTv.setBackgroundResource(ViewUtils.getSystemAttrTypeValue(this, R.attr.selectableItemBackgroundBorderless).resourceId)
        contentTv.setOnClickListener {
            QuickToast.showToastDefault("fdsfdf")
        }
        shareTv.setOnClickListener {
            //            imgIv1.setImageBitmap(ImageUtils.cropCircle(ImageUtils.decodeSampledBitmapFromResource(resources, R.mipmap.ic_launcher, it.measuredWidth, it.measuredHeight)))
//            imgIv2.setImageBitmap(ImageUtils.cropRoundRect(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher), FormatUtils.formatDip2Px(10f)))
//            Log2.e(CheckUtils.isShowSoftInput(this@MainActivity).toString())
//            DevicesUtils.installAPK(File("/storage/emulated/0/Downloads/1.7.2_201807130__172_jiagu_sign.apk1.7.2_201807130_开心红包_172_jiagu_sign.apk"))
            QuickASync.async(object : QuickASync.OnASyncListener<String> {
                override fun onASync(): String {
                    return if (Looper.getMainLooper() == Looper.myLooper()) "主线程" else "子线程"
                }

                override fun onAccept(value: String) {
                    Log2.e("onASync:$value")
                    val temp = if (Looper.getMainLooper() == Looper.myLooper()) "主线程" else "子线程"
                    Log2.e("onAccept:$temp")
                }
            })

            QuickASync.async(object : QuickASync.OnIntervalListener<Long> {
                override fun onNext(value: Long) {
                    shareTv.text = String.format("测试异步(%d)", value)
                }

                override fun onAccept(value: Long) {
                    shareTv.text = "测试异步(End)"
                }

            }, 1000, 10)
        }
        imgIv1.setOnClickListener { QuickStartActivity.startActivity(this@MainActivity, QuickStartActivity.Builder(this@MainActivity, RvListActivity::class.java).build()) }
        imgIv2.setOnClickListener {
            QuickDialog.Builder(this@MainActivity).setLayout(R.layout.dialog_test).setWindowPadding(100, 0, 100, 0).show().setText(R.id.leftTv, "取消", View.OnClickListener {
                QuickToast.showToastDefault("点击了取消")
                QuickDialog.dismiss()
            }).setText(R.id.rightTv, "确定", View.OnClickListener {
                QuickToast.showToastDefault("点击了确定")
                QuickDialog.dismiss()
            })
        }
        tempTv.setOnClickListener {
            AnimUtils.fadeIn(null, 1000, shareTv)
            QuickASync.async(object : QuickASync.OnIntervalListener<Int> {
                override fun onNext(value: Int) {
                    QuickNotify.notifyTempProgresses(1, R.mipmap.ic_launcher, "这是标题", "这是内容", value)
                }

                override fun onAccept(value: Int) {
                    QuickNotify.notifyTempProgressEnd(1, R.mipmap.ic_launcher, "这是标题", "这是内容", null, null)
                }
            }, 500, 100)
        }

    }
}
