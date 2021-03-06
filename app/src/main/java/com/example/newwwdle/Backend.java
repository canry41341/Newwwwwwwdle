package com.example.newwwdle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Backend {
    private Thread thread;                //執行緒
    static protected Socket clientSocket;        //客戶端的socket
    static protected BufferedWriter bw;            //取得網路輸出串流
    static protected BufferedReader br;            //取得網路輸入串流
    private String tmp;                    //做為接收時的緩存
    private String send_msg = "";
    // Member Data
    private String IMEI = "";
    private String announce = "";
    private int startSign = -1;
    private double GPS[];
    private String ID = "";
    private String CID = "";
    private int write_flag = 0;
    private int KEY = -1;
    private int ret_flag = -1;
    private String result = "";
    static protected OutputStreamWriter outputStreamWriter;
    static protected InputStreamReader inputStreamReader;
    Backend(){
        thread = new Thread(Connection);
        thread.start();
        Log.d("Start","123") ;
    }

    //連結socket伺服器做傳送與接收
    private Runnable Connection=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try{
                // IP為Server端
                InetAddress serverIp = InetAddress.getByName("192.168.43.107");
                int serverPort = 8887;
                clientSocket = new Socket(serverIp, serverPort);
                //取得網路輸出串流
                outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
                inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
                bw = new BufferedWriter(outputStreamWriter);
                // 取得網路輸入串流
                br = new BufferedReader(inputStreamReader);
                Log.d("msg","createla");
                // 當連線後
                while (clientSocket.isConnected()) {
                    if(write_flag == 1){
                        Log.d("msg", send_msg);
                        bw.write(send_msg);
                        bw.flush();
                        tmp = br.readLine();
                        //Log.d("msg", tmp);
                        switch(KEY){
                            case 1:
                                if(tmp.equals("No Account")){
                                    Log.d("OUTPUTLA","NO");
                                }
                                else if(tmp.equals("Wrong Password")){
                                    Log.d("OUTPUTLA","WRONG");
                                }
                                else{
                                    Log.d("OUTPUTLA","SUCCESS");
                                }
                                result = tmp;
                                write_flag = 0;
                                break;

                            case 2:
                                Log.d("OUTPUTLA","ANNOUNCE");
                                result = tmp;
                                write_flag = 0;
                                break;

                            case 3:
                                Log.d("OUTPUTLA","ALL STATUS");
                                result = tmp;
                                write_flag = 0;
                                break;

                            case 4:
                                Log.d("OUTPUTLA","IMEI CHECK");
                                result = tmp;
                                write_flag = 0;
                                break;
                            case 5:
                                Log.d("OUTPUTLA", "IMEI ADD");
                                result = tmp;
                                write_flag = 0;
                                break;

                            case 6:
                                Log.d("OUTPUTLA","ROLL CALL CHECK(TODAY)");
                                result = tmp;
                                write_flag = 0;
                                break;

                            case 7:
                                Log.d("OUTPUTLA","ROLL CALL CHECK(ALL)");
                                Log.d("OUTPUTLA",tmp);
                                result = tmp;
                                write_flag = 0;
                                break;

                            case 8:
                                Log.d("OUTPUTLA","ROLL CALL UPDATE");
                                result = tmp;
                                write_flag = 0;
                                break;

                            case 9:
                                Log.d("OUTPUTLA","ADD ANNOUNCEMENT");
                                result = tmp;
                                write_flag = 0;
                                break;

                            case 10:
                                Log.d("OUTPUTLA","ENABLE TO ROLL CALL");
                                result = tmp;
                                write_flag = 0;
                                break;

                            case 11:
                                Log.d("OUTPUTLA","GET TEACHER GPS");

                                result = tmp;
                                write_flag = 0;
                                break;
                        }
                        if(KEY>=1 && KEY<=11){
                            KEY = -1;
                            ret_flag = 1;
                        }

                    }


                }


            }catch(Exception e){
                //當斷線時會跳到catch,可以在這裡寫上斷開連線後的處理
                e.printStackTrace();
                Log.e("text","Socket連線="+e.toString());
                //finish();    //當斷線時自動關閉房間
            }
        }
    };
/*
    @Override
    protected void onDestroy() {            //當銷毀該app時
        super.onDestroy();
        try {
            //關閉輸出入串流後,關閉Socket
            bw.close();
            br.close();
            clientSocket.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("text","onDestroy()="+e.toString());
        }
    }

*/

    public String Communication(int key, String input1){
        // add : [IMEI, start/stop, ID , CID, announce, GPS]
        // catch: [IMEI, SID, passwd, CID, key] key : 0 ~ 2

        switch(key){
            case 2: // key = 2, input: CID and return all class announce (time,title,intent) : function(KEY=2,CID)
                sendMessage("catch,-1,-1,-1,"+input1+",3",key);
                lock();
                initial();
                Log.d("OUTPUTLA",result);
                return result;

            case 3: // key = 3, (Teacher)input: CID and return class roll call status: function(KEY=3,CID), send a key to distinguish
                sendMessage("catch,-1,-1,-1,"+input1+",2",key);
                lock();
                initial();
                Log.d("OUTPUTLA",result);
                return result;

            case 11: // key = 11, input: CID and get teacher's GPS
                sendMessage("catch,-1,-1,-1,"+input1+",4",key);
                lock();
                initial();
                Log.d("OUTPUTLA",result);
                return result;
        }
        return "";
    }

    public String Communication(int key, String input1, String input2){
        // add : [IMEI, start/stop, ID , CID, announce, GPS]
        // catch: [IMEI, SID, passwd, CID, key] key : 0 ~ 2
        switch (key){
            case 1: // key = 1, input: SID,passwd -> Login and return CID cname name type ctime : function(KEY=1,ID)
                sendMessage("catch,-1,"+input1+","+input2+",-1,-1",key);
                lock();
                initial();
                Log.d("OUTPUTLA",result);
                return result;

            case 6: // key = 6, input: SID,CID and (student)get roll call status(today) : function(KEY=6,SID,CID)
                sendMessage("catch,-1,"+input1+",-1,"+input2+",0",key);
                lock();
                initial();
                Log.d("OUTPUTLA",result);
                return result;

            case 7: // key = 7, input: SID,CID and (student)get all roll call of this class : function(KEY=7,SID,CID)
                //result = "";
                sendMessage("catch,-1,"+input1+",-1,"+input2+",1",key);
                lock();
                initial();
                Log.d("OUTPUTLA","HERE");
                return result;


            case 4: // key = 4, input: SID, token : login by checking token and add token into account
                sendMessage("catch,"+input2+","+input1+",-1,-1,-1",key);
                lock();
                initial();
                Log.d("OUTPUTLA",result);
                return result;

            case 5: // key = 5, input: SID,token : logout and 1. remove token from account 2. add token into black
                sendMessage("add,"+input2+",-1,"+input1+",-1,-1,-1",key);
                lock();
                initial();
                Log.d("OUTPUTLA",result);
                return result;
        }
        return "";
    }

    public String Communication(int key, String cid, String title, String announce) {
        // add : [IMEI, start/stop, ID , CID, announce, GPS]
        // key = 9, input: cid, title, announce and add an announce in database : function(KEY=9,CID,title,announce)
        if (key == 8) { // key = 8, input: SID,CID, rollcall and update roll call in database : function(KEY=8,SID,CID) return True or False or Error
            sendMessage("add,-1,-1," + cid + "," + title + ",-1," + announce + ",-1", key);
            lock();
            initial();
            Log.d("OUTPUTLA", result);
            return result;
        }
        if (key == 9) {
            sendMessage("add,-1,-1,-1," + cid + "," + title + "," + announce + ",-1",key);
            lock();
            initial();
            Log.d("OUTPUTLA",result);
            return result;
        }
        return "";
    }

    public String Communication(int key, String cid, int start_stop, double gps_1, double gps_2){
        if(key == 10) {
            // add : [IMEI, start/stop, ID , CID, announce, GPS]
            // key = 10, input: cid, startSign, GPS and update status of roll_call_sign : function(KEY=10,CID,startSign,GPS1,GPS2)
            Log.d("msg", "1231456465651");
            sendMessage("add,-1,"+start_stop+",-1,"+cid+",-1,"+gps_1+","+gps_2,key);
            lock();
            initial();
            Log.d("OUTPUTLA",result);
            return result;
        }
        return "";
    }

    private void sendMessage(String message, int key){
        send_msg = message;
        KEY = key;
        ret_flag = 0;
        write_flag = 1;
    }

    private void lock(){
        // lock until finish passing message
        while(true){
            //
            try {
                this.thread.sleep(100);
            }catch (InterruptedException e){

            }

            if(ret_flag == 1) break;
        }
    }

    private void initial(){
        // initial variable
        ret_flag = -1;
        KEY = -1;
        write_flag = 0;
        send_msg = "";
    }
}
