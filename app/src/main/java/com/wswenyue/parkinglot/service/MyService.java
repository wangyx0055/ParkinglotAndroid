package com.wswenyue.parkinglot.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.wswenyue.parkinglot.constant.Constant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class MyService extends Service implements Runnable {
    private Socket socket;

    private BufferedReader br;//

    private OutputStream os;

    private Binder binder;

    private Thread td;// 线程，获取服务器端发送来的消息

    private String workStatus;// 当前工作状况，null表示正在处理，success表示处理成功，failure表示处理失败

    static public Handler revHandler;

    /**
     * 向服务器发送请求
     *
     * @param msg
     */
    public void sendRequest(String msg) {
        try {
            workStatus = null;
            sendMessage(msg);
        } catch (Exception ex) {
            workStatus = Constant.TAG_FAILURE;
            ex.printStackTrace();
        }
    }

    /**
     * 返回当前workStatus的值
     */

    public String getWorkStatus() {
        return workStatus;
    }

    /**
     * 处理服务器端反馈的数据
     *
     * @param revMsg
     */
    private void dealUploadSuperviseTask(String revMsg) {
        //TODO 分析 revMsg
        Log.i("REVMsg:", revMsg);
        Intent intent = new Intent();
        intent.setAction(Constant.BroadCastSend);
        intent.putExtra("msg", revMsg);
        sendBroadcast(intent);
    }

    /**
     * 退出程序时，关闭Socket连接
     */
    public void closeConnection() {

        try {
            sendMessage("bye");// 向服务器端发送断开连接请求
            Log.v("qlq", "the request is bye ");
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    /**
     * 连接服务器
     */
    private void connectService() {
        try {
//            socket = new Socket();
//            SocketAddress socAddress = new InetSocketAddress(Constant.SERVER_IP, Constant.SERVER_PORT);
//            socket.connect(socAddress, 3000);
//
//            reader = new BufferedReader(new InputStreamReader(
//                    socket.getInputStream(), "UTF-8"));
//
//            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
//                    socket.getOutputStream(), "UTF-8")), true);
            System.out.println("开始连接");
            socket = new Socket();
            SocketAddress socAddress = new InetSocketAddress(Constant.SERVER_IP, Constant.SERVER_PORT);
            socket.connect(socAddress, 3000);
//            socket = new Socket(Constant.SERVER_IP,Constant.SERVER_PORT);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = socket.getOutputStream();

        } catch (SocketException ex) {
            Log.v("QLQ", "socketException ");
            ex.printStackTrace();
            workStatus = Constant.TAG_CONNECTFAILURE;// 如果是网络连接出错了，则提示网络连接错误
            return;
        } catch (SocketTimeoutException ex) {
            ex.printStackTrace();
            workStatus = Constant.TAG_CONNECTFAILURE;// 如果是网络连接出错了，则提示网络连接错误
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            workStatus = Constant.TAG_CONNECTFAILURE;// 如果是网络连接出错了，则提示网络连接错误
            return;
        }
    }

    /**
     * 向服务器发送传入的JSON数据信息
     *
     * @param
     */
    private void sendMessage(String msg) {
        if (!isNetworkConnected())// 如果当前网络连接不可用,直接提示网络连接不可用，并退出执行。
        {
            Log.v("QLQ", "workStatus is not connected!111");
            workStatus = Constant.TAG_CONNECTFAILURE;
//            return;
        }
        if (socket == null)// 如果未连接到服务器，创建连接
            connectService();
        if (!MyService.this.td.isAlive())// 如果当前线程没有处于存活状态，重启线程
            (td = new Thread(MyService.this)).start();
        if (!socket.isConnected() || (socket.isClosed())) // isConnected（）返回的是是否曾经连接过，isClosed()返回是否处于关闭状态，只有当isConnected（）返回true，isClosed（）返回false的时候，网络处于连接状态
        {
            Log.v("QLQ", "workStatus is not connected!111222");
            for (int i = 0; i < 3 && workStatus == null; i++) {// 如果连接处于关闭状态，重试三次，如果连接正常了，跳出循环
                socket = null;
                connectService();
                if (socket.isConnected() && (!socket.isClosed())) {
                    Log.v("QLQ", "workStatus is not connected!11333");
                    break;
                }
            }
            if (!socket.isConnected() || (socket.isClosed()))// 如果此时连接还是不正常，提示错误，并跳出循环
            {
                workStatus = Constant.TAG_CONNECTFAILURE;
                Log.v("QLQ", "workStatus is not connected!111444");
                return;
            }

        }

        if (!socket.isOutputShutdown()) {// 输入输出流是否关闭
            try {
//                writer.println(msg);
                os.write((msg + "\r\n").getBytes("utf-8"));
                os.flush();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.v("QLQ", "workStatus is not connected!55555666666");
                e.printStackTrace();
                workStatus = Constant.TAG_FAILURE;
            }
        } else {
            workStatus = Constant.TAG_CONNECTFAILURE;
        }
    }

    private boolean isNetworkConnected() {
        if (socket != null) {
            return true;
        }
        return false;
    }

    /**
     * 处理服务器端传来的消息，并通过action头信息判断，传递给相应处理方法
     *
     * @param str
     */
    private void getMessage(String str) {
        try {
            dealUploadSuperviseTask(str);
        } catch (Exception ex) {
            ex.printStackTrace();
            workStatus = Constant.TAG_FAILURE;
        }
    }

    public class InterBinder extends Binder {

        public MyService getService() {
            return MyService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        binder = new InterBinder();
        td = new Thread(MyService.this);// 启动线程
        td.start();

        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // connectService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                revHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //
                        if (msg.what == Constant.MSG_WHAT_SENDMSG) {
                            sendRequest(msg.obj.toString());
                        }
                    }
                };
                Looper.loop();
            }
        }).start();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        closeConnection();
        super.onDestroy();
        Log.v("QLQ", "Service is on destroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v("QLQ", "service on onUnbind");
        return super.onUnbind(intent);
    }

    /**
     * 循环，接收从服务器端传来的数据
     */
    public void run() {
        try {
            while (true) {
                Thread.sleep(500);// 休眠0.5s
                if (socket != null && !socket.isClosed()) {// 如果socket没有被关闭
                    if (socket.isConnected()) {// 判断socket是否连接成功
                        if (!socket.isInputShutdown()) {
                            String content;
                            if ((content = br.readLine()) != null) {
                                getMessage(content);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {

            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            workStatus = Constant.TAG_CONNECTFAILURE;// 如果出现异常，提示网络连接出现问题。
            ex.printStackTrace();
        }
    }

}
