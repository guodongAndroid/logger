package com.guodong.android.logger.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guodong.android.logger.Log
import com.guodong.android.logger.LogLevel
import com.guodong.android.logger.flattener.Flattener
import com.guodong.android.logger.flattener.PatternFlattener
import com.guodong.android.logger.printer.Printer
import com.guodong.android.logger.sample.databinding.ItemLogBinding

class RecyclerViewPrinter(private val recyclerView: RecyclerView) : Printer {

    private val adapter =
        LogAdapter(LayoutInflater.from(recyclerView.context))

    init {
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun println(log: Log) {
        if (isMainThread()) {
            doPrintln(log)
        } else {
            recyclerView.post {
                doPrintln(log)
            }
        }
    }

    private fun doPrintln(log: Log) {
        adapter.addLog(log)
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    private class LogAdapter(private val inflater: LayoutInflater) :
        RecyclerView.Adapter<LogViewHolder>() {
        private val logs: MutableList<Log> = ArrayList()

        fun addLog(logItem: Log) {
            logs.add(logItem)
            notifyItemInserted(logs.size - 1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
            return LogViewHolder(ItemLogBinding.inflate(inflater))
        }

        override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
            val log = logs[position]
            holder.bind(log)
        }

        override fun getItemCount(): Int {
            return logs.size
        }
    }

    private class LogViewHolder(private val binding: ItemLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            private val flattener: Flattener = PatternFlattener("{d} {l}/{t}: ")
        }

        fun bind(log: Log) {
            val color = getHighlightColor(log.level)
            binding.label.setTextColor(color)
            binding.message.setTextColor(color)

            binding.label.text = flattener.flatten(log)
            binding.message.text = log.formattedMessage
        }

        private fun getHighlightColor(logLevel: Int): Int {
            val highlightColor = when (logLevel) {
                LogLevel.VERBOSE -> -0x444445
                LogLevel.DEBUG -> -0x1
                LogLevel.INFO -> -0x9578a7
                LogLevel.WARN -> -0x444ad7
                LogLevel.ERROR -> -0x9498
                else -> -0x100
            }
            return highlightColor
        }
    }
}
