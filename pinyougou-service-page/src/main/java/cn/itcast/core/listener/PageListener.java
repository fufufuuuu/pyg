package cn.itcast.core.listener;

import cn.itcast.core.service.staticpage.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @ClassName PageListener
 * @Description 自定义消息监听器：生成静态页
 * @Author 传智播客
 * @Date 19:42 2019/5/15
 * @Version 2.1
 **/
public class PageListener implements MessageListener {

    @Resource
    private StaticPageService staticPageService;

    /**
     * @author 栗子
     * @Description 获取消息并且消费消息
     * @Date 19:42 2019/5/15
     * @param message
     * @return void
     **/
    @Override
    public void onMessage(Message message) {

        try {
            // 获取消息
            ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
            String id = activeMQTextMessage.getText();
            System.out.println("page消费者获取的商品id："+id);
            // 消费消息
            staticPageService.getHtml(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
