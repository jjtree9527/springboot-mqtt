package top.inson.boot.sender;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import top.inson.boot.config.MqttSenderConfig;

@Component
@MessagingGateway(defaultRequestChannel = MqttSenderConfig.CHANNEL_NAME_OUT)
public interface IMqttSender {

    /**
     * 发送信息到mqtt服务器
     * @param data
     */
    void sendToMqtt(String data);

    /**
     * 发送信息到mqtt服务器
     * @param topic
     * @param payload
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);

    /**
     * 发送信息到mqtt服务器
     * @param topic
     * @param qos
     * @param payload
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic,
                    @Header(MqttHeaders.QOS) Integer qos,
                    String payload);
}
