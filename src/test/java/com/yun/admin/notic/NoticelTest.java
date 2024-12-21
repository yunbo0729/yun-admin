package com.yun.admin.notic;

import com.yun.admin.notic.service.NoticeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@SpringBootTest()
@RunWith(SpringJUnit4ClassRunner.class)
public class NoticelTest {

    @Autowired
    private NoticeService noticeService;
    @Test
    public void sendByMd(){
        noticeService.sendByMd("aaaaaaaaaaa");
    }
}