第一步：
Backend backend = new Backend();
(一創建好就連上server)

第二步：
呼叫function，共10個function:

1. 登入並回傳該學生課程資訊 (Input:key,SID,passwd Output:學生姓名、帳戶類型(學生or老師)、課程名稱、課程時間)
result = backend.Communication(1,"學號","密碼")  --> result的type是string
(備註:若輸入帳戶錯誤，會回傳No Account、密碼錯誤，會回傳Wrong Password，正確登入才會回傳上面的Output)

2. 取得某課程的所有公告(Input:key,CID Output:announce、time、title) -->未切割字串
result = backend.Communication(2,"課程名稱")

3. (老師)獲得某堂課整學期的點名狀況 (Input:key,CID Output: 學生ID:日期/簽到) -->未切割字串
result = backend.Communication(3,"課程名稱")

4. 確認某IMEI是否在黑名單內 (Input:key,IMEI Output:True or False)
result = backend.Communication(4,"IMEI")

5. 登出時將某個IMEI加入黑名單 (Input:key,IMEI Output:NothingToDo) --> 更改資料庫的資料而已，回傳值不重要
result = backend.Communication(5,"IMEI")

6. (學生)拿到自己某堂課的當天出席狀況 (Input:key,SID,CID Output: True or False)
result = backend.Communication(6,"學號","課程名稱")

7. (學生)拿到自己某堂課的所有出席狀況 (Input:key,SID,CID Output: 日期/簽到) -->未切割字串
result = backend.Communication(7,"學號","課程名稱")

8. 更改某學生當天的某堂出席狀況(Input:key,SID,CID Output:NothingToDo) --> 回傳值同5
result = backend.Communication(8,"學號","課程名稱")

9. 發布某堂課的公告(Input:key,CID,TITLE,ANNOUNCE Output:NothingToDo) --> 回傳值同5
result = backend.Communication(9,"課程名稱","標題","公告")

10. 老師開啟點名功能(Input:key,CID,StartSign,GPS1,GPS2 Output:NothingToDo) --> 回傳值同5
result = backend.Communication(10,"課程名稱","點名/未點名狀態","GPS1","GPS2") --> 回傳值同5
(備註:)