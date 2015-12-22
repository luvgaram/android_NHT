package org.nhnnext.nearhoneytip.item;

/**
 * Created by eunjooim on 2015. 12. 22.
 */
public class Result {

//    + error - error
//    + result - result
//
//{
//    "ok":1,
//    "nModified":0,
//    "n":0
//}

    public int ok;
    public int nModified;
    public int n;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getnModified() {
        return nModified;
    }

    public void setnModified(int nModified) {
        this.nModified = nModified;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

}
