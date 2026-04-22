package com.example.network

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File


class ProgressRequestBody(
    private val file: File,
    private val contentType: String,
    private val onProgress: (percent: Int) -> Unit
) : RequestBody() {

    override fun contentType() = contentType.toMediaTypeOrNull()

    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {
        val fileLength = file.length()
        val buffer = ByteArray(2048) // Читаем по 2 КБ
        val inputStream = file.inputStream()
        var uploaded = 0L

        inputStream.use { input ->
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                sink.write(buffer, 0, read)
                uploaded += read

                // Вычисляем процент и отправляем в UI
                val progress = ((uploaded * 100) / fileLength).toInt()
                onProgress(progress)
            }
        }
    }
}