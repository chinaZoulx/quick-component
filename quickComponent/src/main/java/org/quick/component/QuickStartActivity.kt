package org.quick.component

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import java.io.Serializable

/**
 * @describe 快速简洁的返回startActivityForResult值，以回调的方式使用
 * @author ChrisZou
 * @date 2018/6/14-14:33
 * @from https://github.com/SpringSmell/quick.library
 * @email chrisSpringSmell@gmail.com
 */
object QuickStartActivity {

    private val requestParamsList = SparseArray<((resultCode: Int, data: Intent?) -> Unit)>()

    fun startActivity(activity: Activity?, intent: Intent) {
        startActivity(activity, intent, null)
    }

    fun startActivity(activity: Activity?, intent: Intent, onActivityResultListener: ((resultCode: Int, data: Intent?) -> Unit)?) {
        if (activity == null) return
        if (onActivityResultListener == null)
            activity.startActivity(intent)
        else {
            val requestCode = createRequestCode(intent.component.className)
            requestParamsList.put(requestCode, onActivityResultListener)/*这里是以目的地存储的*/
            activity.startActivityForResult(intent, requestCode)
        }
    }

    fun createRequestCode(className: String): Int {
        val hasCodeStr = className.hashCode().toString()
        var tempCode = ""
        for (index in hasCodeStr.length - 1 downTo 0)
            if (index % 2 != 0)
                tempCode += hasCodeStr[index]
        val requestCode = tempCode.toInt()
        return if (requestCode > 65536) requestCode / 2 else requestCode
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        requestParamsList.get(requestCode)?.invoke(resultCode, data)
    }

    fun remove(activity: Activity?) {
        if (activity == null) return
        requestParamsList.remove(createRequestCode(String.format("%s.%s", activity.packageName, activity.localClassName)))
    }

    fun resetInternal() {
        requestParamsList.clear()
    }

    class Builder(context: Context, clazz: Class<*>) {
        var intent: Intent = Intent(context, clazz)

        fun addParams(key: String, vararg value: String): Builder {
            if (value.size == 1) intent.putExtra(key, value[0]) else intent.putExtra(key, value)
            return this
        }

        fun addParams(key: String, vararg value: Float): Builder {
            if (value.size == 1) intent.putExtra(key, value[0]) else intent.putExtra(key, value)
            return this
        }

        fun addParams(key: String, vararg value: Int): Builder {
            if (value.size == 1) intent.putExtra(key, value[0]) else intent.putExtra(key, value)
            return this
        }

        fun addParams(key: String, vararg value: Double): Builder {
            if (value.size == 1) intent.putExtra(key, value[0]) else intent.putExtra(key, value)
            return this
        }

        fun addParams(key: String, vararg value: Byte): Builder {
            if (value.size == 1) intent.putExtra(key, value[0]) else intent.putExtra(key, value)
            return this
        }

        fun addParams(key: String, vararg value: CharSequence): Builder {
            if (value.size == 1) intent.putExtra(key, value[0]) else intent.putExtra(key, value)
            return this
        }

        fun addParams(key: String, vararg value: Boolean): Builder {
            if (value.size == 1) intent.putExtra(key, value[0]) else intent.putExtra(key, value)
            return this
        }

        fun addParams(key: String, vararg value: Long): Builder {
            if (value.size == 1) intent.putExtra(key, value[0]) else intent.putExtra(key, value)
            return this
        }

        fun addParams(key: String, vararg value: Short): Builder {
            if (value.size == 1) intent.putExtra(key, value[0]) else intent.putExtra(key, value)
            return this
        }

        fun addParams(key: String, value: ArrayList<String>): Builder {
            intent.putExtra(key, value)
            return this
        }

        fun addParams(key: String, value: Bundle): Builder {
            intent.putExtra(key, value)
            return this
        }

        fun addParams(key: String, value: Serializable): Builder {
            intent.putExtra(key, value)
            return this
        }

        fun build() = intent
    }
}
