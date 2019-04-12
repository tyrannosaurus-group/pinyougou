package cn.itcast.core.listener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 删除 索引
 */
public class ItemDeleteListener implements MessageListener {

    @Autowired
    private SolrTemplate solrTemplate;
    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;
        try {
            String id = atm.getText();

            //2:索引库中商品 得删除掉
            SolrDataQuery solrDataQuery = new SimpleQuery("item_goodsid:" + id);
            solrTemplate.delete(solrDataQuery);
            solrTemplate.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
