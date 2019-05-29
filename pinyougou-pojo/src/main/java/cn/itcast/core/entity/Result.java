package cn.itcast.core.entity;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description 封装保存或更新操作
 * @Author 传智播客
 * @Date 12:48 2019/4/28
 * @Version 2.1
 **/
public class Result implements Serializable {

    private boolean flag;   // 操作成功or失败
    private String message; // 操作的信息

    public Result(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
