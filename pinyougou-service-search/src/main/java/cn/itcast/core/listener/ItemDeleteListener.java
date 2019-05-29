package cn.itcast.core.listener;

import cn.itcast.core.service.search.ItemSearchService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @ClassName ItemDeleteListener
 * @Description 自定义消息监听器：商品下架
 * @Author 传智播客
 * @Date 9:53 2019/5/17
 * @Version 2.1
 **/
public class ItemDeleteListener implements MessageListener {

    @Resource
    private ItemSearchService itemSearchService;

    /**
     * @author 栗子
     * @Description 获取消息并且消费消息
     * @Date 9:54 2019/5/17
     * @param message
     * @return void
     **/
    @Override
    public void onMessage(Message message) {

        try {
            // 获取消息
            ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
            String id = activeMQTextMessage.getText();
            System.out.println("商品下架的id：" + id);
            // 消费消息
            itemSearchService.deleteItemFromSolr(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
