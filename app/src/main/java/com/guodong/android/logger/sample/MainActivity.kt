package com.guodong.android.logger.sample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.guodong.android.logger.LogLevel
import com.guodong.android.logger.Logger
import com.guodong.android.logger.printer.AndroidPrinter
import com.guodong.android.logger.sample.databinding.ActivityMainBinding
import com.guodong.android.logger.sample.databinding.DialogChangeTagBinding
import kotlin.concurrent.thread

/**
 * Created by john.wick on 2024/5/7 10:49.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val MESSAGE = "Simple message"

        private val LEVELS = intArrayOf(
            LogLevel.VERBOSE, LogLevel.DEBUG, LogLevel.INFO,
            LogLevel.WARN, LogLevel.ERROR, LogLevel.ASSERT
        )

        private val STACK_TRACE_DEPTHS = intArrayOf(0, 1, 2, 3, 4, 5)

        private const val PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 1
    }

    private var hasPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.initView()
    }

    override fun onResume() {
        super.onResume()
        hasPermission = hasPermission()
        val message = if (hasPermission) {
            "Permission granted. Log to file."
        } else {
            "Permission not granted. Can not log to file."
        }
        Logger.i(message)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                hasPermission = (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                if (!hasPermission) {
                    if (shouldShowRequestPermissionRationale()) {
                        showPermissionRequestDialog(false)
                    } else {
                        showPermissionRequestDialog(true)
                    }
                }
            }
        }
    }

    private fun ActivityMainBinding.initView() {
        tag.setOnClickListener {
            showChangeTagDialog()
        }

        threadInfo.setOnClickListener {
            threadInfo.toggle()
        }

        stackTraceInfo.setOnClickListener {
            stackTraceInfo.toggle()
            setEnabledStateOnViews(stackTraceDepthContainer, stackTraceInfo.isChecked)
        }

        setEnabledStateOnViews(stackTraceDepthContainer, false)

        border.setOnClickListener {
            border.toggle()
        }

        print.setOnClickListener {
            printLog()

            thread {
                printLog()
            }
        }

        Logger.addPrinter(RecyclerViewPrinter(logContainer))

        Logger.i("Logger is ready. Print your log now!")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasPermission = hasPermission()
            if (!hasPermission) {
                if (shouldShowRequestPermissionRationale()) {
                    showPermissionRequestDialog(false)
                } else {
                    requestPermission()
                }
            }
        }
    }

    private fun hasPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun shouldShowRequestPermissionRationale(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSIONS_REQUEST_EXTERNAL_STORAGE
        )
    }

    private fun showPermissionRequestDialog(gotoSettings: Boolean) {
        AlertDialog.Builder(this)
            .setTitle(R.string.permission_request)
            .setMessage(R.string.permission_explanation)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(
                if (gotoSettings) R.string.go_to_settings else R.string.allow
            ) { _, _ ->
                if (gotoSettings) {
                    startAppSettings()
                } else {
                    requestPermission()
                }
            }
            .show()
    }

    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.setData(Uri.parse("package:$packageName"))
        startActivity(intent)
    }

    private fun ActivityMainBinding.printLog() {
        val builder = Logger.newBuilder()

        builder.withThread(threadInfo.isChecked)
            .withBorder(border.isChecked)
            .withStackTrace(stackTraceInfo.isChecked)

        if (stackTraceInfo.isChecked) {
            builder.stackTraceDepth(STACK_TRACE_DEPTHS[stackTraceDepth.selectedItemPosition])
        }

        val tag = tag.text.toString()

        val level = LEVELS[level.selectedItemPosition]
        when (level) {
            LogLevel.VERBOSE -> Logger.v(tag, MESSAGE)
            LogLevel.DEBUG -> Logger.d(tag, MESSAGE)
            LogLevel.INFO -> Logger.i(tag, MESSAGE)
            LogLevel.WARN -> Logger.w(tag, MESSAGE)
            LogLevel.ERROR -> Logger.e(tag, MESSAGE)
            LogLevel.ASSERT -> Logger.a(tag, MESSAGE)
        }
    }

    private fun ActivityMainBinding.showChangeTagDialog() {
        val binding = DialogChangeTagBinding.inflate(layoutInflater)
        binding.tag.setText(tag.getText())

        AlertDialog.Builder(root.context)
            .setTitle(R.string.change_tag)
            .setView(binding.root)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(
                android.R.string.ok
            ) { _, _ ->
                val tag = binding.tag.text.toString().trim { it <= ' ' }
                if (tag.isNotEmpty()) {
                    this.tag.text = tag
                }
            }
            .show()
    }

    private fun setEnabledStateOnViews(v: View, enabled: Boolean) {
        v.isEnabled = enabled

        if (v is ViewGroup) {
            for (i in v.childCount - 1 downTo 0) {
                setEnabledStateOnViews(v.getChildAt(i), enabled)
            }
        }
    }
}