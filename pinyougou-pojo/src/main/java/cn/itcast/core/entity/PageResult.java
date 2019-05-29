package cn.itcast.core.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PageResult
 * @Description 封装前端需要的分页数据
 * @Author 传智播客
 * @Date 11:40 2019/4/28
 * @Version 2.1
 *
 * pojo 实现虚拟化接口场景：网络传输、orm框架缓存
 * 好处：
 * 1、传输效率高
 * 2、灾备（容灾）
 * 3、不同平台数据可以共享
 **/

public class PageResult implements Serializable {

    private long total;     // 总条数
    private List rows;      // 结果集

    public PageResult(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
