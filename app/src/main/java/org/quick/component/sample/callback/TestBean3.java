package org.quick.component.sample.callback;

public class TestBean3<T> {

    /**
     * code : 0
     * msg : 请填写您的账号！
     */

    private T code;
    private String msg;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getCode() {
        return code;
    }

    public void setCode(T code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
