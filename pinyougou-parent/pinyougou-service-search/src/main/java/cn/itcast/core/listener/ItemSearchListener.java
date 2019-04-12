package cn.itcast.core.listener;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;

/**
 * 自定义消息处理类
 */
public class ItemSearchListener implements MessageListener{


    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private ItemDao itemDao;
    //接收消息
    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;

        try {
            //商品ID
            String id = atm.getText();
            System.out.println("搜索项目接收到的ID：" + id);

            //2:保存商品信息到索引库 待办事宜

            //商品ID外键 查询多个库存数据  是否为默认 1  返回值 一条
            ItemQuery itemQuery = new ItemQuery();
            itemQuery.createCriteria().andGoodsIdEqualTo(Long.parseLong(id)).andIsDefaultEqualTo("1");
            List<Item> itemList = itemDao.selectByExample(itemQuery);
            solrTemplate.saveBeans(itemList,1000);



        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
