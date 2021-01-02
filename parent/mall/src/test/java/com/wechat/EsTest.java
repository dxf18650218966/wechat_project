package com.wechat;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import com.wechat.es.document.EsDecument;
import com.wechat.es.mapping.EsMapper;
import com.wechat.tool.DateUtil;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 搜索引擎测试
 * @Author dai
 * @Date 2020/12/30 
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MallApplication.class})
public class EsTest {
    @Autowired
    EsMapper esMapper;
    @Test
    public void save(){
        ArrayList<EsDecument> arrayList = new ArrayList<EsDecument>();
        for (int i = 0; i < 10; i++) {
            EsDecument esDecument = new EsDecument();
            esDecument.setId(RandomUtil.randomString(15));
            esDecument.setCreateTime(DateUtil.nowTime());
            esDecument.setName("红红的苹果，小米pro"+i);
            esDecument.setPrice(RandomUtil.randomNumber());
            esDecument.setInventory(RandomUtil.randomLong());
            esDecument.setTotalNum(RandomUtil.randomInt());
            arrayList.add(esDecument);
        }
        esMapper.saveAll(arrayList);
    }

    @Test
    public void highlight(){
        int page = 0; // 页数，从0开始
        int size = 10;
        PageRequest pageable = PageRequest.of(page, size);
        List<SearchHit<EsDecument>> list = esMapper.findByNameAndPriceBetweenOrderByPriceDesc("苹果",50,60,pageable );
        list.forEach(esDecument -> {
            // EsDecument对象
            Console.log(esDecument.getContent());
            // 对象高亮显示的字段
            Console.log(esDecument.getHighlightField("name").get(0));
        });
    }

    @Test
    public void dd(){
        int page = 0;
        int size = 20;
        // 创建自定义查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
        .withQuery(QueryBuilders.matchQuery("name","苹果"));
        HighlightBuilder.Field allHighLight = new HighlightBuilder.Field("name").preTags("aa").postTags("bb");
        queryBuilder.withHighlightFields(allHighLight);
        // 排序
        queryBuilder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.ASC));//ASC\DESC
        // 分页
        queryBuilder.withPageable(PageRequest.of(page, size));
        Page<EsDecument> items  = esMapper.search(queryBuilder.build());
        for (EsDecument esDecument : items) {
            Console.log(esDecument);
        }
    }
}
