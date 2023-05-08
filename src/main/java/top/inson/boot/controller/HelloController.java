package top.inson.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.inson.boot.sender.IMqttSender;

/**
 * @author jingjitree
 * @version 1.0
 * @className HelloController
 * @description
 * @date 2022/3/27 15:10
 **/
@RestController
@RequestMapping("/hello")
public class HelloController {
    @Autowired
    private IMqttSender mqttSender;

    @PostMapping("/sendMsg")
    public void sendMsg(@RequestBody String body){

        mqttSender.sendToMqtt(body);
    }

    @PostMapping("/sendTopic")
    public void sendTopic(@RequestHeader String topic,
            @RequestBody String body){

        mqttSender.sendToMqtt(topic, body);
    }

}
