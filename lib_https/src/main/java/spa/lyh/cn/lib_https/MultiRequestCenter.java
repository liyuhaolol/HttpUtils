package spa.lyh.cn.lib_https;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import spa.lyh.cn.lib_https.listener.RequestResultListener;
import spa.lyh.cn.lib_https.multirequest.MultiCall;
import spa.lyh.cn.lib_https.multirequest.RequestThreadPool;

public class MultiRequestCenter {
   public final static String TAG = "MultiRequestCenter";
   public final static int TASK_SUCCESS = 2000;//任务完成
   public final static int TASK_CANCAL = 2001;//任务取消

   private int number = 5;//线程并发数

   private List<MultiCall> requestList;

   private String errorTag;

   private RequestThreadPool pool;

   private boolean devMode;

   private RequestResultListener listener;

   private Context context;

   private final Handler handler = new Handler(new Handler.Callback() {
      @Override
      public boolean handleMessage(@NonNull Message msg) {
         switch (msg.what){
            case TASK_SUCCESS:
               if (listener != null){
                  new Handler(Looper.getMainLooper()).post(new Runnable() {
                     @Override
                     public void run() {
                        listener.onFinish();
                        release();
                     }
                  });
               }
               break;
            case TASK_CANCAL:
               if (listener != null){
                  new Handler(Looper.getMainLooper()).post(new Runnable() {
                     @Override
                     public void run() {
                        listener.onCancel();
                        release();
                     }
                  });
               }
               break;
         }
         return true;
      }
   });

   public static MultiRequestCenter get(Context context){
      return new MultiRequestCenter(context);
   }

   public MultiRequestCenter(Context context){
      requestList = new ArrayList<>();
      this.context = context;
   }

   public MultiRequestCenter setDevMode(boolean devMode){
      this.devMode = devMode;
      return this;
   }

   public MultiRequestCenter setThreadNumber(int number){
      this.number = number;
      return this;
   }

   public MultiRequestCenter addRequest(MultiCall multiCall){
      if (canAddRequest()){
         requestList.add(multiCall);
      }else {
         Log.e(TAG,"无法添加请求");
      }
      return this;
   }

   public MultiRequestCenter addRequests(List<MultiCall> listCall){
      if (canAddRequest()){
         requestList.addAll(listCall);
      }else {
         Log.e(TAG,"无法添加请求");
      }
      return this;
   }

   public void startTasks(RequestResultListener listener){
      this.listener = listener;
      if (checkCanStart()){
         if (pool == null){
            pool = new RequestThreadPool(context,handler,number,requestList,devMode);
            pool.start();
         }else {
            if (!pool.isAlive()){
               pool = new RequestThreadPool(context,handler,number,requestList,devMode);
               pool.start();
            }else {
               Log.e(TAG,"任务正在进行，请不要重复启动");
            }
         }
      }else {
         Log.e(TAG,"存在一个参数未能初始化或不正确:"+errorTag);
      }
   }

   private void stopTasks(){
      if (pool != null){
         pool.stopPoolThread(TASK_SUCCESS);
      }
   }

   public void cancalTasks(){
      if (pool != null){
         pool.stopPoolThread(TASK_CANCAL);
      }
   }

   private boolean canAddRequest(){
      if (pool != null){
         if (pool.isAlive()){
            return false;
         }else {
            return true;
         }
      }else {
         return true;
      }
   }

   private boolean checkCanStart(){
      if (requestList.size() == 0){
         errorTag = "请求池为空";
         return false;
      }
      if (number < 1){
         errorTag = "number线程并发数小于1";
         return false;
      }
      return true;
   }

   private void release(){
      //释放掉所有资源
      errorTag = null;

      requestList.clear();

      pool = null;//线程池

      number = 5;//线程并发数

      devMode = false;

      listener = null;

      context = null;

   }
}
