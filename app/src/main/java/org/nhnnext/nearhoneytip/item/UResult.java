package org.nhnnext.nearhoneytip.item;


import java.io.Serializable;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public class UResult implements Serializable {

//    + ok - ok result
//    + n - inserted count
//{
//    "ok": 1,
//    "n": 1
//}
    public int ok;
    public int n;

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

}
