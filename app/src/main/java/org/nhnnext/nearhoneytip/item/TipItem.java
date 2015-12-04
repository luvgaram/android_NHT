package org.nhnnext.nearhoneytip.item;


import org.nhnnext.nearhoneytip.item.PhotoFile;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public class TipItem {

//    + _id - 팁 id
//    + file - 이미지 배열
//        + path - 이미지 저장 경로
//        + name - 이미지 이름
//    + storename - 가게 이름
//    + tipdetail - 게시물 내용
//    + uid - 게시자 아이디
//    + nickname - 게시자 닉네임
//    + profilephoto - 게시자 프로필사진 경로
//    + status - 게시물 활성화 상태 1: 활성 0: 비활성(노출 안됨)
//
//    [
//      {
//        "_id": "5645724eb33e2e513111c5ae",
//        "file": [
//          {
//            "path": "data/upload_2a3a13a432239b3b1de51a30649e2b94.jpg",
//            "name": "cat03.jpg"
//          }
//        ],
//        "storename": "올리브영 유스페이스",
//        "tipdetail": "50% 할인 이벤트!",
//        "uid": "1",
//        "nickname": "익명의 허니팁퍼",
//        "profilephoto": "icon/profilephoto1.png",
//        "date": "Fri Nov 13 2015 14:17:02 GMT+0900 (KST)",
//        "area": "1",
//        "status": "1"
//      }
//    ]

    public String _id;
    public PhotoFile[] file;
    public String storename;
    public String tipdetail;
    public String uid;
    public String nickname;
    public String profilephoto;
    public String date;
    public int status;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public PhotoFile[] getFile() {
        return file;
    }

    public void setFile(PhotoFile[] file) {
        this.file = file;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getTipdetail() {
        return tipdetail;
    }

    public void setTipdetail(String tipdetail) {
        this.tipdetail = tipdetail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfilephoto() {
        return profilephoto;
    }

    public void setProfilephoto(String profilephoto) {
        this.profilephoto = profilephoto;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
