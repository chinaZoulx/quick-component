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
import org.quick.component.utils.ImageUtils


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
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

            }, 1000, 10, true)
        }
        imgIv1.setOnClickListener { QuickStartActivity.startActivity(this@MainActivity, QuickStartActivity.Builder(this@MainActivity, RvListActivity::class.java).build()) }
        imgIv2.setOnClickListener {
            QuickDialog.Builder(this@MainActivity).setLayout(R.layout.dialog_test).create()
            QuickDialog.Builder(this@MainActivity).setLayout(R.layout.dialog_test).setWindowPadding(100, 0, 100, 0).show().setText(R.id.leftTv, "取消", View.OnClickListener {
                QuickToast.showToastDefault("点击了取消")
                QuickDialog.dismiss()
            }).setText(R.id.rightTv, "确定", View.OnClickListener {
                QuickToast.showToastDefault("点击了确定")
                QuickDialog.dismiss()
            })
        }
        tempTv.setOnClickListener {
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
        shortcutChangeTv.setOnClickListener {
            if (it.tag != null && it.tag as Boolean) {
                it.tag = false
                disableComponent(ComponentName(this, "org.quick.component.sample.MainAliasActivity"))
                enableComponent(ComponentName(this, "org.quick.component.sample.MainActivity"))
            } else {
                it.tag = true
                disableComponent(ComponentName(this, "org.quick.component.sample.MainActivity"))
                enableComponent(ComponentName(this, "org.quick.component.sample.MainAliasActivity"))
            }
        }
//        var intent = Intent()
//        QuickNotify.notifyTempNormal(R.mipmap.ic_launcher, "这是标题", "这是内容", intent) { context, intent ->
//            QuickToast.showToastDefault("点击了通知")
//        }
//        QuickNotify.notifyTempProgress(1, R.mipmap.ic_launcher, "这是标题", "这是内容")
//        QuickSPHelper.putValue("A", "valueA").putValue("B", 1L).putValue("C", true)
//        QuickSPHelper.getValue("B", 0L)
        shortcutTv.setOnClickListener {
            QuickNotify.notifyDesktopShortcut(QuickNotify.ShortcutBuilder(Math.random().toString()).setActivity(packageName, RvListActivity::class.java.simpleName, Bundle()).setShortcut(Math.random().toString(), ImageUtils.drawableToBitmap(getDrawable(R.mipmap.ic_launcher)))) { context, intent ->
                QuickToast.showToastDefault("已成功创建" + intent.getStringExtra(QuickNotify.shortcutName))
            }
        }
    }

    fun changeIcon(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
//        pm.setComponentEnabledSetting(ComponentName(this, activityPath), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        //重启桌面 加速显示
//        restartSystemLauncher(pm);
    }

    //启用组件
    fun enableComponent(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        restartSystemLauncher(packageManager)
    }

    //隐藏组件
    fun disableComponent(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
    }

    fun restartSystemLauncher(pm: PackageManager) {
        val am = getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val i = Intent(Intent.ACTION_MAIN)
        i.addCategory(Intent.CATEGORY_HOME)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        val resolves = pm.queryIntentActivities(i, 0)
        for (res in resolves) {
            if (res.activityInfo != null) {
                am.killBackgroundProcesses(res.activityInfo.packageName)
            }
        }
    }
}
