package com.example.myapplication

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1.setOnClickListener {
            MainDialog().showNow(supportFragmentManager, "MainDialog")
        }
        button2.setOnClickListener {
            MainDialogWorkaround().showNow(supportFragmentManager, "MainDialogWorkaround")
        }
    }
}


open class MainDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        builder.setView(R.layout.main_dialog)
        return builder.create()
    }
}


class MainDialogWorkaround : MainDialog() {
    private var focusOnStop = View.NO_ID

    override fun onStart() {
        super.onStart()
        focusOnStop = View.NO_ID
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (focusOnStop != View.NO_ID) {
            val hierarchy = outState.getBundle("android:savedDialogState")
                ?.getBundle("android:dialogHierarchy")
            if (hierarchy != null &&
                hierarchy.getInt("android:focusedViewId", View.NO_ID) == View.NO_ID) {
                hierarchy.putInt("android:focusedViewId", focusOnStop)
            }
        }
    }

    override fun onStop() {
        focusOnStop = dialog?.findViewById<View>(android.R.id.content)?.findFocus()?.id
                      ?: View.NO_ID
        super.onStop()
    }
}