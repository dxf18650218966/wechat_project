package com.wechat;

import cn.hutool.core.lang.Console;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechat.minio.service.MinioService;
import com.wechat.tool.DateUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.wx.entity.AutoReplyBean;
import com.wechat.wx.mapper.AutoReplyMapper;
import com.wechat.wx.mapper.UserInfoMapper;
import com.wechat.wx.util.MaterialImgUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author dai
 * @Date 2020/10/31 
 */
@RunWith(SpringRunner.class)
// 因为business依赖了有主启动类的thirdparty，junit不知道加载哪个启动类，需要在junit的@SpringBootTest注解上指定启动类。
// 否则会报：junit报错Found multiple @SpringBootConfiguration annotated classes
@SpringBootTest(classes = {MallApplication.class})
@Slf4j
public class redisTest {
    @Autowired
    private  RedisTemplate<String,String> redisTemplate;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    MaterialImgUtil mediaUploadUtil;
    @Autowired
    AutoReplyMapper autoReplyMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    MinioService minioService;


    @Test
    public void test2() throws Exception{
        //String cardId = userInfoMapper.selectCardIdByPhone("18650218966");
       // minioService.putObject("https://web-cdn.agora.io/website-files/images/homepage-solution-xiaotiancai.png");

        QueryWrapper<AutoReplyBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("has_valid",1).or(autoReply -> autoReply.eq("gzh","gh_babca4b63868").eq("id","4400000000000005"));
        Page<AutoReplyBean> page = new Page<>(1, 2);
        IPage<AutoReplyBean> list = autoReplyMapper.selectPage(page, queryWrapper);

        // 分页查询出来的结果集
        List<AutoReplyBean> records = list.getRecords();
        records.forEach(autoReply -> {
            Console.log(autoReply);
        });

        // 总页数
        long pages = list.getPages();
        Console.log(pages);

        // 当前页数
        long current = list.getCurrent();
        Console.log(current);


        // 每页显示多少条
        long size = list.getSize();
        Console.log(size);


        // 总记录数
        long total = list.getTotal();
        Console.log(total);
        log.error("error");
        log.info("info");
        log.debug("error");

    }
}
