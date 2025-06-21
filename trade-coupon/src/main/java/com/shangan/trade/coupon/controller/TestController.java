package com.shangan.trade.coupon.controller;

import com.shangan.trade.coupon.mq.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @Autowired
    private MessageSender messageSender;

    @RequestMapping("/coupon/test")
    @ResponseBody
    public  String test(){
        return  "hello world";
    }


    @RequestMapping("/sendMessage/{message}")
    @ResponseBody
    public String sendMessage(@PathVariable("message") String message){
        messageSender.sendMessage("test-topic",message);
        return "发送消息:"+message;
    }
}
