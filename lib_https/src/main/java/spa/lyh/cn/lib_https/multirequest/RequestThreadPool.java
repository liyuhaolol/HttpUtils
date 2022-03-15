package spa.lyh.cn.lib_https.multirequest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import spa.lyh.cn.lib_https.MultiRequestCenter;

public class RequestThreadPool extends Thread{
    private ExecutorService service;
    private Handler handler;
    private int threads;
    private int mode;
    private boolean devMode;

    private List<MultiCall> listCall;

    public RequestThreadPool(Handler handler, int threads,List<MultiCall> listCall,boolean devMode){
        this.handler = handler;
        this.threads = threads;
        this.listCall = new ArrayList<>();
        this.listCall.addAll(listCall);
        this.devMode = devMode;
    }

    @Override
    public void run() {
        super.run();
        service = Executors.newFixedThreadPool(threads);
        for (int i = 0;i < listCall.size(); i++){
            service.execute(new RequestThread(listCall.get(i),devMode));
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
    }
}
