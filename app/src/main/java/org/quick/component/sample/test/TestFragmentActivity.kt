package org.quick.component.sample.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.quick.component.sample.R

class TestFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_test_fragment)
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().add(R.id.container, TestFragment()).commitAllowingStateLoss()
    }
}