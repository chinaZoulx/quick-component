package org.quick.component

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat
import android.util.SparseArray
import org.chris.quick.tools.DateUtils

/**
 * @describe 快速使用通知
 * @author ChrisZou
 * @date 2018/08/02-14:33
 * @from https://github.com/SpringSmell/quick.library
 * @email chrisSpringSmell@gmail.com
 */
object QuickNotify {
    val action = javaClass.`package`.name + "-" + javaClass.simpleName + ":action"
    val actionCancel = javaClass.`package`.name + "-" + javaClass.simpleName + ":actionCancel"
    val actionClick = javaClass.`package`.name + "-" + javaClass.simpleName + ":actionClick"
    val actionNotificationId = javaClass.`package`.name + "-" + javaClass.simpleName + ":actionNotificationId"

    val notificationListeners: SparseArray<(context: Context, intent: Intent) -> Unit> by lazy { return@lazy SparseArray<((context: Context, intent: Intent) -> Unit)>() }
    val notificationManager: NotificationManager by lazy { return@lazy QuickAndroid.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.createNotificationChannel(NotificationChannel(QuickAndroid.applicationContext.packageName, QuickAndroid.applicationContext.packageName, NotificationManager.IMPORTANCE_HIGH))
        QuickAndroid.applicationContext.registerReceiver(NotificationReceiver(), IntentFilter(action))
    }

    /**
     * 临时通知，允许移除
     * 该通知只响应点击动作
     */
    fun notifyTempNormal(@DrawableRes icon: Int, title: String, content: String) {
        val notificationId = Math.abs(DateUtils.getCurrentTimeInMillis().toInt())
        notify(notificationId, NotificationCompat.Builder(QuickAndroid.applicationContext, QuickAndroid.appBaseName)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing(false)
                .setAutoCancel(true), null, null, null)
    }

    /**
     * 临时通知，允许移除
     * 该通知只响应点击动作
     * @param intentClick 传递自定义内容，用于点击回调返回内容
     */
    fun notifyTempNormal(@DrawableRes icon: Int, title: String, content: String, intentClick: Intent, onNotificationListener: ((context: Context, intent: Intent) -> Unit)) {
        val notificationId = Math.abs(DateUtils.getCurrentTimeInMillis().toInt())
        notify(notificationId, NotificationCompat.Builder(QuickAndroid.applicationContext, QuickAndroid.appBaseName)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing(false)
                .setAutoCancel(true), intentClick, null, onNotificationListener)
    }


    /**
     * 临时通知-通知进度准备
     */
    fun notifyTempProgress(notifyId: Int, @DrawableRes icon: Int, title: String, content: String) {
        notify(notifyId, NotificationCompat.Builder(QuickAndroid.applicationContext, QuickAndroid.appBaseName)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setProgress(100, 0, true)
                .setOngoing(true)/*是否正在通知（是否不可以取消）*/
                .setAutoCancel(false)/*是否点击时取消*/)
    }

    /**
     * 临时通知-通知进度中
     */
    fun notifyTempProgresses(notifyId: Int, @DrawableRes icon: Int, title: String, content: String, progress: Int) {
        notify(notifyId, NotificationCompat.Builder(QuickAndroid.applicationContext, QuickAndroid.appBaseName)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setProgress(100, progress, false)
                .setOngoing(true)/*是否正在通知（是否不可以取消）*/
                .setAutoCancel(false)/*是否点击时取消*/)
    }

    /**
     * 临时通知-通知进度完成-点击取消
     */
    fun notifyTempProgressEnd(notifyId: Int, @DrawableRes icon: Int, title: String, content: String, intentClick: Intent?, onNotificationListener: ((context: Context, intent: Intent) -> Unit)?) {
        notify(notifyId, NotificationCompat.Builder(QuickAndroid.applicationContext, QuickAndroid.appBaseName)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setProgress(100, 100, false)
                .setOngoing(false)/*是否正在通知（是否不可以取消）*/
                .setAutoCancel(true)/*是否点击时取消*/
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                , intentClick, null, onNotificationListener)
    }

    /**
     * 自定义通知-不接管任务操作，只显示通知
     */
    fun notify(notificationId: Int, builder: NotificationCompat.Builder) {
        notify(notificationId, builder, null, null, null)
    }

    /**
     * 自定义通知-接管回调事件
     */
    fun notify(notificationId: Int, builder: NotificationCompat.Builder, onNotificationListener: ((context: Context, intent: Intent) -> Unit)) {
        val intentCancel = Intent(action)
        intentCancel.putExtra(action, actionCancel)
        intentCancel.putExtra(actionNotificationId, notificationId)

        val intentClick = Intent(action)
        intentCancel.putExtra(action, actionClick)
        intentCancel.putExtra(actionNotificationId, notificationId)
        notify(notificationId, builder, intentClick, intentCancel, onNotificationListener)
    }

    /**
     * 自定义通知-不接管任何事件
     *
     * 注意：如果需要使用自带的点击回调，请将在intent的action指定为{@link #action }例如：Intent(QuickNotify.action)
     * 并且将通知ID添加进intent{@link #actionNotificationId},例如：intentCancel.putExtra(actionNotificationId, notificationId)
     *
     * 这里提供一个自定义通知View的写法
     * for example:
     * 1、先实例一个RemoteViews
     * val customLayout = RemoteViews(packageName, R.layout.app_download_notification)
     *     customLayout.setTextViewText(R.id.titleTv, model.title)
     *     customLayout.setImageViewResource(R.id.coverIv, model.cover)
     *     customLayout.setOnClickPendingIntent(R.id.downloadStatusContainer, pendingIntentCancel)
     * 2、将实例好的RemoteViews安装进通知
     *     NotificationCompat.Builder(this, packageName).setCustomContentView(customLayout)
     * 3、安装好的Builder进行通知即可
     *
     * @param onNotificationListener 如果自行实现点击事件可不传
     */
    fun notify(notificationId: Int, builder: NotificationCompat.Builder, intentClick: Intent?, intentCancel: Intent?, onNotificationListener: ((context: Context, intent: Intent) -> Unit)?) {
        if (onNotificationListener != null) {
            if (intentCancel == null) {
                val tempCancel = Intent(action)
                tempCancel.putExtra(action, actionCancel)
                tempCancel.putExtra(actionNotificationId, notificationId)
                builder.setDeleteIntent(PendingIntent.getBroadcast(QuickAndroid.applicationContext, notificationId, tempCancel, PendingIntent.FLAG_UPDATE_CURRENT))
            }
            if (intentClick == null) {
                val tempClick = Intent(action)
                tempClick.putExtra(action, actionClick)
                tempClick.putExtra(actionNotificationId, notificationId)
                builder.setContentIntent(PendingIntent.getBroadcast(QuickAndroid.applicationContext, notificationId, tempClick, PendingIntent.FLAG_UPDATE_CURRENT))
            }
            notificationListeners.put(notificationId, onNotificationListener)
        }
        notificationManager.notify(notificationId, builder.build())
    }

    fun cancelnotify(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            QuickNotify.notificationListeners.get(intent.getIntExtra(actionNotificationId, 0))?.invoke(context, intent)
        }
    }
}