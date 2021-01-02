package com.wechat.es.mapping;

import com.wechat.es.document.EsDecument;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author dxf
 * @version 1.0
 * @date 2020/12/29 21:29
 */
public interface EsMapper extends ElasticsearchRepository<EsDecument,String> {
    /**
     * 通过名称分词查询，按价格区间筛选，并高亮显示
     * @param name 名称
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param pageable 分页
     * @return
     */
     @Highlight(
        fields = {
          @HighlightField(name = "name")
        },
        parameters = @HighlightParameters(
                preTags = "<strong><font style='color:red'>",
                postTags = "</font></strong>"
        )
    )
    List<SearchHit<EsDecument>> findByNameAndPriceBetweenOrderByPriceDesc(String name, double minPrice, double maxPrice, PageRequest pageable);

}
