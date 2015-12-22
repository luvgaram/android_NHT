package org.nhnnext.nearhoneytip.item;

/**
 * Created by eunjooim on 2015. 12. 22.
 */
public class ResponseResult {

//    + error - error
//    + result - result
//
//{
//    "error": null,
//    "results":{
//        "ok":1,
//        "nModified":0,
//        "n":0
//    }
//}
    public Error error;
    public Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    public void Error(Error error) {
        this.error = error;
    }
}
