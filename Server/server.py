# change from 2020/6/21 the newest version
import socket
import sys
import time
import keyboard
import json

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import db
from pyfcm import FCMNotification
from _thread import *
import threading

host = "192.168.43.107"

lock = threading.Lock()

port = 8887
loop_out = False

def create_thread(conn,addr):
    while(True):
        try:
            if(loop_out):
                break
            data = conn.recv(1024)
            #lock.release()
            #print(data)
            if data != b"":
                print(data)
                #lock.acquire()
                parse_data(conn,data.decode())
                #lock.release()
                print("finish")
        except Exception as e:
            pass

def press_event(x):
    global loop_out
    
    enter = keyboard.KeyboardEvent('down', 28, 'q')
    if x.event_type == 'down' and x.name == enter.name:
        loop_out = True

def write_database(str_reference , str_key, str_value):
    doc = {
  str_key: str_value
    }
    db_ref = db.reference(str_reference)
    db_ref.update(doc)
    return 0

def get_database(str_reference):
    db_ref = db.reference(str_reference)
    return db_ref.get()

def remove_database(str_reference):
    if(get_database(str_reference) != None):
        db_ref = db.reference(str_reference)
        db_ref.delete()
    return 0

def parse_data(conn,data):
    element = [ ]
    data = data.split(',')
    operator = data[0]
    if(operator == "add" and len(data) >= 7):  #(args = [IMEI , start/stop,  ID , CID,announce,  GPS])
        IMEI = data[1]
        startSign = data[2]
        ID = data[3]
        CID = data[4]
        Announce = data[5]
        if(IMEI != "-1" and ID != "-1"):
            print("add " + str(IMEI) +" into black")
            add_black(IMEI)
            write_database("/Accounts/"+str(ID)+"/AccountData/","Devicetoken","-1")
            conn.sendall("True\n".encode())
        elif(startSign != "-1"):
            if(startSign == "1" and len(data) == 8):
                GPS_x = data[6]
                GPS_y = data[7]
                print("start class rollcall with GPS")
                if(start_rollcall(CID,float(GPS_x),float(GPS_y))):
                    conn.sendall("True\n".encode())
                else:
                    conn.sendall("No Course\n".encode())
            elif(startSign == "0"):
                print("stop class rollcall with or without GPS")
                if(stop_rollcall(CID)):
                    conn.sendall("True\n".encode())
                else:
                    conn.sendall("No Course\n".encode())   
        elif(Announce != "-1" and len(data) == 8):
            Title = data[5]
            Announce = data[6]
            print("add announce")
            if(add_announce(CID,Title,Announce)):
                conn.sendall("True\n".encode())
            else:
                conn.sendall("No Course\n".encode())
        elif(ID != "-1" and CID != "-1" and data[6] != "-1"):
            print("add rollcall data")
            Announce = data[6]
            msg = add_rollcall(ID,CID,Announce)
            if(msg == "True\n"):
                conn.sendall("True\n".encode())
            else:
                conn.sendall(msg.encode())
        else:
            print("Error Format")
            conn.sendall("Error Format\n".encode())
    elif(operator == "catch" and len(data) >= 6):    #(args = [IMEI, SID, passwd, CID,key])
        IMEI = data[1]
        SID = data[2]
        passwd = data[3]
        CID = data[4]
        key = data[5]
        if(IMEI != "-1" and SID != "-1"):
            print("check if it can login")
            type = get_database("/Accounts/"+str(SID)+"/AccountData/Type/")
            print(type)
            if(type == "student"):
                if(IMEI != "null" and check_login(IMEI)):
                    print("successfully login")
                    write_database("/Accounts/"+str(SID)+"/AccountData/","Devicetoken",IMEI)
                    conn.sendall("True\n".encode())
                    print("True")
                elif IMEI != "null" and not check_login(IMEI):
                    print("login failed") 
                    conn.sendall("False\n".encode()) 
                    print("False")  
                else:
                    print("No IMEI")
                    conn.sendall("Error\n".encode())
            elif(type == "teacher"):
                conn.sendall("True\n".encode())
            else:
                conn.sendall("Error\n".encode())
        elif (SID != "-1" and passwd != "-1"):
            print("Checking Account")
            able,mode = check_account(SID,passwd)
            if(able):
                print(True)
                print("give info")
                name,type_id,cls_id,cls_name,cls_time,cls_pos = give_info(SID)
                print("name: ",name)
                print("cls_name: ",cls_name)
                print("cls_time: ",cls_time)
                print("cls_pos: ",cls_pos)
                msg = name+";"+type_id+";"
                for cs in cls_id:
                    if(cs != None):
                        msg += (cs+";")
                for cs in cls_name:
                    if(cs != None):
                        msg += (cs+";")
                for cs in cls_time:
                    if(cs != None):
                        msg += (cs+";")
                for cs in cls_pos:
                    if(cs != None):
                        msg += (cs+";")
                print(msg)
                conn.sendall((msg+"\n").encode())
            else:
                if(mode == 1):
                    conn.sendall("No Account\n".encode())
                elif(mode == 2):
                    conn.sendall("Wrong Password\n".encode())
                else:
                    pass
                print(False)                      
        elif (SID != "-1" and CID != "-1" and key == "0"): # personal sign
            print("get personal sign info")
            result = get_personal_sign(SID,CID)
            if(type(result) == bool):
                if(result == True):
                    print("True")
                    conn.sendall("True\n".encode())
                else:
                    print("False")
                    conn.sendall("False\n".encode())
            else:
                print(result)
                conn.sendall(result.encode())
                #conn.sendall("False\n".encode())
        elif (SID != "-1" and CID != "-1" and key == "1"): #persinal all sign
            print("get personal all sign info")
            sign = get_database("Accounts/"+str(SID)+"/Courses/"+str(CID)+"/")
            sg_k = list(sign.keys())
            sg_v = list(sign.values())
            msg = ""
            j = 0
            while(j < len(sg_k)):
                msg += (str(sg_k[j])+"/"+str(sg_v[j])+";")
                j += 1
            msg += "\n"
            print("msg: ",msg)
            conn.sendall(msg.encode())
        elif (SID == "-1" and CID != "-1" and key == "2"): #all sign
            stds,signs = get_all_sign(CID)
            msg = ""
            i = 0
            for std in stds:
                msg += (std+":")
                sig = signs[i]
                sig_k = list(sig.keys())
                sig_v = list(sig.values())
                j = 0
                while(j < len(sig_k)):
                    msg += (str(sig_k[j])+"/"+str(sig_v[j])+",")
                    j += 1
                i += 1
                msg += ";"
            msg += "\n"
            if(msg != ""):
                conn.sendall(msg.encode())  
                print("msg: ",msg)            
        elif (CID != "-1" and SID == "-1" and key == "3"):
            print("get announce")
            announces = get_database("/Courses/"+str(CID)+"/Announces/")
            print("announces: ",announces)
            msg = ""
            i = 0
            while(i<len(announces)):
                if(announces[i] != None):
                    lst_val = list(announces[i].values())
                    msg += (str(i)+"/"+lst_val[0]+"/"+lst_val[1]+"/"+lst_val[2]+";")
                i += 1
            msg += "\n"
            conn.sendall(msg.encode())
        elif(CID != "-1" and SID == "-1" and key == "4"):
            print("get sign and GPS")
            sign = get_database("/Courses/"+str(CID)+"/CourseData/Sign/")
            gps_0 = get_database("/Courses/"+str(CID)+"/CourseData/GPS/0/")
            gps_1 = get_database("/Courses/"+str(CID)+"/CourseData/GPS/1/")
            if(sign == None):
                print("No Sign\n")
                conn.sendall("No Sign\n".encode())
            elif(gps_0 == None or gps_1 == None):
                print("No GPS\n")
                conn.sendall("No GPS\n".encode())
            else:
                msg = ""
                msg = msg + str(sign) + "," + str(gps_0) + "," + str(gps_1) + "\n"
                print(msg)
                conn.sendall(msg.encode())
        else:
            print("Error format~~~~")
            return -1
    else:
        print("Error Format")
        conn.sendall("Error Format\n")

def add_black(IMEI):
    write_database("/Black/",IMEI,time.time())

def add_rollcall(ID,CID,Status):
    date = time.localtime(time.time())
    year = date.tm_year
    month = date.tm_mon
    day = date.tm_mday
    if(get_database("/Courses/"+str(CID)+"/") == None ):
        print("No Course")
        return "Error\n"
    elif(get_database("/Courses/"+str(CID)+"/CourseData/Sign/") == None):
        print("Not Rollcall Time")
        return "Error\n"
    elif(get_database("Accounts/"+str(ID)+"/") == None):
        print("No Student")
        return "Error\n"
    elif(get_database("Accounts/"+str(ID)+"/Courses/"+str(CID)+"/"+str(year)+"y"+str(month)+"m"+str(day)+"d"+"/") == None):
        print("No Sign")
        return "Error\n"
    else:
        sign = get_database("Accounts/"+str(ID)+"/Courses/"+str(CID)+"/"+str(year)+"y"+str(month)+"m"+str(day)+"d"+"/")
        if(sign == float(Status)):
            pass
        else:
            write_database("Accounts/"+str(ID)+"/Courses/"+str(CID)+"/",str(year)+"y"+str(month)+"m"+str(day)+"d",int(Status))
        if(Status == "1"):
            print("Status: True\n")
            return "True\n"
        else:
            print("Status: False\n")
            return "False\n"


def start_rollcall(CID,GPS_x,GPS_y):
    GPS = [GPS_x,GPS_y]
    if(not class_exist(CID)):
        print("The course is not exist")
        return False
    write_database("Courses/"+str(CID)+"/CourseData/","Sign",True)
    write_database("Courses/"+str(CID)+"/CourseData/","GPS",GPS)
    date = time.localtime(time.time())
    year = date.tm_year
    month = date.tm_mon
    day = date.tm_mday
    for std in get_database("Courses/"+str(CID)+"/Students/").keys():
        if(get_database("Accounts/"+str(std)+"/Courses/"+str(CID)+"/"+str(year)+"y"+str(month)+"m"+str(day)+"d") == None):
            write_database("Accounts/"+str(std)+"/Courses/"+str(CID)+"/",str(year)+"y"+str(month)+"m"+str(day)+"d",0)
    return True    

def stop_rollcall(CID):
    if(not class_exist(CID)):
        print("The course is not exist")
        return False
    write_database("Courses/"+str(CID)+"/CourseData/","Sign",False)
    write_database("Courses/"+str(CID)+"/CourseData/","GPS",[-1,-1])
    return True

def add_announce(CID,Title,Announce):
    if(not class_exist(CID)):
        print("The course is not exist")
        return False
    date = time.localtime(time.time())
    year = date.tm_year
    month = date.tm_mon
    day = date.tm_mday
    AID = 1
    while(True):
        if(get_database("Courses/"+str(CID)+"/Announces/"+str(AID)) == None):
            break
        else:
            AID += 1
    if len(Title)<1:
        Title = "Untitle"
    if len(Announce)<1:
        Announce = " "
    write_database("Courses/"+str(CID)+"/Announces/"+str(AID)+"/","Date",str(year)+"y"+str(month)+"m"+str(day)+"d")
    write_database("Courses/"+str(CID)+"/Announces/"+str(AID)+"/","Title",Title)
    write_database("Courses/"+str(CID)+"/Announces/"+str(AID)+"/","Announce",Announce)

    push_server = FCMNotification(api_key='AAAAkgMVFwk:APA91bGzftIDgzp7nz1MLSODtNjDLRjlBiXGZSOypeVeQVKDtOEaP09sJpsanDuBX09Mqv0s-afCN-PHjC0_fUOdhmZUBfKVV6YS6-Q1Uhcln42WMZ70264A0Jg_JGJm6pEGoW9Yld1y')
    register_ids =[]
    stds = get_database("Courses/"+str(CID)+"/Students")
    for std in stds:
        token = get_database("Accounts/"+str(std)+"/AccountData/Devicetoken/")
        if(token != None and token != "-1"):
            print(token)
            print(std)
            register_ids.append(token)
    push_server.notify_multiple_devices(registration_ids=register_ids,message_title=Title,message_body=Announce)
  
    return True

def check_login(IMEI):
    basic_time = get_database("/Black/"+str(IMEI))
    if(basic_time == None):
        print("UnBlock")
        return True
    if((time.time()-basic_time)/60 > 1):
        print("out black")
        remove_database("/Black/"+str(IMEI))
        return True
    else:
        print("lock")
        return False

def give_info(SID):
    name = get_database("/Accounts/"+str(SID)+"/AccountData/Name/")
    type_id = get_database("/Accounts/"+str(SID)+"/AccountData/Type/")
    id_class = get_database("/Accounts/"+str(SID)+"/Courses/")
    if(name == None):
        print("No Account")
        return -1
    if(id_class == None):
        print("No class")
        return -1
    id_class_name = []
    id_class_time = []
    id_class_pos = []
    for std_cls in id_class.keys():
        id_class_name.append(get_database("/Courses/"+std_cls+"/CourseData/Name"))
        id_class_time.append(get_database("/Courses/"+std_cls+"/CourseData/Time"))
        id_class_pos.append(get_database("/Courses/"+std_cls+"/CourseData/Position"))
    
    return name,type_id,id_class,id_class_name,id_class_time,id_class_pos

def get_personal_sign(SID,CID):
    if(get_database("Accounts/"+str(SID)+"/") == None):
        return "Wrong ID\n"
    elif(get_database("Accounts/"+str(SID)+"/Courses/"+str(CID)+"/") == None):
        return "No Course\n"
    date = time.localtime(time.time())
    year = date.tm_year
    month = date.tm_mon
    day = date.tm_mday
    sign = get_database("Accounts/"+str(SID)+"/Courses/"+str(CID)+"/"+str(year)+"y"+str(month)+"m"+str(day)+"d/")
    if(sign == None):
        return "No Sign\n"
    elif(sign == 0):
        return False
    else:
        return True

def get_all_sign(CID):
    sign = []
    students = get_database("Courses/"+str(CID)+"/Students/")
    for std in students.keys():
        sign.append(get_database("Accounts/"+str(std)+"/Courses/"+str(CID)+"/"))
    return students.keys(),sign

def check_account(SID,passwd):
    get_acnt = get_database("Accounts/"+str(SID)+"/")
    get_pwd = get_database("Accounts/"+str(SID)+"/AccountData/Password/")
    if(get_acnt == None):
        print("No Account")
        return False,1
    elif(get_pwd == None or get_pwd != passwd):
        print("Wrong Password")
        return False,2
    else:
        return True,0

def class_exist(CID):
    courses = get_database("/Courses/")
    if courses == None:
        return False
    for clas in courses:
        if(clas == CID):
            return True
    return False

if __name__ == "__main__":
    keyboard.hook(press_event)
    print("Connecting to DataBase ~~~~")
    cred = credentials.Certificate("./key.json")
    firebase_admin.initialize_app(cred, {'databaseURL': 'https://nedle-2cf29.firebaseio.com/' })

    print("Connecting to Socket")
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((host, port))
    except:
        sys.exit()

    s.setblocking(False)
    s.listen(5)
    print("Start Server ~~~~")
    while(True):
        if(loop_out):
            break
        try:
            conn, addr = s.accept()
            print("Connection")
            conn.setblocking(False)
            #lock.acquire()
            threading.Thread(target=create_thread,args=(conn,addr)).start()
        except Exception as e:
            pass
        
