package spa.lyh.cn.lib_https.multirequest;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.exception.OkHttpException;
import spa.lyh.cn.lib_https.log.LyhLog;
import spa.lyh.cn.lib_https.response.base.CommonBase;
import spa.lyh.cn.lib_https.utils.LogUtils;

public class RequestThread extends Thread implements Runnable{

   private MultiCall multiCall;
   volatile boolean shouldLock = true;
   private Handler handler;
   private String mResponse;
   private boolean devMode;
   private long requestTime;
   private Context context;

   public RequestThread(Context context,MultiCall call,boolean devMode){
      this.context = context;
      this.multiCall = call;
      handler = new Handler(Looper.getMainLooper());
      this.devMode = devMode;
   }


   @Override
   public void run() {
      super.run();
      try{
         //执行网络请求
         uploadRequest();
      }catch (Exception e){
         if (multiCall.listener != null){
            handler.post(new Runnable() {
               @Override
               public void run() {
                  multiCall.listener.onFailure(new OkHttpException(OkHttpException.NETWORK_ERROR, CommonBase.NET_MSG));
                  shouldLock = false;
               }
            });
         }else {
            shouldLock = false;
         }
      }
      while (shouldLock){
         //线程阻塞
      }
      release();
   }

   private void uploadRequest()  throws IOException {
      requestTime = System.currentTimeMillis();
      //执行请求
      mResponse = null;
      Response execute = multiCall.call.execute();

      if (!execute.isSuccessful()){
         //请求失败了
         if (multiCall.listener != null){
            handler.post(new Runnable() {
               @Override
               public void run() {
                  multiCall.listener.onFailure(new OkHttpException(OkHttpException.NETWORK_ERROR, CommonBase.NET_MSG));
                  shouldLock = false;
               }
            });
         }else {
            shouldLock = false;
         }
         return;
      }

      ResponseBody body = execute.body();
      mResponse = body.string();

      if (!TextUtils.isEmpty(mResponse)){
         try {

            //是否在控制台打印信息
            if (devMode){
               long finishTime = System.currentTimeMillis();
               long time = finishTime - requestTime;
               LyhLog.e(CommonBase.TAG, LogUtils.makeResponseLog(execute.request().url().toString(),mResponse,time+"ms"));
            }


            //范型为空时，直接回调成功的字符串
            if (multiCall.typeReference == null){
               if (multiCall.listener != null){
                  handler.post(new Runnable() {
                     @Override
                     public void run() {
                        boolean sendToListener = true;
                        if (HttpClient.getInstance(context).getHttpFilter() != null && multiCall.useHttpFilter){
                           sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,execute.request().url().toString(),execute.headers(),mResponse);
                        }
                        if (sendToListener){
                           multiCall.listener.onSuccess(execute.headers(),mResponse);
                        }
                        shouldLock = false;
                     }
                  });
               }else {
                  shouldLock = false;
               }
               return;
            }
            //按照范型解析
            Object realResult = JSONObject.parseObject(mResponse, multiCall.typeReference);

            if (realResult != null){
               if (multiCall.listener != null){
                  handler.post(new Runnable() {
                     @Override
                     public void run() {
                        boolean sendToListener = true;
                        if (HttpClient.getInstance(context).getHttpFilter() != null && multiCall.useHttpFilter){
                           sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,execute.request().url().toString(),execute.headers(),mResponse);
                        }
                        if (sendToListener){
                           multiCall.listener.onSuccess(execute.headers(),realResult);
                        }
                        shouldLock = false;
                     }
                  });
               }else {
                  shouldLock = false;
               }
            }else {
               if (multiCall.listener != null){
                  handler.post(new Runnable() {
                     @Override
                     public void run() {
                        multiCall.listener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR, CommonBase.EMPTY_MSG));
                        shouldLock = false;
                     }
                  });
               }else {
                  shouldLock = false;
               }

            }
         } catch (Exception e) {
            e.printStackTrace();
            if (multiCall.listener != null){
               handler.post(new Runnable() {
                  @Override
                  public void run() {
                     multiCall.listener.onFailure(new OkHttpException(OkHttpException.JSON_ERROR, CommonBase.JSON_MSG_TYPEREFERENCE));
                     shouldLock = false;
                  }
               });
            }else {
               shouldLock = false;
            }
         }
      }else {
         if (multiCall.listener != null){
            handler.post(new Runnable() {
               @Override
               public void run() {
                  multiCall.listener.onFailure(new OkHttpException(OkHttpException.NETWORK_ERROR, CommonBase.EMPTY_MSG));
                  shouldLock = false;
               }
            });
         }else {
            shouldLock = false;
         }
      }
   }

   private void release(){
      handler = null;
      mResponse = null;
      shouldLock = true;
      multiCall = null;
      devMode = false;
      context = null;
   }
}
