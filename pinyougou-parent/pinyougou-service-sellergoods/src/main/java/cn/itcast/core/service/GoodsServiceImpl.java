package cn.itcast.core.service;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;
import vo.GoodsVo;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.swing.*;
import java.util.*;

/**
 * 商品管理
 */
@SuppressWarnings("all")
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsDescDao goodsDescDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private BrandDao brandDao;

    //添加
    @Override
    public void add(GoodsVo vo) {

        //商品表
        //审核状态
        vo.getGoods().setAuditStatus("0");
        //添加商品表
        goodsDao.insertSelective(vo.getGoods());
        //设置跟商品表使用同一个主键
        vo.getGoodsDesc().setGoodsId(vo.getGoods().getId());
        //添加商品详情表
        goodsDescDao.insertSelective(vo.getGoodsDesc());

        //判断是否启用规格
        if ("1".equals(vo.getGoods().getIsEnableSpec())) {
            //库存表
            List<Item> itemList = vo.getItemList();
            for (Item item : itemList) {

                //标题  = 商品名称 + " " + 规格1 + " “ + 规格2 .....

                String title = vo.getGoods().getGoodsName();
                //  {"机身内存":"16G","网络":"联通3G"}
                String spec = item.getSpec();
                Map<String, String> specMap = JSON.parseObject(spec, Map.class);

                Set<Map.Entry<String, String>> entries = specMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    title += " " + entry.getValue();
                }
                item.setTitle(title);
                //图片 第一
                //[{"color":"粉色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXq2AFIs5AAgawLS1G5Y004.jpg"},{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXrWAcIsOAAETwD7A1Is874.jpg"}]

                List<Map> images = JSON.parseArray(vo.getGoodsDesc().getItemImages(), Map.class);
                if (null != images && images.size() > 0) {
                    item.setImage((String) images.get(0).get("url"));
                }
                //第三级商品分类的ID
                item.setCategoryid(vo.getGoods().getCategory3Id());
                //第三级商品分类的名称
                item.setCategory(itemCatDao.selectByPrimaryKey(vo.getGoods().getCategory3Id()).getName());
                //添加时间
                item.setCreateTime(new Date());
                item.setUpdateTime(new Date());

                //外键
                item.setGoodsId(vo.getGoods().getId());
                //商家的ID
                item.setSellerId(vo.getGoods().getSellerId());
                //商家的名称
                item.setSeller(sellerDao.selectByPrimaryKey(vo.getGoods().getSellerId()).getNickName());

                //品牌名称
                item.setBrand(brandDao.selectByPrimaryKey(vo.getGoods().getBrandId()).getName());

                //保存库存 表
                itemDao.insertSelective(item);
            }

        } else {
            //默认
        }

    }

    //查询分页对象
    @Override
    public PageResult search(Integer page, Integer rows, Goods goods) {

        //分页插件
        PageHelper.startPage(page, rows);

        //分页插件 排序
        //1:办法：PageHelper.orderBy("id desc");
        //

        //条件 对象
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();

        //排序
        goodsQuery.setOrderByClause("id desc");

        //判断 状态  运营商后台 默认查询所有状态
        if (null != goods.getAuditStatus() && !"".equals(goods.getAuditStatus())) {
            criteria.andAuditStatusEqualTo(goods.getAuditStatus());
        }
        //商品名称
        if (null != goods.getGoodsName() && !"".equals(goods.getGoodsName().trim())) {
            criteria.andGoodsNameLike("%" + goods.getGoodsName().trim() + "%");
        }

        //判断当前查询的是商家后台 还是运营商后台
        if (null != goods.getSellerId()) {
            //商家查询
            //只能查询自己家的商品
            criteria.andSellerIdEqualTo(goods.getSellerId());
        }
        //运营商查询  没有商家ID 查询所有商家的

        //只查询不删除
        criteria.andIsDeleteIsNull();

        Page<Goods> p = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        //ArrayList arrayList = new ArrayList();//Ctrl + alt + V
        //select * from tb_goods where ....  order by id desc limit 开始行,每页数  Mybatis -- dataSource -- Mysql

        return new PageResult(p.getTotal(), p.getResult());
    }

    //查询一个GoodsVo
    @Override
    public GoodsVo findOne(Long id) {

        GoodsVo vo = new GoodsVo();
        //商品对象
        vo.setGoods(goodsDao.selectByPrimaryKey(id));
        //商品详情对象
        vo.setGoodsDesc(goodsDescDao.selectByPrimaryKey(id));

        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        //库存结果集
        vo.setItemList(itemDao.selectByExample(itemQuery));
        return vo;
    }

    //修改
    @Override
    public void update(GoodsVo vo) {
        //1:商品表
        goodsDao.updateByPrimaryKeySelective(vo.getGoods());
        //2:商品详情表
        goodsDescDao.updateByPrimaryKeySelective(vo.getGoodsDesc());
        //3:先删除 再添加
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(vo.getGoods().getId());
        itemDao.deleteByExample(itemQuery);

        //判断是否启用规格
        if ("1".equals(vo.getGoods().getIsEnableSpec())) {
            //库存表
            List<Item> itemList = vo.getItemList();
            for (Item item : itemList) {

                //标题  = 商品名称 + " " + 规格1 + " “ + 规格2 .....

                String title = vo.getGoods().getGoodsName();
                //  {"机身内存":"16G","网络":"联通3G"}
                String spec = item.getSpec();
                Map<String, String> specMap = JSON.parseObject(spec, Map.class);

                Set<Map.Entry<String, String>> entries = specMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    title += " " + entry.getValue();
                }
                item.setTitle(title);
                //图片 第一
                //[{"color":"粉色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXq2AFIs5AAgawLS1G5Y004.jpg"},{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXrWAcIsOAAETwD7A1Is874.jpg"}]

                List<Map> images = JSON.parseArray(vo.getGoodsDesc().getItemImages(), Map.class);
                if (null != images && images.size() > 0) {
                    item.setImage((String) images.get(0).get("url"));
                }
                //第三级商品分类的ID
                item.setCategoryid(vo.getGoods().getCategory3Id());
                //第三级商品分类的名称
                item.setCategory(itemCatDao.selectByPrimaryKey(vo.getGoods().getCategory3Id()).getName());
                //添加时间
                item.setCreateTime(new Date());
                item.setUpdateTime(new Date());

                //外键
                item.setGoodsId(vo.getGoods().getId());
                //商家的ID
                item.setSellerId(vo.getGoods().getSellerId());
                //商家的名称
                item.setSeller(sellerDao.selectByPrimaryKey(vo.getGoods().getSellerId()).getNickName());

                //品牌名称
                item.setBrand(brandDao.selectByPrimaryKey(vo.getGoods().getBrandId()).getName());

                //保存库存 表
                itemDao.insertSelective(item);
            }

        } else {
            //默认
        }

    }



    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination topicPageAndSolrDestination;
    @Autowired
    private Destination queueSolrDeleteDestination;

    //开始审核
    //status :为1时 审核通过  2时：不通过
    @Override
    public void updateStatus(Long[] ids, String status) {

        Goods goods = new Goods();
        //状态
        goods.setAuditStatus(status);

        for (Long id : ids) {
            //商品ID
            //商品详情表
            //库存表  多个库存

            goods.setId(id);
            //1:更新商品的审核状态
            goodsDao.updateByPrimaryKeySelective(goods);
            //判断是否为审核通过
            if("1".equals(status)){

                //发消息 目的地
               jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                   @Override
                   public Message createMessage(Session session) throws JMSException {
                       //五大类型 TextMessage
                       return  session.createTextMessage(String.valueOf(id));// null + ""  "null"
                   }
               });


            }

        }

    }

    //删除
    @Override
    public void delete(Long[] ids) {
        Goods goods = new Goods();
        //已删除状态
        goods.setIsDelete("1");


        //JSON.parse 返回值是一个Object类型 一个对象 {k:v}
        //JSON.parseObject 返回值 是一个指定了类型的一个对象  {k:v}
        //JSON.parseArray 返回值 一个指定了类型的集合对象 [{},{}]


        for (Long id : ids) {
            //1:更新是否删除状态
            goods.setId(id);
            goodsDao.updateByPrimaryKeySelective(goods);

            jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    //五大类型 TextMessage
                    return  session.createTextMessage(String.valueOf(id));// null + ""  "null"
                }
            });



        }
    }
}
