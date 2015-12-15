package org.nhnnext.nearhoneytip.item;


import java.io.Serializable;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public class UserResult implements Serializable {

//    + result - 결과
//    + ops - user 배열
//    + insertedCount - 입력된 횟수
//    + insertedIds - 입력된 id 배열
//
//{
//    "result": {
//    "ok": 1,
//            "n": 1
//},
//    "ops": [
//    {
//        "_id": "testuid5",
//            "nickname": "익명의 허니팁퍼",
//            "profilephoto": "icon/profilephoto1.png",
//            "status": "1"
//    }
//    ],
//    "insertedCount": 1,
//        "insertedIds": [
//    "testuid5"
//    ]
//}

    public UResult result;
    public User[] ops;
    public String insertedCount;
    public String[] insertedIds;

    public UResult getResult() {
        return result;
    }

    public void setResult(UResult result) {
        this.result = result;
    }

    public User[] getOps() {
        return ops;
    }

    public void setOps(User[] ops) {
        this.ops = ops;
    }

    public String getInsertedCount() {
        return insertedCount;
    }

    public void setInsertedCount(String insertedCount) {
        this.insertedCount = insertedCount;
    }

    public String[] getInsertedIds() {
        return insertedIds;
    }

    public void setInsertedIds(String[] insertedIds) {
        this.insertedIds = insertedIds;
    }
}
