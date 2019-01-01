package entity;

import java.io.Serializable;

public class Result implements Serializable{
    private Boolean flag;
    private String msg;

    public Result() {
    }

    public Result(Boolean flag, String msg) {

        this.flag = flag;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "flag=" + flag +
                ", msg='" + msg + '\'' +
                '}';
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
