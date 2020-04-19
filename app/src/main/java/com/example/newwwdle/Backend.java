package com.example.newwwdle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Backend {
    private Thread thread;                //執行緒
    private Socket clientSocket;        //客戶端的socket
    private BufferedWriter bw;            //取得網路輸出串流
    private BufferedReader br;            //取得網路輸入串流
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
    private int status = -1;    // 0 for catch, 1 for
    private int ret_flag = -1;
    private String result = "";
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
                InetAddress serverIp = InetAddress.getByName("192.168.201.24");
                int serverPort = 8888;
                clientSocket = new Socket(serverIp, serverPort);
                //取得網路輸出串流
                bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                // 取得網路輸入串流
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // 當連線後
                while (clientSocket.isConnected()) {
/*
                    // 取得網路訊息
                    Log.d("OUTPUTLA","123");
                    tmp = br.readLine();    //宣告一個緩衝,從br串流讀取值
                    Log.d("OUTPUTLA",tmp);
                    // 如果不是空訊息
                    if(tmp!=null){
                        // send message from server
                        bw.write(tmp);
                        bw.flush();
                    }
*/

                    if(write_flag == 1){
                        bw.write(send_msg);
                        bw.flush();
                        status = 0;
                        if(status == 0){
                            //Log.d("OUTPUTLA",send_msg);
                            tmp = br.readLine();
                            switch(KEY){
                                case 1:
                                    if(tmp.equals("No Account")){
                                        Log.d("OUTPUTLA","NO");
                                        ret_flag = 1;
                                        KEY = -1;
                                        result = tmp;
                                    }
                                    else if(tmp.equals("Wrong Password")){
                                        Log.d("OUTPUTLA","WRONG");
                                        ret_flag = 1;
                                        KEY = -1;
                                        result = tmp;
                                    }
                                    else{
                                        Log.d("OUTPUTLA","SUCCESS");
                                        ret_flag = 1;
                                        KEY = -1;
                                        result = tmp;
                                    }
                                    break;

                                case 2:
                                    Log.d("OUTPUTLA","ANNOUNCE");
                                    ret_flag = 1;
                                    KEY = -1;
                                    result = tmp;
                                    break;
                                case 3:
                                    Log.d("OUTPUTLA","ALL STATUS");
                                    ret_flag = 1;
                                    KEY = -1;
                                    result = tmp;
                                    break;
                                case 4:
                                    Log.d("OUTPUTLA","IMEI CHECK");
                                    ret_flag = 1;
                                    KEY = -1;
                                    result = tmp;
                                    break;
                                case 5:
                                    Log.d("OUTPUTLA", "IMEI ADD");
                                    ret_flag = 1;
                                    KEY = -1;
                                    result = tmp;
                                    break;
                                case 6:
                                    Log.d("OUTPUTLA","ROLL CALL CHECK(TODAY)");
                                    ret_flag = 1;
                                    KEY = -1;
                                    result = tmp;
                                    break;

                                case 7:
                                    Log.d("OUTPUTLA","ROLL CALL CHECK(ALL)");
                                    ret_flag = 1;
                                    KEY = -1;
                                    result = tmp;
                                    break;
                                case 8:
                                    Log.d("OUTPUTLA","ROLL CALL UPDATE");
                                    ret_flag = 1;
                                    KEY = -1;
                                    result = tmp;
                                    break;

                                case 9:
                                    Log.d("OUTPUTLA","ADD ANNOUNCEMENT");
                                    ret_flag = 1;
                                    KEY = -1;
                                    result = tmp;
                                    break;

                                case 10:
                                    Log.d("OUTPUTLA","ENABLE TO ROLL CALL");
                                    ret_flag = 1;
                                    KEY = -1;
                                    result = tmp;
                                    break;
                            }
                            while(true){
                                // stop
                            }
                            //Log.d("OUTPUTLA",tmp);
                        }
                        write_flag = 0;

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
            json_write=new JSONObject();
            json_write.put("action","離線");    //傳送離線動作給伺服器
            Log.i("text","onDestroy()="+json_write+"\n");
            //寫入後送出
            bw.write(json_write+"\n");
            bw.flush();
            //關閉輸出入串流後,關閉Socket
            //最近在小作品有發現close()這3個時,導致while (clientSocket.isConnected())這個迴圈內的區域錯誤
            //會跳出java.net.SocketException:Socket is closed錯誤,讓catch內的處理再重複執行,如有同樣問題的可以將下面這3行註解掉
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
                sendMessage("catch,-1,-1,-1,"+input1+",-1",key);
                while(ret_flag == 0){
                    // enable to return string

                }
                ret_flag = -1;
                Log.d("OUTPUTLA",result);
                return result;

            case 3: // key = 3, (Teacher)input: CID and return class roll call status: function(KEY=3,CID), send a key to distinguish
                sendMessage("catch,-1,-1,-1,"+input1+",2",key);
                while(ret_flag == 0){
                    // enable to return string

                }
                ret_flag = -1;
                Log.d("OUTPUTLA",result);
                return result;

            case 4: // key = 4, input: IMEI and return a result(Enable or Disable to login) : function(KEY=4,IMEI)
                sendMessage("catch,"+input1+",-1,-1,-1,-1",key);
                while(ret_flag == 0){
                    // enable to return string

                }
                ret_flag = -1;
                Log.d("OUTPUTLA",result);
                return result;

            case 5: // key = 5, input: IMEI and add this IMEI to database : function(KEY=5,IMEI)
                sendMessage("add,"+input1+",-1,-1,-1,-1,-1",key);
                while(ret_flag == 0){
                    // enable to return string

                }
                ret_flag = -1;
                Log.d("OUTPUTLA",result);
                return "NothingToDo";
        }
        return "";
    }

    public String Communication(int key, String input1, String input2){
        // add : [IMEI, start/stop, ID , CID, announce, GPS]
        // catch: [IMEI, SID, passwd, CID, key] key : 0 ~ 2
        switch (key){
            case 1: // key = 1, input: SID,passwd -> Login and return CID cname name type ctime : function(KEY=1,ID)
                sendMessage("catch,-1,"+input1+","+input2+",-1,-1",key);
                while(ret_flag == 0){
                    // enable to return string

                }
                ret_flag = -1;
                Log.d("OUTPUTLA",result);
                return result;

            case 6: // key = 6, input: SID,CID and (student)get roll call status(today) : function(KEY=6,SID,CID)
                sendMessage("catch,-1,"+input1+",-1,"+input2+",0",key);
                while(ret_flag == 0){
                    // enable to return string

                }
                ret_flag = -1;
                Log.d("OUTPUTLA",result);
                return result;

            case 7: // key = 7, input: SID,CID and (student)get all roll call of this class : function(KEY=7,SID,CID)
                sendMessage("catch,-1,"+input1+",-1,"+input2+",1",key);
                while(ret_flag == 0){
                    // enable to return string

                }
                ret_flag = -1;
                Log.d("OUTPUTLA",result);
                return result;

            case 8: // key = 8, input: SID,CID and update roll call in database : function(KEY=8,SID,CID)
                sendMessage("add,-1,-1,"+input1+","+input2+",-1,-1",key);
                while(ret_flag == 0){
                    // enable to return string

                }
                ret_flag = -1;
                Log.d("OUTPUTLA",result);
                return "NothingToDo";
        }
        return "";
    }

    public String Communication(int key, String cid, String title, String announce) {
        // add : [IMEI, start/stop, ID , CID, announce, GPS]
        // key = 9, input: cid, title, announce and add an announce in database : function(KEY=9,CID,title,announce)
        if (key == 9) {
            sendMessage("add,-1,-1,-1," + cid + "," + title + "," + announce + ",-1",key);
            while(ret_flag == 0){
                // enable to return string

            }
            ret_flag = -1;
            Log.d("OUTPUTLA",result);
            return "NothingToDo";
        }
        return "";
    }

    public String Communication(int key, String cid, int start_stop, double gps_1, double gps_2){
        if(key == 10) {
            // add : [IMEI, start/stop, ID , CID, announce, GPS]
            // key = 10, input: cid, startSign, GPS and update status of roll_call_sign : function(KEY=10,CID,startSign,GPS1,GPS2)
            sendMessage("add,-1,"+start_stop+",-1,"+cid+",-1,"+gps_1+","+gps_2,key);
            while(ret_flag == 0){
                // enable to return string

            }
            ret_flag = -1;
            Log.d("OUTPUTLA",result);
            return "NothingToDo";
        }
        return "";
    }

    private void sendMessage(String message, int key){
        send_msg = message;
        KEY = key;
        ret_flag = 0;
        write_flag = 1;
    }

}
