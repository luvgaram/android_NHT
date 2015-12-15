package org.nhnnext.nearhoneytip.item;


import java.io.Serializable;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public class User implements Serializable {

//    + _id - user id
//    + nickname - 닉이름
//    + profilephoto - 프로필사진 경로
//    + status - 게시물 활성화 상태 1: 활성 0: 비활성(노출 안됨)
//
//    [
//    {
//        "_id": "testuid",
//            "nickname": "슬픔이",
//            "profilephoto": "icon/upload_e3b194650692f68706e512b3fd372af4.png",
//            "status": "1"
//    }
//    ]

    public String _id;
    public String nickname;
    public String profilephoto;
    public int status;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
