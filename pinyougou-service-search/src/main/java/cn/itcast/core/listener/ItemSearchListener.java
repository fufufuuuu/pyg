package cn.itcast.core.listener;

import cn.itcast.core.service.search.ItemSearchService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @ClassName ItemSearchListener
 * @Description 自定义消息监听器：商品上架
 * @Author 传智播客
 * @Date 19:27 2019/5/15
 * @Version 2.1
 **/
public class ItemSearchListener implements MessageListener {

    @Resource
    private ItemSearchService itemSearchService;

    /**
     * @author 栗子
     * @Description 获取消费并且进行消费
     * @Date 19:28 2019/5/15
     * @param message
     * @return void
     **/
    @Override
    public void onMessage(Message message) {

        try {
            // 获取消息
            ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
            String id = activeMQTextMessage.getText();
            System.out.println("search消费者获取的商品id："+id);
            // 消费消息
            itemSearchService.saveItemToSolr(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
