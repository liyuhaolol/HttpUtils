package spa.lyh.cn.httputils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.fastjson2.JSONObject
import okhttp3.Headers
import spa.lyh.cn.lib_https.exception.OkHttpException
import spa.lyh.cn.lib_https.listener.DisposeDownloadListener
import spa.lyh.cn.lib_https.listener.DisposeJsonListener

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RequestCenter.getNewVersion(this,object : DisposeJsonListener{
            override fun onSuccess(
                headerData: Headers,
                jsonObject: JSONObject
            ) {
            }

            override fun onFailure(error: OkHttpException) {
            }
        })

        RequestCenter.downloadFile(this,
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3208238474,2536510412&fm=26&gp=0.jpg",
            externalCacheDir!!.path,
            object : DisposeDownloadListener{
                override fun onSuccess(filePath: String, fileName: String) {
                }

                override fun onFailure(error: OkHttpException) {
                }

                override fun onProgress(
                    haveFileSize: Boolean,
                    progress: Int,
                    currentSize: String,
                    sumSize: String
                ) {
                }
            })
    }


}