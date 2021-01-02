package com.wechat.es.document;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 测试案例
 * @Author dai
 * @Date 2020/12/29
 */
@Data
@Document(indexName="es_decument1")
public class EsDecument {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    /**
     * 总数量
     */
    @Field(type = FieldType.Integer )
    private int totalNum;

    /**
     * 库存
     */
    @Field(type = FieldType.Long )
    private Long inventory;

    /**
     * 金额
     */
    @Field(type = FieldType.Double )
    private double price;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Keyword )
    private String createTime;
}
