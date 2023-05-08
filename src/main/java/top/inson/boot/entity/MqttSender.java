package top.inson.boot.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jingjitree
 * @version 1.0
 * @className MqttSender
 * @description
 * @date 2022/3/27 12:18
 **/
@Getter
@Setter
public class MqttSender {
    private String username;
    private String password;

    private String clientId;
    private String defaultTopic;
    private String channelName;

}
