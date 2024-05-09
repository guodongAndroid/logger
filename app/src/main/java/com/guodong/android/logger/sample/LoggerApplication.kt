package com.guodong.android.logger.sample

import android.app.Application
import android.os.Build
import com.guodong.android.logger.LogConfiguration
import com.guodong.android.logger.LogLevel
import com.guodong.android.logger.Logger
import com.guodong.android.logger.flattener.ClassicFlattener
import com.guodong.android.logger.interceptor.BlacklistTagsFilterInterceptor
import com.guodong.android.logger.printer.AndroidPrinter
import com.guodong.android.logger.printer.Printer
import com.guodong.android.logger.printer.file.FilePrinter
import com.guodong.android.logger.printer.file.backup.FileSizeBackupStrategy
import com.guodong.android.logger.printer.file.clean.FileLastModifiedCleanStrategy
import com.guodong.android.logger.printer.file.naming.DateFileNameGenerator
import com.guodong.android.logger.printer.file.writer.DefaultWriter
import java.io.File

class LoggerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
    }

    /**
     * Initialize Logger.
     */
    private fun initLogger() {
        val config = LogConfiguration.Builder()
            .level(if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.VERBOSE)
            .tag(getString(R.string.global_tag))
            .addInterceptor(
                BlacklistTagsFilterInterceptor(
                    "blacklist1", "blacklist2", "blacklist3"
                )
            )
            // .addInterceptor(new WhitelistTagsFilterInterceptor( "whitelist1", "whitelist2", "whitelist3"))
            // .addInterceptor(new MyInterceptor())
            .build()

        val androidPrinter: Printer = AndroidPrinter()

        val logDirPath = File(externalCacheDir!!.absolutePath, "log").path
        val filePrinter: Printer = FilePrinter.Builder(logDirPath)
            .fileNameGenerator(DateFileNameGenerator())
            .backupStrategy(FileSizeBackupStrategy(BACKUP_MAX_LENGTH, BACKUP_MAX_INDEX))
            .cleanStrategy(FileLastModifiedCleanStrategy(MAX_TIME))
            .flattener(ClassicFlattener())
            .writer(object : DefaultWriter() {
                override fun onNewFileCreated(file: File) {
                    super.onNewFileCreated(file)
                    val header = """
                     
                     >>>>>>>>>>>>>>>> File Header >>>>>>>>>>>>>>>>
                     Device Manufacturer: ${Build.MANUFACTURER}
                     Device Model       : ${Build.MODEL}
                     Android Version    : ${Build.VERSION.RELEASE}
                     Android SDK        : ${Build.VERSION.SDK_INT}
                     App VersionName    : ${BuildConfig.VERSION_NAME}
                     App VersionCode    : ${BuildConfig.VERSION_CODE}
                     <<<<<<<<<<<<<<<< File Header <<<<<<<<<<<<<<<<
                     """.trimIndent()
                    write(header)
                }
            })
            .build()

        Logger.init(
            config,
            androidPrinter,
            filePrinter
        )
    }

    companion object {
        private const val MAX_TIME = 1000L * 60 * 60 * 24 * 15 // 15 days

        private const val BACKUP_MAX_LENGTH = 1024L * 10 // 10KB
        private const val BACKUP_MAX_INDEX = 10 //
    }
}
