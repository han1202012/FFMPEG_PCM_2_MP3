package com.example.ffmpeg_pcm_2_mp3

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import java.io.File

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    /**
     * 应用内置存储下的 files 目录
     */
    lateinit var mFilePath: String

    lateinit var ffmpeg: FFmpeg

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFilePath = this.filesDir.toString()

        // 初始化 FFMPEG
        ffmpeg = FFmpeg.getInstance(this)

        // 加载 FFMPEG 可执行文件
        ffmpeg.loadBinary(LoadBinaryResponseHandler())
    }

    /**
     * 拷贝文件
     */
    fun copy(view: View) {
        // 将 assets 中的 audio.pcm 拷贝到内置存储中
        CommandUtils.copyAssets2File(
            this,
            "audio.pcm",
            "${mFilePath}/audio.pcm")

        showText()
    }

    /**
     * 执行混音命令
     */
    fun mix(view: View) {
        var cmd = "-y -f s16be -ac 2 -ar 48000 -acodec pcm_s16le -i ${mFilePath}/audio.pcm ${mFilePath}/audio.mp3"

        Log.i(TAG, "执行命令 : $cmd")

        var cmdArraay = cmd.split(" ").toTypedArray();
        ffmpeg.execute(cmdArraay, object : ExecuteBinaryResponseHandler(){
            override fun onStart() {
                super.onStart()
                Log.i(TAG, "onStart")
            }

            override fun onFinish() {
                super.onFinish()
                Log.i(TAG, "onStart")
                showText()
            }

            override fun onSuccess(message: String?) {
                super.onSuccess(message)
                Log.i(TAG, "onSuccess : $message")
            }

            override fun onProgress(message: String?) {
                super.onProgress(message)
                Log.i(TAG, "onProgress : $message")
            }

            override fun onFailure(message: String?) {
                super.onFailure(message)
                Log.i(TAG, "onFailure : $message")
            }
        })
    }

    /**
     * 显示内置存储目录
     */
    fun showText(){
        var fileString = ""
        var files = File(mFilePath).listFiles()
        files.forEach {
            fileString += "${it}\n"
        }
        findViewById<TextView>(R.id.text).text = fileString
    }
}