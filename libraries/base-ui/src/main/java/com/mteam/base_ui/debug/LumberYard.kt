package com.mteam.base_ui.debug

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import okio.BufferedSink
import okio.buffer
import okio.sink
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayDeque
import java.util.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LumberYard constructor(private val app: Application) {

    private val entries = ArrayDeque<Entry>(BUFFER_SIZE + 1)
    private val entryChannel = BroadcastChannel<Entry>(Channel.BUFFERED)

    fun tree(): Timber.Tree {
        return object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                addEntry(Entry(priority, tag, message))
            }
        }
    }

    @Synchronized
    private fun addEntry(entry: Entry) {
        entries.addLast(entry)
        if (entries.size > BUFFER_SIZE) {
            entries.removeFirst()
        }

        entryChannel.safeOffer(entry)
    }

    fun bufferedLogs() = ArrayList(entries)

    fun logs(): Flow<Entry> = entryChannel.asFlow()

    /**
     * Save the current logs to disk.
     */
    @SuppressLint("NewApi")
    suspend fun save(): File = suspendCancellableCoroutine { continuation ->
        val folder = app.getExternalFilesDir(null)
        if (folder == null) {
            continuation.resumeWithException(IOException("External storage is not mounted."))
        } else {
            val fileName = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())
            val output = File(folder, fileName)

            var sink: BufferedSink? = null
            try {
                sink = output.sink().buffer()
                val entries1 = bufferedLogs()
                for (entry in entries1) {
                    sink.writeUtf8(entry.prettyPrint()).writeByte('\n'.toInt())
                }
                // need to close before emiting file to the subscriber, because when subscriber receives
                // data in the same thread the file may be truncated
                sink.close()
                sink = null

                continuation.resume(output)
            } catch (e: IOException) {
                continuation.resumeWithException(e)
            } finally {
                if (sink != null) {
                    try {
                        sink.close()
                    } catch (e: IOException) {
                        continuation.resumeWithException(e)
                    }
                }
            }
        }
    }

    /**
     * Delete all of the log files saved to disk. Be careful not to call this before any intents have
     * finished using the file reference.
     */
    @WorkerThread
    fun cleanUp(): Long {
        return app.getExternalFilesDir(null)?.let { folder ->
            val initialSize = folder.length()
            (folder.listFiles() ?: return -1L)
                .asSequence()
                .filter { it.name.endsWith(".log") }
                .forEach { it.delete() }
            return@let initialSize - folder.length()
        } ?: -1L
    }

    data class Entry(val level: Int, val tag: String?, val message: String) {
        fun prettyPrint(): String {
            return String.format(
                "%22s %s %s", tag ?: "CATCHUP", displayLevel(),
                // Indent newlines to match the original indentation.
                message.replace("\\n".toRegex(), "\n                         ")
            )
        }

        fun displayLevel() = when (level) {
            Log.VERBOSE -> "V"
            Log.DEBUG -> "D"
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            Log.ASSERT -> "A"
            else -> "?"
        }
    }

    companion object {
        private const val BUFFER_SIZE = 200
    }

    fun <E> SendChannel<E>.safeOffer(value: E) = !isClosedForSend && try {
        offer(value)
    } catch (t: Throwable) {
        // Ignore all
        false
    }

}