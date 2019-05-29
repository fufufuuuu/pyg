package cn.itcast.core.vo;

import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.item.Item;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName GoodsVo
 * @Description 封装页面的商品数据
 * @Author 传智播客
 * @Date 11:34 2019/5/6
 * @Version 2.1
 **/
public class GoodsVo implements Serializable {

    private Goods goods;            // 商品基本
    private GoodsDesc goodsDesc;    // 商品描述信息
    private List<Item> itemList;    // 商品对应的库存

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(GoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
