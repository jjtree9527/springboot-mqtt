package top.inson.boot.config;

import cn.hutool.core.util.StrUtil;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import top.inson.boot.constant.MqttConstant;

/**
 * @author jingjitree
 * @version 1.0
 * @className MqttSenderConfig
 * @description mqtt生产者
 * @date 2022/3/27 12:24
 **/
@Configuration
public class MqttSenderConfig {
    public static final String CHANNEL_NAME_OUT = "mqttIotOutChannel";

    @Autowired
    private MqttConstant mqttConstant;

    /**
     * mqtt连接器选项
     * @return
     */
    @Bean
    MqttConnectOptions senderMqttConnectOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        if (StrUtil.isNotBlank(mqttConstant.getSender().getUsername())){
            options.setUserName(mqttConstant.getSender().getUsername());
        }
        if (StrUtil.isNotBlank(mqttConstant.getSender().getPassword())){
            options.setPassword(mqttConstant.getSender().getPassword().toCharArray());
        }
        options.setServerURIs(StrUtil.split(mqttConstant.getUrl(), ","));
        //超时时间，单位 秒
        options.setConnectionTimeout(mqttConstant.getTimeout());
        //设置会话心跳时间单位秒 服务器每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
        options.setKeepAliveInterval(20);
        return options;
    }

    /**
     * mqtt客户端
     */
    @Bean
    MqttPahoClientFactory senderMqttPahoClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(this.senderMqttConnectOptions());
        return factory;
    }

    /**
     * mqtt信号通道（生产者）
     * @return
     */
    @Bean(name = CHANNEL_NAME_OUT)
    MessageChannel senderMessageChannel(){

        return new DirectChannel();
    }

    /**
     * mqtt消息处理器（生产者）
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
    MessageHandler senderMessageHandler(){
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(
                mqttConstant.getSender().getClientId(),
                this.senderMqttPahoClientFactory());
        handler.setAsync(true);
        handler.setDefaultTopic(mqttConstant.getSender().getDefaultTopic());
        return handler;
    }

}
