package spa.lyh.cn.lib_https.multirequest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.MultiRequestCenter;

public class RequestDownloadThreadPool extends Thread{
    private ExecutorService service;
    private Handler handler;
    private int threads;
    private int mode;
    private boolean devMode;
    private Context context;
    private int mod;
    private String mFilePath;

    private List<MultiDownloadCall> listCall;

    public RequestDownloadThreadPool(Context context,Handler handler, int threads,List<MultiDownloadCall> listCall,boolean devMode,int mod,String filePath){
        this.handler = handler;
        this.threads = threads;
        this.listCall = new ArrayList<>();
        this.listCall.addAll(listCall);
        this.devMode = devMode;
        this.context = context;
        this.mod = mod;
        this.mFilePath = filePath;
    }

    @Override
    public void run() {
        super.run();
        service = Executors.newFixedThreadPool(threads);
        for (int i = 0;i < listCall.size(); i++){
            service.execute(new RequestDownloadThread(context,handler,listCall.get(i),devMode,mod,mFilePath));
        }
        service.shutdown();
        while (!service.isTerminated()){
            //线程阻塞
        }
        Message msg = Message.obtain();
        if (mode == MultiRequestCenter.TASK_CANCAL){
            //取消
            msg.what = MultiRequestCenter.TASK_CANCAL;
        }else {
            //执行完毕
            msg.what = MultiRequestCenter.TASK_SUCCESS;
        }

        if (handler != null){
            handler.sendMessage(msg);
        }
        release();
    }

    public void stopPoolThread(int mode){
        if (service != null && !service.isTerminated()){
            if (mode == MultiRequestCenter.TASK_SUCCESS){
                this.mode = MultiRequestCenter.TASK_SUCCESS;
            }else if (mode == MultiRequestCenter.TASK_CANCAL){
                this.mode = MultiRequestCenter.TASK_CANCAL;
            }else {
                Log.e(MultiRequestCenter.TAG,"停止线程池参数不对，无法停止任务");
                return;
            }
            service.shutdownNow();
        }
    }

    private void release(){
        service = null;
        handler = null;
        mode = 0;
        listCall.clear();
        context = null;
        mod = HttpClient.OVERWRITE_FIRST;
        mFilePath = null;
    }
}
