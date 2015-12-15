package org.nhnnext.nearhoneytip.item;

import java.io.Serializable;

/**
 * Created by eunjooim on 15. 11. 21.
 */
public class PhotoFile implements Serializable{
    //    + file - 이미지 배열
    //        + path - 이미지 저장 경로
    //        + name - 이미지 이름

    public String path;
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
