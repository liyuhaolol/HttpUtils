package spa.lyh.cn.lib_https.multirequest;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import okhttp3.Response;
import okhttp3.ResponseBody;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.MultiRequestCenter;
import spa.lyh.cn.lib_https.exception.OkHttpException;
import spa.lyh.cn.lib_https.log.LyhLog;
import spa.lyh.cn.lib_https.model.Progress;
import spa.lyh.cn.lib_https.model.Success;
import spa.lyh.cn.lib_https.response.base.CommonBase;
import spa.lyh.cn.lib_https.utils.LogUtils;
import spa.lyh.cn.utils_io.IOUtils;
import spa.lyh.cn.utils_io.model.FileData;

public class RequestDownloadThread extends Thread implements Runnable{
    private MultiDownloadCall multiCall;
    volatile boolean shouldLock = true;
    volatile boolean allowFinish = true;
    private Handler mDeliveryHandler;
    private Handler questHandler;
    private boolean devMode;
    private long requestTime;
    private Context context;
    private int mod;
    private String mFilePath;

    private static final int PROGRESS_MESSAGE = 0x01;
    private static final int SUCCESS_MESSAGE = 0x02;
    private static final int FAILURE_MESSAGE = 0x03;
    public static final String TAG = "HttpUtils";
    /**
     * 默认的错误文件代替名
     */
    private static final String DEAFULT_FILE_NAME = "default";

    public RequestDownloadThread(Context context,Handler handler,MultiDownloadCall call,boolean devMode,int mod,String filePath){
        this.context = context;
        this.multiCall = call;
        this.questHandler = handler;
        this.mod = mod;
        this.mFilePath = filePath;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PROGRESS_MESSAGE:
                        Progress p = (Progress) msg.obj;
                        if (multiCall.downloadListener != null){
                            multiCall.downloadListener.onProgress(p.haveFileSize(),p.getProgress(),p.getCurrentSize(),p.getSumSize());
                        }
                        break;
                    case SUCCESS_MESSAGE:
                        Success success = (Success) msg.obj;
                        if (devMode){
                            long finishTime = System.currentTimeMillis();
                            long time = finishTime - requestTime;
                            Log.e(TAG,"下载时长:"+time+"ms");
                            Log.e(TAG,"文件名:"+success.getFileName());
                            Log.e(TAG,"文件路径:"+success.getFilePath());
                        }
                        if (multiCall.downloadListener != null){
                            allowFinish = multiCall.downloadListener.onSuccess(success.getFilePath(),success.getFileName());
                        }
                        shouldLock = false;
                        break;
                    case FAILURE_MESSAGE:
                        OkHttpException e = (OkHttpException) msg.obj;
                        if (multiCall.downloadListener != null){
                            allowFinish = multiCall.downloadListener.onFailure(e);
                        }
                        shouldLock = false;
                        break;
                }
            }
        };
        this.devMode = devMode;
    }


    @Override
    public void run() {
        super.run();
        try{
            //执行网络请求
            startRequest();
        }catch (Exception e){
            mDeliveryHandler.obtainMessage(FAILURE_MESSAGE, new OkHttpException(OkHttpException.OTHER_ERROR, CommonBase.EMPTY_MSG,e.getMessage())).sendToTarget();
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
        Response execute = multiCall.call.execute();

        if (!execute.isSuccessful()){
            //请求失败了
            mDeliveryHandler.obtainMessage(FAILURE_MESSAGE, new OkHttpException(OkHttpException.NETWORK_ERROR, CommonBase.NET_MSG,null)).sendToTarget();
            return;
        }

        String filename = getFileName(execute);

        int length;//每一块的长度
        byte[] buffer = new byte[2048];//每一段的长度
        InputStream inputStream = null;
        FileOutputStream fos = null;


        int currentLength = 0;//当前已经下载的大小
        int lastSize = -1;
        long sumLength;
        float mProgress;
        FileData data;

        Progress p;
        try {
            inputStream  = execute.body().byteStream();//输入流
            data = IOUtils.createFileOutputStream(context,mFilePath,filename,mod);
            if (data == null || data.getFos() == null){
                mDeliveryHandler.obtainMessage(FAILURE_MESSAGE, new OkHttpException(OkHttpException.OTHER_ERROR, CommonBase.EMPTY_RESPONSE,null)).sendToTarget();
                return;
            }
            fos = data.getFos();
            sumLength = execute.body().contentLength();//文件总大小


            if (sumLength > 0){
                //这里应该发送一下总大小，已经进度为0
                p = new Progress(true,0,convertFileSize(0),convertFileSize(sumLength));
                if (devMode){
                    Log.e(TAG,p.getProgress()+"%   "+p.getCurrentSize()+"/"+p.getSumSize());
                }

                mDeliveryHandler.obtainMessage(PROGRESS_MESSAGE, p).sendToTarget();
                while ((length = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                    currentLength += length;
                    //计算百分比
                    mProgress = getNumber((float)currentLength/(float)sumLength)*100;
                    int currentSize = (int) mProgress;
                    //判断整型进度去重只传100次0到100
                    if (currentSize != lastSize){
                        lastSize = currentSize;
                        p = new Progress(true,lastSize,convertFileSize(currentLength),convertFileSize(sumLength));
                        if (devMode){
                            Log.e(TAG,p.getProgress()+"%   "+p.getCurrentSize()+"/"+p.getSumSize());
                        }
                        mDeliveryHandler.obtainMessage(PROGRESS_MESSAGE, p).sendToTarget();
                    }
                }
                fos.flush();
            }else {
                //无法获取到对应的文件总长度
                p = new Progress(false,0,convertFileSize(0),"");
                if (devMode){
                    Log.e(TAG,"无法获取到文件流长度");
                    Log.e(TAG,"已下载"+p.getCurrentSize());
                }

                mDeliveryHandler.obtainMessage(PROGRESS_MESSAGE, p).sendToTarget();
                while ((length = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                    currentLength += length;

                    p = new Progress(false,0,convertFileSize(currentLength),"");
                    if (devMode){
                        Log.e(TAG,"已下载"+p.getCurrentSize());
                    }
                    mDeliveryHandler.obtainMessage(PROGRESS_MESSAGE, p).sendToTarget();
                }
                fos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mDeliveryHandler.obtainMessage(FAILURE_MESSAGE, new OkHttpException(OkHttpException.IO_ERROR, CommonBase.IO_NET_MSG,e.getMessage())).sendToTarget();
            return;
        } finally {
            try {
                if (fos != null){
                    fos.close();
                }
                if (inputStream != null) {

                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Success success = new Success(data.getFilePath(),data.getFileName());
        mDeliveryHandler.sendMessageDelayed(mDeliveryHandler.obtainMessage(SUCCESS_MESSAGE,success),50);
        if (!data.getFilePath().startsWith(Environment.getExternalStorageDirectory().getPath() + "/Android")){
            IOUtils.sendSystemScanBroadcast(context,data.getFilePath());
        }
    }

    private void release(){
        mDeliveryHandler = null;
        shouldLock = true;
        multiCall = null;
        devMode = false;
        context = null;
        allowFinish = true;
        questHandler = null;
        mod = HttpClient.OVERWRITE_FIRST;
        mFilePath = null;
    }

    /**
     * 得到文件名
     * @param response response获取文件名
     * @return 文件名
     */
    private String getFileName(Response response){
        boolean flag = false;//判断是否截取url文件名
        String filename = DEAFULT_FILE_NAME;
        String content = response.header("Content-Disposition");
        if (content != null){
            int startindex = content.indexOf("filename=");
            if (startindex != -1){
                String frontPart = content.substring(startindex+9);
                int endindex = frontPart.indexOf(";");
                if (endindex != -1){
                    filename = frontPart.substring(0,endindex);
                }else {
                    filename = frontPart;
                }
            }else {
                flag = true;
            }

        }else {
            flag = true;
        }

        if (flag){
            String url = response.request().url().toString();
            filename = url.substring(url.lastIndexOf("/")+1);
        }
        return getRealFileName(filename);
    }

    /**
     * 得到正确的文件名
     * @param fileName 文件名
     * @return 返回正确的文件名
     */
    private String getRealFileName(String fileName){
        if (fileName.equals("")){
            return DEAFULT_FILE_NAME;
        }


        String front;
        String behind = "";
        int  spot = fileName.lastIndexOf(".");
        if (spot == 0){
            //点在首位
            front = DEAFULT_FILE_NAME;
            behind = fileName.substring(spot);

        }else if (spot == (fileName.length()-1)){
            //点在末尾
            front = fileName.substring(0,spot);
        }else if (spot != -1){
            //点在中间
            front = fileName.substring(0,spot);
            behind = fileName.substring(spot);
        }else {
            //不存在点
            front = fileName;
        }
        //如果只有.则去除后缀
        if(behind.equals(".")){
            behind = "";
        }
        //已经将文件名按照.分为2段,分别验证是否合法
        front = syncFileName(front,false);
        behind = syncFileName(behind,true);

        return front+behind;
    }

    /**
     * 分析输入字符串是否符合文件命名规则
     * @param contnet 被分析内容
     * @param isSuffix 是否为后缀
     * @return 返回分析结果
     */
    private String syncFileName(String contnet,boolean isSuffix){
        //取得内容
        String syncContent = contnet;
        //判空,因为name有初值，所以这里判断是否存在后缀，没有就没有
        if (syncContent.equals("")){
            return syncContent;
        }

        //
        int flag = contnet.length();

        String[] patterns = new String[]{"\\","/",":","*","?","\"","<",">","|"};

        for (String pattern:patterns){
            int index = contnet.indexOf(pattern);
            if (index != -1){
                if (index < flag){
                    flag = index;
                }
            }
        }
        //取得最近非法字符index
        if (isSuffix){
            //当为后缀时,flag必然不为0
            if (flag == 1){
                //点后第一位就是非法字符
                return "";
            }
        }else {
            //当为name时
            if (flag == 0){
                //name第一位就是非法字符
                return DEAFULT_FILE_NAME;
            }
        }

        if (flag < contnet.length()){
            syncContent = syncContent.substring(0,flag);
        }

        return syncContent;

    }

    /**
     * 计算文件大小<P/>
     * Created by liyuhao on 2016/3/24.<P/>
     * @param size 字节数
     * @return 对应的G，M，K
     */
    public String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.2f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.2f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.2f KB", f);
        } else
            return String.format("%d B", size);
    }

    /**
     * 得到对应位数小数<P/>
     * Created by liyuhao on 2016/3/24.<P/>
     * @param number float的数
     * @return float的数
     */
    public float getNumber(float number){
        DecimalFormat df = new DecimalFormat("#.##############");
        float f=Float.valueOf(df.format(number));
        return f;
    }
}
