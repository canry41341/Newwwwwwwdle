server前置安裝:
1. pip install firebase-admin google-cloud-firestore keyboard
2. 把json檔放到跟server同個資料夾(或是自己去340行改路徑)(這是金鑰不要push上去可以放到gitignore裡)


database 的架構:

Accounts
	ID1
		AccountData
			Name: "Tom"(String)
			Password: "123"(String)
			Type: "student"(String)       -> 學生帳戶 or 老師帳戶
		Courses
			CID1
				2020y4m12d:0(int)     -> 學生課堂點名資料
Courses
	CID1
		CourseData
			GPS
				0:3.14(double)
				1:3.14(double)        -> GPS (兩個double)
			Name : "Math"(String)         -> 課程名稱
			Sign: true(boolean)           -> 這堂課可不可以點名(後端沒計時)
			Time: "2d3h5h" (String)       -> 這堂課ㄉ上課時間(星期二3.~5.)
		Announces
			1
				Announce: "announce"(String)
				Date: "2020y4m14d"(String)
				Title: "title"(String)             -> 公告 1是unique_id, 然後會存公告內容、標題跟公告日期
		Students
			ID1:1(int)
			ID3:1(int)
		Teachers
			ID2:1(int)
			ID4:1(int)               ->  這邊就是紀錄有哪些老師、學生在這堂課裡
Black
	123:123245345563.2312313(float)          ->  IMEI:被鎖的時間