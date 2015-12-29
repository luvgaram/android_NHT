package org.nhnnext.nearhoneytip.item;


import java.io.Serializable;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public class NearTipItem implements Serializable{

//    + _id - 팁 id
//    + file - 이미지 배열
//        + path - 이미지 저장 경로
//        + name - 이미지 이름
//    + storename - 가게 이름
//    + tipdetail - 게시물 내용
//    + uid - 게시자 아이디
//    + nickname - 게시자 닉네임
//    + profilephoto - 게시자 프로필사진 경로
//    + date - 게시 날짜
//    + status - 게시물 활성화 상태 1: 활성 0: 비활성(노출 안됨)
//    + like - 좋아요 수
//    + reply - 댓글
//    + TipLocation - 위치 정보
//    + dis - 거리
//    + isliked - 좋아요 상태
//
//    [
//        {
//            "_id": "5677e4526c18955f3ae513dd",
//                "file": [
//                    {
//                        "path": "data/upload_9cd6eb09ef63f58d8f9e9758648705a9.JPG",
//                            "name": "DSC02312.JPG"
//                    }
//            ],
//            "storename": "스타벅스 판교역점",
//            "tipdetail": "텀블러 사면 커피쿠폰 준대요. 빨강색 예뻐서 크리스마스 선물로 좋을듯..",
//            "uid": "357765060214043",
//            "nickname": "nexus5",
//            "profilephoto": "icon/upload_6898f46671c7dfac1fcb1e2c602e0b35.jpeg",
//            "date": "15-12-24 13:13:21",
//            "area": "1",
//            "status": "1",
//            "like": 6,
//            "reply": [],
//            "loc": {
//                    "type": "Point",
//                    "coordinates": [
//                        127.108548,
//                        37.401319
//                    ]
//            },
//            "dis": 78,
//            "isliked": false
//        }
//    ]


    public String storename;
    public String tipdetail;
    public String uid;
    public String nickname;
    public String profilephoto;
    public String date;
    public int status;
    public int like;
    public String[] reply;
    public TipLocation loc;
    public int dis;
    public boolean isliked;

    public String _id;
    public PhotoFile[] file;

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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String[] getReply() {
        return reply;
    }

    public void setReply(String[] reply) {
        this.reply = reply;
    }

    public TipLocation getLoc() {
        return loc;
    }

    public void setLoc(TipLocation loc) {
        this.loc = loc;
    }

    public int getDis() {
        return dis;
    }

    public void setDis(int dis) {
        this.dis = dis;
    }

    public boolean isliked() {
        return isliked;
    }

    public void setIsliked(boolean isliked) {
        this.isliked = isliked;
    }

}
