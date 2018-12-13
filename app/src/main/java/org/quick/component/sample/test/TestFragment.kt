package org.quick.component.sample.test

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.quick.component.Log2
import org.quick.component.sample.R

class TestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test, null)
    }

    override fun onAttach(context: Context?) {
        Log2.e("onAttach")
        super.onAttach(context)
    }

    override fun onResume() {
        Log2.e("onResume")
        super.onResume()
    }

    override fun onPause() {
        Log2.e("onPause")
        super.onPause()
    }

    override fun onDestroy() {
        Log2.e("onDestroy")
        super.onDestroy()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log2.e("onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        Log2.e("onAttachFragment")
        super.onAttachFragment(childFragment)
    }

    override fun onDestroyView() {
        Log2.e("onDestroyView")
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log2.e("onCreate")
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log2.e("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
        Log2.e("onDetach")
        super.onDetach()
    }

    override fun onStart() {
        Log2.e("onStart")
        super.onStart()
    }

    override fun onStop() {
        Log2.e("onStop")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log2.e("onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }
}