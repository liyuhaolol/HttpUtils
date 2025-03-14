package spa.lyh.cn.lib_https.multirequest;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.MultiRequestCenter;
import spa.lyh.cn.lib_https.exception.OkHttpException;
import spa.lyh.cn.lib_https.log.LyhLog;
import spa.lyh.cn.lib_https.response.base.CommonBase;
import spa.lyh.cn.lib_https.utils.LogUtils;

public class RequestThread extends Thread implements Runnable{

   private MultiCall multiCall;
   volatile boolean shouldLock = true;
   volatile boolean allowFinish = true;
   private Handler handler;
   private Handler questHandler;
   private String mResponse;
   private boolean devMode;
   private long requestTime;
   private Context context;

   public RequestThread(Context context,Handler handler,MultiCall call,boolean devMode){
      this.context = context;
      this.multiCall = call;
      this.questHandler = handler;
      this.handler = new Handler(Looper.getMainLooper());
      this.devMode = devMode;
   }


   @Override
   public void run() {
      super.run();
      try{
         //执行网络请求
         startRequest();
      }catch (Exception e){
         if(multiCall.dataListener != null){
            handler.post(new Runnable() {
               @Override
               public void run() {
                  allowFinish = multiCall.dataListener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR, CommonBase.EMPTY_MSG,e.getMessage()));
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
      Message msg = Message.obtain();
      msg.what = MultiRequestCenter.TASK_STOP_FINISH;
      if (allowFinish){
         msg.arg1 = 1;
      }else {
         msg.arg1 = 0;
      }

      if (questHandler != null){
         questHandler.sendMessage(msg);
      }
      release();
   }

   private void startRequest() throws IOException {
      requestTime = System.currentTimeMillis();
      //执行请求
      mResponse = null;
      Response execute = multiCall.call.execute();

      if (!execute.isSuccessful()){
         //请求失败了
         if (multiCall.dataListener != null){
            handler.post(new Runnable() {
               @Override
               public void run() {
                  allowFinish = multiCall.dataListener.onFailure(new OkHttpException(OkHttpException.NETWORK_ERROR, CommonBase.NET_MSG,null));
                  shouldLock = false;
               }
            });
         }else{
            shouldLock = false;
         }
         return;
      }

      ResponseBody body = execute.body();
      mResponse = body.string();

      if (!TextUtils.isEmpty(mResponse)){
         //是否在控制台打印信息
         if (devMode){
            long finishTime = System.currentTimeMillis();
            long time = finishTime - requestTime;
            LyhLog.e(CommonBase.TAG, LogUtils.makeResponseLog(execute.request().url().toString(),mResponse,time+"ms"));
         }
         if (multiCall.dataListener != null){
            handler.post(new Runnable() {
               @Override
               public void run() {
                  boolean sendToListener = true;
                  if (HttpClient.getInstance(context).getHttpFilter() != null && multiCall.useHttpFilter){
                     sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,execute.request().url().toString(),execute.headers(),mResponse);
                  }
                  if (sendToListener){
                     allowFinish = multiCall.dataListener.onSuccess(execute.headers(),mResponse);
                  }else{
                     allowFinish = false;
                  }
                  shouldLock = false;
               }
            });
         }else{
            shouldLock = false;
         }
      }else {
         if(multiCall.dataListener != null){
            handler.post(new Runnable() {
               @Override
               public void run() {
                  allowFinish = multiCall.dataListener.onFailure(new OkHttpException(OkHttpException.NETWORK_ERROR, CommonBase.EMPTY_MSG,null));
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
      allowFinish = true;
      questHandler = null;
   }
}
