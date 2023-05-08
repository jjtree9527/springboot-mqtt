package top.inson.boot.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.inson.boot.entity.MqttReceiver;
import top.inson.boot.entity.MqttSender;

/**
 * @author jingjitree
 * @version 1.0
 * @className MqttConstant
 * @description
 * @date 2022/3/27 12:16
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttConstant {

    private String url;
    private Integer timeout;

    private MqttSender sender;
    private MqttReceiver receiver;
}
