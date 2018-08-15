package org.quick.component.sample

import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.quick.component.*
import org.quick.component.utils.ViewUtils
import android.content.pm.PackageManager
import android.app.Activity
import android.app.ActivityManager
import android.content.*
import android.widget.Toast
import org.quick.component.callback.OnClickListener2
import org.quick.component.utils.DevicesUtils
import org.quick.component.utils.ImageUtils


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val onClickListener2 = object : OnClickListener2() {
            override fun onClick2(view: View) {
                when (view.id) {
                    R.id.sampleTv0 -> {//快速普通通知
                        QuickNotify.notifyTempNormal(R.mipmap.ic_launcher2, "这是标题", "我是弹来耍的，任性！")
                    }
                    R.id.sampleTv1 -> {//快速进度通知
                        QuickNotify.notifyTempProgress(1,R.mipmap.ic_launcher2,"这是标题","这是内容")
                        QuickASync.async(object : QuickASync.OnIntervalListener<Int> {
                            override fun onNext(value: Int) {
                                QuickNotify.notifyTempProgresses(1, R.mipmap.ic_launcher, "这是标题", "这是内容", value)
                            }

                            override fun onAccept(value: Int) {
                                QuickNotify.notifyTempProgressEnd(1, R.mipmap.ic_launcher, "这是标题", "这是内容", null) { context, intent ->
                                    QuickToast.showToastDefault("点击了")
                                }
                            }
                        }, 500, 100, false)
                    }
                    R.id.sampleTv2 -> {//快速异步线程
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
                    }
                    R.id.sampleTv3 -> {//异步计时
                        val test = QuickASync.async(object : QuickASync.OnIntervalListener<Long> {
                            override fun onNext(value: Long) {
                                sampleTv3.text = String.format("异步计时(%d)", value)
                            }

                            override fun onAccept(value: Long) {
                                sampleTv3.text = "异步计时(End)"
                            }
                        }, 1000, 10, checkbox.isChecked)

                        QuickASync.async({/*5秒后暂停*/
                            test.cancel(true)
                        }, 5000)
                    }
                    R.id.sampleTv4 -> {//快速弹框Dialog
                        QuickDialog.Builder(this@MainActivity).setLayout(R.layout.dialog_test).show()
                                .setText(R.id.leftTv, "取消", View.OnClickListener {
                                    QuickToast.showToastDefault("点击了取消")
                                    QuickDialog.dismiss()
                                }).setText(R.id.rightTv, "确定", View.OnClickListener {
                                    QuickToast.showToastDefault("点击了确定")
                                    QuickDialog.dismiss()
                                })
                    }
                    R.id.sampleTv5 -> {//快速广播
                        val intent = QuickBroadcast.Builder().addParams("data", "这是一个字符串", "这又是一个字符串").build()
                        QuickBroadcast.sendBroadcast(intent, "testAction")
                    }
                    R.id.sampleTv6 -> {//快速适配器
                        QuickStartActivity.startActivity(this@MainActivity, QuickStartActivity.Builder(this@MainActivity, RvListActivity::class.java).addParams("TITLE", "这是标题").build())
                    }
                    R.id.sampleTv7 -> {//快速存取本地数据
                        QuickSPHelper.putValue("A", "valueA").putValue("B", 1L).putValue("C", true)
                        QuickToast.Builder().setDuration(Toast.LENGTH_LONG).showToast(QuickSPHelper.getValue("B", 0L).toString())
                    }
                    R.id.sampleTv8 -> {//快速Toast
                        QuickToast.showToastDefault("这是一个Toast")
                    }
                    R.id.sampleTv9 -> {//快速通知-桌面快捷方式
                        val shortBuilder=QuickNotify.ShortcutBuilder("这是shortId")
                                .setActivity(packageName, RvListActivity::class.java.simpleName)
                                .setShortcut("this is a name", ImageUtils.decodeSampledBitmapFromResource(resources, R.mipmap.ic_launcher))

                        QuickNotify.notifyDesktopShortcut(shortBuilder) { context, intent ->
                            QuickToast.showToastDefault("已成功创建" + intent.getStringExtra(QuickNotify.shortcutName))
                        }
                    }
                }
            }
        }
        for (index in 0..9) {
            val id = ViewUtils.getViewId("sampleTv", index.toString())
            findViewById<View>(id).setOnClickListener(onClickListener2)
            findViewById<View>(id).setBackgroundResource(ViewUtils.getSystemAttrTypeValue(this, R.attr.selectableItemBackground).resourceId)
        }

        QuickBroadcast.addBroadcastListener(this, { action, intent ->
            QuickToast.showToastDefault("收到一个广播$action")
        }, "testAction", "testAction2", "testAction3")
//        val intent = Intent(this, RvListActivity::class.java)
//        val requestCode = 0x123
//        QuickStartActivity.startActivity(this, intent) { resultCode: Int, data: Intent? ->
//
//        }
//
//        val intent = Intent(this, MainActivity::class.java)
//        val requestCode = 0x123
//        QuickStartActivity.startActivity(this, intent) { resultCode: Int, data: Intent? ->
//
//        }

//        startActivityForResult(intent, requestCode)

    }

    //启用组件
    fun enableComponent(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        DevicesUtils.restartSystemLauncher()
    }

    //隐藏组件
    fun disableComponent(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        QuickStartActivity.onActivityResult(requestCode, resultCode, data)
    }
}
