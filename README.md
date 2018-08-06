## quick-component
Android 快速开发组件，对常用的组件进行了二次封装，更加便捷个性化使用。纯代码编写未引用其他组件，避免了组件冲突，使得代码量很小<br>
## gradle引用示例
~~~java
implementation 'org.quick.android:quick-component:0.0.3'
~~~
## 组件使用前需初始化：
一次初始化即可
~~~java
QuickAndroid.init(this)
~~~
## QuickAdapter
快速创建RecyclerView的Adapter，高度灵活设置item参数,padding,margin都可以设置，集成了多种回调事件，OnItemClickListener,OnItemLongClickListener,OnCheckListener,OnClickLister。
### 简单使用示例
~~~java
class Adapter : QuickAdapter<Int,QuickViewHolder>() {
        override fun onBindData(holder: QuickViewHolder, position: Int, itemData: Int) {
            holder.setImgCircle(R.id.iv, itemData)
        }

        override fun onResultLayoutResId(): Int = R.layout.item_rv_list
    }
~~~
### 复杂使用示例-可自定义margin,padding
~~~java
class Adapter : QuickAdapter<Int,QuickViewHolder>() {
        override fun onResultLayoutResId(): Int = R.layout.item_discover_list

        override fun onBindData(holderRv: QuickViewHolder, position: Int, itemData: String?) {
            holderRv.setImg(R.id.contentIv, itemData)
        }

        //----------margin 一般用于CardView
        override fun onResultItemMargin(): Int = 40

        override fun onResultItemMarginLeft(position: Int): Int = when {
            position % 2 == 0 -> 40
            else -> 20
        }

        override fun onResultItemMarginRight(position: Int): Int = when {
            position % 2 != 0 -> 40
            else -> 20
        }

        override fun onResultItemMarginTop(position: Int): Int {
            return super.onResultItemMarginTop(position)
        }

        override fun onResultItemMarginBottom(position: Int): Int {
            return super.onResultItemMarginBottom(position)
        }

        //------------Padding，一般用于常规列表
        override fun onResultItemPadding(): Int {
            return super.onResultItemPadding()
        }

        override fun onResultItemPaddingLeft(position: Int): Int {
            return super.onResultItemPaddingLeft(position)
        }
    }
~~~
有时业务有特殊需求，需要自定义Adapter，可继承自定义
~~~java
abstract class BaseAdapter<M> : QuickAdapter<M, BaseViewHolder>() {
    override fun onResultViewHolder(itemView: View): BaseViewHolder = BaseViewHolder(itemView)
}
~~~
重写后Adapter中使用的ViewHolder也可换成自己的BaseViewHolder
## QuickViewHolder
灵活使用的View持有器，使用链式设置方式，快速设置text，Listener等常用操作数据方式。
<br>
注意：每个人使用的图片加载框架不一样，此处需要自定义并重写绑定图片,换成自已的加载框架
<br>
框架内与QuickAdapter、QuickDialog配合使用
### 使用示例
~~~java
class BaseViewHolder(itemView: View) : QuickViewHolder(itemView) {

    override fun bindImg(context: Context, url: String, imageView: ImageView): QuickViewHolder {
        return super.bindImg(context, url, imageView)
    }

    override fun bindImgRoundRect(context: Context, url: String, radius: Float, imageView: ImageView): QuickViewHolder {
        return super.bindImgRoundRect(context, url, radius, imageView)
    }

    override fun bindImgCircle(context: Context, url: String, imageView: ImageView): BaseViewHolder {
        return this
    }
}
~~~
## QuickASync
快速常用的异常操作，包含了倒计时等操作<br>
### 异步示例
~~~java
QuickASync.async(object : QuickASync.OnASyncListener<String> {
                override fun onASync(): String {
                //在子线程中操作耗时操作，然后将结果返回到主线程中
                    return if (Looper.getMainLooper() == Looper.myLooper()) "主线程" else "子线程"
                }

                override fun onAccept(value: String) {
                //这是主线程接收值
                    Log2.e("onASync:$value")
                    val temp = if (Looper.getMainLooper() == Looper.myLooper()) "主线程" else "子线程"
                    Log2.e("onAccept:$temp")
                }
            })
~~~
### 计时10秒，每秒执行一次，1到10，示例
~~~java
QuickASync.async(object : QuickASync.OnIntervalListener<Long> {
                override fun onNext(value: Long) {
                    shareTv.text = String.format("测试异步(%d)", value)
                }

                override fun onAccept(value: Long) {
                    shareTv.text = "测试异步(End)"
                }

            }, 1000, 10)
~~~
### 也可以翻转回来，做倒计时9到0，示例
只需要多加一个参数即可
~~~java
QuickASync.async(object : QuickASync.OnIntervalListener<Long> {
                override fun onNext(value: Long) {
                    shareTv.text = String.format("测试异步(%d)", value)
                }

                override fun onAccept(value: Long) {
                    shareTv.text = "测试异步(End)"
                }

            }, 1000, 10,true)
~~~
## QuickBroadcast
快速方便的使用动态广播，告别繁琐的注册与注销广播。<br>
正常写法<br>
##先写一个广播
~~~java
val broadcastRecevier=object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            
        }
    }
~~~
##再根据action注册一个
~~~java 
registerReceiver(broadcastRecevier, IntentFilter("action"))
~~~
##再onDestroy中注销
~~~java
unregisterReceiver(broadcastRecevier)
~~~
##再发送广播
~~~java
sendBroadcast(Intent("test"))
~~~
每个页面都需这样去写，你到底累不累？<br><br>
现在来看看新的写法，只需两步即可实现<br>
## 1、注册
~~~java
QuickBroadcast.addBroadcastListener(绑定者, { action, intent ->
            when (action) {
                "test" -> showToast(intent.getStringExtra("test"))
                "test2" -> showToast(intent.getStringExtra("test"))
            }
        }, "test", "test2")/*天呐，这里居然可以指定多个接受者*/
~~~
## 2、发送广播
~~~java
QuickBroadcast.sendBroadcast(Intent(), "test")
~~~
如此简单的两步就完成了广播的注册与发送，so easy!<br><br>
如果更复杂的使用呢，当前页面发送给多个页面。<br><br>
正常写法
~~~java
sendBroadcast(Intent("test"))
sendBroadcast(Intent("MyCenterFragment"))
~~~
##新写法
~~~java
QuickBroadcast.sendBroadcast(Intent(), "test","MyCenterFragment")
~~~
对于发送只需增加接收者即可，这样注册了test与MyCenterFragment的接收者都将收到你的消息。<br><br>

并且此组件同一时刻不会同时触发接收者为同一监听的广播
~~~java
QuickBroadcast.addBroadcastListener(绑定者, { action, intent ->
            when (action) {
                "test" -> showToast(intent.getStringExtra("test"))
                "test2" -> showToast(intent.getStringExtra("test"))
            }
        }, "test", "test2")/*天呐，这里居然可以指定多个接受者*/
~~~
比如，test,test2为同一组接收者。<br>
如果发送同时发送test,test2
~~~java
QuickBroadcast.sendBroadcast(Intent(), "test","test2")
~~~
那只会触发早先的test，test2将不再触发。<br><br>
此组件需要注意的是注册时需要传递唯一的绑定者。
## QuickStartActivity
以回调的方式管理onActivityForResult的返回值。无需再向传统那样，先startActivityForResult,然后再监听onActivityForResult，真正做到在哪开始，就在哪结束，方便开发者debug，并且无需再为担心requestCode怎么传，会不会有重复的，有没有超出65536上限而担心，因为已经生成了唯一的requestCode,每个Activity对应一个requestCode。<br>
开始调用
~~~java
var intent=Intent(this,YourActivity::class.java)
intent.putExtra("TYPE","this is a type")
QuickStartActivity.startActivity(activity, intent, { resultCode, data ->
                
})
~~~
在这里绑定
~~~java
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        QuickStartActivity.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
~~~

一般来讲，A跳转到B，A中只会有一处跳转到B。如果有特殊需求，A存在多处跳转到B，就像按钮1跳转B，按钮2也要跳转到B，因为每个Activity只对应一个requestCode,所以应该以动态传值的方法来写

~~~java
fun startActionB(intent:Intent){
  QuickStartActivity.startActivity(activity, intent, { resultCode, data ->
    
  })
}
~~~

## QuickToast
可在任意线程弹出，方便的使用Toast，无需在使用的时候频繁传递context，一次初始化，终生受益。<br>
传统使用方式
~~~java
Toast.makeText(activity,"这是一个Toast",Toast.LENGTH_SHORT).show()
~~~
如果需要自定义时
~~~java
val toast = Toast.makeText(activity, "这是一个Toast", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
~~~
这种方式使用时每次都需要依赖context，如果需要在子线程使用，将会变得很不方便，那么此组件如何呢<br>
所以请看
~~~java
QuickToast.showToastDefault("这是一个Toast")
~~~
或者，使用链式配置参数，或者自定义Toast
~~~java
QuickToast.Builder().setGravity(Gravity.CENTER).setDuration(Toast.LENGTH_SHORT).setLayoutView(customView).build().showToast("这是一个Toast")
~~~
看到这里或许就有疑问了，在哪里初始化context呢，使用quickLibrary将无需处理，若只需使用此组件，重写QuickToast中的context方法即可。<br>
示例
~~~java
class CustomToast:QuickToast() {

    override val context: Context
        get() = super.context/*这里替换为自定义context*/
}
~~~
如此就能愉快的玩耍了。
## QuickSPHelper
快速方便使用SharedPreferences
### 链式存入示例
~~~java
QuickSPHelper.putValue("A","valueA").putValue("B",1).putValue("C",true)
~~~
### 取值
键字对方式存入，第二个参数表示默认的数据类型值，如果没有数据或者数据转换错误，将返回默认值。
~~~java
QuickSPHelper.getValue("B",0L)
~~~
## QuickDialog
快速使用Dialog，并且缓存上一个使用的Dialog，不会重复创建View相同的Dialog.
### 使用示例
~~~java
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="40dp"
    android:background="#ffffff">

    <TextView
        android:id="@+id/titleTv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:minHeight="100dp"
        android:text="这是标题"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/leftTv"
        android:layout_width="0dp"
        android:layout_height="50dp"
        android:gravity="center"
        android:text="这是左边按钮"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toLeftOf="@+id/rightTv"
        app:layout_constraintTop_toBottomOf="@+id/titleTv" />

    <TextView
        android:id="@+id/rightTv"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:gravity="center"
        android:text="这是右边按钮"
        app:layout_constraintBottom_toBottomOf="@+id/leftTv"
        app:layout_constraintLeft_toRightOf="@+id/leftTv"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="@+id/leftTv" />
</android.support.constraint.ConstraintLayout>

QuickDialog.Builder(this@MainActivity).setLayout(R.layout.dialog_test).setWindowPadding(100, 0, 100, 0).show().setText(R.id.leftTv, "取消", View.OnClickListener {
                QuickToast.showToastDefault("点击了取消")
                QuickDialog.dismiss()
            }).setText(R.id.rightTv, "确定", View.OnClickListener {
                QuickToast.showToastDefault("点击了确定")
                QuickDialog.dismiss()
            })
~~~
有时一个页面会有多个Dialog，也可以创建Dialog
~~~java
val tempDialog = QuickDialog.Builder(this@MainActivity).setLayout(R.layout.dialog_test).create()
~~~
## QuickNotify
快速方式使用通知，组件提供常用的临时通知，进度条两种通知。同时也可以根据业务自定义通知
### 临时普通通知
不需要点击事件
~~~java
QuickNotify.notifyTempNormal(R.mipmap.ic_launcher,"这是标题","这是内容")
~~~
需要点击事件
~~~java
QuickNotify.notifyTempNormal(R.mipmap.ic_launcher,"这是标题","这是内容",intent) { context, intent ->
            QuickToast.showToastDefault("点击了通知")
        }
~~~
### 临时通知-进度条
准备通知-正在请求数据，进度条处于准备阶段
~~~java
QuickNotify.notifyTempProgress(notifyId,R.mipmap.ic_launcher,"这是标题","这是内容")
~~~
开始通知-进度已经开始
~~~java
QuickNotify.notifyTempProgresses(notifyId, R.mipmap.ic_launcher, "这是标题", "这是内容", value)
~~~
通知结束-文件已下载完成-用户触发点击事件
~~~java
QuickNotify.notifyTempProgressEnd(1, R.mipmap.ic_launcher, "这是标题", "这是内容", intent){ context, intent ->
            QuickToast.showToastDefault("点击了通知")
        }
~~~
### 自定义通知
所有配置数据全由开发者自行配置，组件只负责通知。如果需要接管回调事件，只需将监听传空即可
~~~java
QuickNotify.notify(1, NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("这是标题")
                .setContentText("这是内容")
                .setProgress(100, 100, false)
                .setOngoing(false)/*是否正在通知（是否不可以取消）*/
                .setAutoCancel(true)/*是否点击时取消*/
                .setDefaults(NotificationCompat.DEFAULT_ALL),intentClick,intentCancel) { context, intent ->

        }
~~~




