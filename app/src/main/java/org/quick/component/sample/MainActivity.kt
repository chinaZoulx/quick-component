package org.quick.component.sample

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.quick.component.*
import org.quick.component.utils.ViewUtils
import android.content.pm.PackageManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.quick.component.callback.OnClickListener2
import org.quick.component.utils.DateUtils
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
                        QuickNotify.notifyTempProgress(1, R.mipmap.ic_launcher2, "这是标题", "这是内容")
                        QuickAsync.asyncTime(object : QuickAsync.OnIntervalListener<Int> {
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
                        QuickAsync.async(object : QuickAsync.OnASyncListener<String> {
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
                        val test = QuickAsync.asyncTime(object : QuickAsync.OnIntervalListener<Long> {
                            override fun onNext(value: Long) {
                                sampleTv3.text = String.format("异步计时(%d)", value)
                            }

                            override fun onAccept(value: Long) {
                                sampleTv3.text = "异步计时(End)"
                            }
                        }, 1000, 10, checkbox.isChecked)

                        QuickAsync.asyncDelay({
                            /*5秒后暂停*/
                            test.cancel(true)
                        }, 5000)
                    }
                    R.id.sampleTv4 -> {//快速弹框Dialog
                        QuickDialog.Builder(this@MainActivity,R.layout.dialog_test).show()
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
                        QuickStartActivity.startActivity(QuickStartActivity.Builder(this@MainActivity, RvListActivity::class.java).addParams("TITLE", "这是标题"))
                    }
                    R.id.sampleTv7 -> {//快速存取本地数据
                        QuickSPHelper.putValue("A", "valueA").putValue("B", 1L).putValue("C", true)
                        QuickToast.Builder().setDuration(Toast.LENGTH_LONG).showToast(QuickSPHelper.getValue("B", 0L).toString())
                    }
                    R.id.sampleTv8 -> {//快速Toast
                        QuickToast.showToastDefault("这是一个Toast")
                    }
                    R.id.sampleTv9 -> {//快速通知-桌面快捷方式
                        val shortBuilder = QuickNotify.ShortcutBuilder("这是shortId")
                                .setActivity(packageName, RvListActivity::class.java.simpleName)
                                .setShortcut("this is a name", ImageUtils.decodeSampledBitmapFromResource(resources, R.mipmap.ic_launcher))

                        QuickNotify.notifyDesktopShortcut(shortBuilder) { context, intent ->
                            QuickToast.showToastDefault("已成功创建" + intent.getStringExtra(QuickNotify.shortcutName))
                        }
                    }
                    R.id.sampleTv10 -> {//快速指纹验证
                        QuickBiometric.startFingerprintListener { type, resultMsg ->
                            when (type) {
                                QuickBiometric.TYPE.AuthenticationSucceeded -> {/*成功*/
                                    QuickToast.showToastDefault("验证成功：加密串-$resultMsg")
                                }
                                QuickBiometric.TYPE.AuthenticationFailed -> {/*失败*/
                                    QuickToast.showToastDefault("验证失败")
                                }
                                else -> QuickToast.showToastDefault(resultMsg)/*错误*/
                            }
                            Log2.e(resultMsg)
                        }
                    }
                }
            }
        }
        for (index in 0..10) {
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
        /*50毫秒*/
        Log2.e(DateUtils.formatDateStopwatch(DateUtils.MILLISECOND * 50))
        /*1天零2小时*/
        Log2.e(DateUtils.formatDateStopwatch(DateUtils.DAY * 1 + DateUtils.HOURS * 2))
        Log2.e(DateUtils.formatDateStopwatch(DateUtils.SECOND * 179 + DateUtils.MINUTE * 1 + DateUtils.MILLISECOND * 10))
        Log2.e(DateUtils.formatDateStopwatch((DateUtils.SECOND * 1.5).toLong()))
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
