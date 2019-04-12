package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 静态化程序实现类
 */
@Service
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware {


    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private ItemDao itemDao;
    @Autowired
    private GoodsDescDao goodsDescDao;
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private GoodsDao goodsDao;


    //静态化程序
    // 手动点击 审核通过
    // 半夜     定时器
    public void index(Long id){

        //1:获取有模板目录的 COnfiguration
        Configuration configuration = freeMarkerConfigurer.getConfiguration();

        //路径 webapps 路径  绝对路径
        String path = getPath("/"+id+".html");

        //D:\ideaProject\111\pinyougou-parent\pinyougou-service-sellergoods\target\pinyougou-service-sellergoods-1.0-SNAPSHOT\id.html
        Writer out = null;
        //加载模板 当初 模板是什么编码 UTF-8
        try {
            //  读
            Template template = configuration.getTemplate("item.ftl");

            //数据
            Map<String,Object> root = new HashMap<>();

            //库存结果集  商品ID外键
            ItemQuery itemQuery = new ItemQuery();
            itemQuery.createCriteria().andGoodsIdEqualTo(id);
            List<Item> itemList =
                    itemDao.selectByExample(itemQuery);
            root.put("itemList",itemList);

            //商品详情表  商品表  商品ID　　商品详情ID
            GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
            root.put("goodsDesc",goodsDesc);

            //查询商品对象
            Goods goods = goodsDao.selectByPrimaryKey(id);
            root.put("goods",goods);

            //查询 一级分类名称
            root.put("itemCat1",itemCatDao.selectByPrimaryKey(goods.getCategory1Id()).getName());
            //查询 二级分类名称
            root.put("itemCat2",itemCatDao.selectByPrimaryKey(goods.getCategory2Id()).getName());
            //查询 三级分类名称
            root.put("itemCat3",itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());

            //输出流 写
            out = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");

            //处理
            template.process(root,out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(null != out){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    //获取全部路径
    public String getPath(String path){
        return servletContext.getRealPath(path);
    }

    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {
       this.servletContext = servletContext;
    }
}
