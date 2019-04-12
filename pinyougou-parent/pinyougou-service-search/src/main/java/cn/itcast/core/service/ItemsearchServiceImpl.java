package cn.itcast.core.service;

import cn.itcast.core.pojo.item.Item;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.*;

/**
 * 搜索管理
 */
@Service
public class ItemsearchServiceImpl implements ItemsearchService {

    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {

        Map<String, Object> resultMap = new HashMap<>();

        //1:商品分类  去重  分组 手机 选号入网
        List<String> categoryList = searchCategoryListByKeywords(searchMap);
        resultMap.put("categoryList", categoryList);

        if (null != categoryList && categoryList.size() > 0) {
            //2:品牌结果集
            //3:规格结果集
            resultMap.putAll(searchBrandListAndSpecListByCategory(categoryList.get(0)));

        }
        //4：查询分页结果集
        /* resultMap.putAll(searchPageByKeywords(searchMap)); */
        resultMap.putAll(searchHighlightPageByKeywords(searchMap));

        return resultMap;
    }

    //查询品牌及规格结果集
    public Map<String, Object> searchBrandListAndSpecListByCategory(String category) {
        //从缓存中查询
        Map<String, Object> resultMap = new HashMap<>();

        // 1：根据商品分类名称 查询模板ID
        Object typeId = redisTemplate.boundHashOps("itemCatList").get(category);
        //2:根据模板ID 查询品牌结果集 及规格结果集
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);
        List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeId);

        resultMap.put("brandList", brandList);
        resultMap.put("specList", specList);

        return resultMap;
    }

    //查询商品分类 按分组查询
    public List<String> searchCategoryListByKeywords(Map<String, String> searchMap) {

        //关键词
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        Query query = new SimpleQuery(criteria);

        //设置分组域  商品分类
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");
        query.setGroupOptions(groupOptions);

        //创建商品分类结果集
        List<String> categoryList = new ArrayList<>();

        //执行查询
        GroupPage<Item> page = solrTemplate.queryForGroupPage(query, Item.class);

        GroupResult<Item> item_category = page.getGroupResult("item_category");

        Page<GroupEntry<Item>> groupEntries =
                item_category.getGroupEntries();

        List<GroupEntry<Item>> content = groupEntries.getContent();
        if (null != content && content.size() > 0) {
            for (GroupEntry<Item> itemGroupEntry : content) {
                categoryList.add(itemGroupEntry.getGroupValue());

            }

        }

        //page.get

        return categoryList;
    }

    //查询分页高亮结果集
    public Map<String, Object> searchHighlightPageByKeywords(Map<String, String> searchMap) {
        Map<String, Object> resultMap = new HashMap<>();


        //处理空格
        searchMap.put("keywords",searchMap.get("keywords").replaceAll(" ",""));


        //关键词   三 星   手  机 5个字母
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));


        //SpringDataSolr  solrJ  OR AND
        HighlightQuery highlightQuery = new SimpleHighlightQuery(criteria);

        //        //过滤条件
        //1:商品分类名称
        if (null != searchMap.get("category") && !"".equals(searchMap.get("category").trim())) {

            FilterQuery fq = new SimpleFilterQuery
                    (new Criteria("item_category").is(searchMap.get("category").trim()));
            highlightQuery.addFilterQuery(fq);
        }

        //2K品牌名称
        if (null != searchMap.get("brand") && !"".equals(searchMap.get("brand").trim())) {

            FilterQuery fq = new SimpleFilterQuery
                    (new Criteria("item_brand").is(searchMap.get("brand").trim()));
            highlightQuery.addFilterQuery(fq);
        }

        //3:规格 （多个规格）
        if (null != searchMap.get("spec") && !"".equals(searchMap.get("spec").trim())) {

            Map<String, String> specMap = JSON.parseObject(searchMap.get("spec"), Map.class);
            Set<Map.Entry<String, String>> entries = specMap.entrySet();

            for (Map.Entry<String, String> entry : entries) {
                FilterQuery fq = new SimpleFilterQuery
                        (new Criteria("item_spec_" + entry.getKey()).is(entry.getValue()));
                highlightQuery.addFilterQuery(fq);
            }

        }
        //查询索引库
        // $scope.searchMap={'price':'0-500或3000-*','pageNo':1,'pageSize':40,'sort':'','sortField':''};
        //4价格区间
        if (null != searchMap.get("price") && !"".equals(searchMap.get("price"))) {
            String[] p = searchMap.get("price").split("-");
            FilterQuery fq = null;
            if (p[1].equals("*")) {// searchMap.get(price).constats("*"

                fq = new SimpleFilterQuery
                        (new Criteria("item_price").greaterThanEqual(p[0]));
            } else {
                fq = new SimpleFilterQuery
                        (new Criteria("item_price").between(p[0], p[1]));

            }
            highlightQuery.addFilterQuery(fq);
        }

        //排序
        //综合
        //新品

        // $scope.searchMap={'sort':'DESC','sortField':'updatetime或price'};
        if(null != searchMap.get("sort") && !"".equals(searchMap.get("sort"))){

            if("ASC".equals(searchMap.get("sort"))){

                Sort sort = new Sort(Sort.Direction.ASC,"item_" + searchMap.get("sortField"));

                highlightQuery.addSort(sort);
            }else{
                Sort sort = new Sort(Sort.Direction.DESC,"item_" + searchMap.get("sortField"));
                highlightQuery.addSort(sort);

            }

        }
        //价格由高到低
        //价格由低到高


        //打开高亮开关

        //设置高亮的域
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");

        highlightQuery.setHighlightOptions(highlightOptions);
        //分页
        String pageNo = searchMap.get("pageNo");
        String pageSize = searchMap.get("pageSize");
        highlightQuery.setOffset((Integer.parseInt(pageNo) - 1) * Integer.parseInt(pageSize));
        highlightQuery.setRows(Integer.parseInt(pageSize));

        //执行查询
        HighlightPage<Item> page = solrTemplate.queryForHighlightPage(highlightQuery, Item.class);

        //高亮集合
        List<HighlightEntry<Item>> highlighted = page.getHighlighted();
        for (HighlightEntry<Item> itemHighlightEntry : highlighted) {

            //普通名称的
            Item entity = itemHighlightEntry.getEntity();
            List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();
            if (null != highlights && highlights.size() > 0) {
                //高亮的值
                entity.setTitle(highlights.get(0).getSnipplets().get(0));
            }
        }

        //结果集
        resultMap.put("rows", page.getContent());
        resultMap.put("total", page.getTotalElements());
        resultMap.put("totalPages", page.getTotalPages());
        return resultMap;

    }

    //查询分页结果集
    public Map<String, Object> searchPageByKeywords(Map<String, String> searchMap) {
        Map<String, Object> resultMap = new HashMap<>();
        //定义搜索对象的结构  category:商品分类
        // $scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};

        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        Query query = new SimpleQuery(criteria);

        //分页
        String pageNo = searchMap.get("pageNo");
        String pageSize = searchMap.get("pageSize");
        query.setOffset((Integer.parseInt(pageNo) - 1) * Integer.parseInt(pageSize));
        query.setRows(Integer.parseInt(pageSize));
        //执行查询
        ScoredPage<Item> page = solrTemplate.queryForPage(query, Item.class);

        //结果集
        resultMap.put("rows", page.getContent());
        resultMap.put("total", page.getTotalElements());
        resultMap.put("totalPages", page.getTotalPages());
        return resultMap;
    }

}
