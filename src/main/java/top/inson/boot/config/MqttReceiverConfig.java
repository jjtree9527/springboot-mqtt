package top.inson.boot.config;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import top.inson.boot.constant.MqttConstant;

/**
 * @author jingjitree
 * @version 1.0
 * @className MqttReceiverConfig
 * @description mqtt消息订阅
 * @date 2022/3/27 14:36
 **/
@Slf4j
@Configuration
public class MqttReceiverConfig {
    public static final String CHANNEL_NAME_IN = "mqttIotInChannel";
    @Autowired
    private MqttConstant mqttConstant;

    /**
     * mqtt连接器选项
     * @return
     */
    @Bean
    MqttConnectOptions receiverMqttConnectOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        if (StrUtil.isNotBlank(mqttConstant.getReceiver().getUsername())){
            options.setUserName(mqttConstant.getReceiver().getUsername());
        }
        if (StrUtil.isNotBlank(mqttConstant.getReceiver().getPassword())){
            options.setPassword(mqttConstant.getReceiver().getPassword().toCharArray());
        }
        options.setServerURIs(StrUtil.split(mqttConstant.getUrl(), ","));
        //超时时间，单位 秒
        options.setConnectionTimeout(mqttConstant.getTimeout());
        //设置会话心跳时间单位秒 服务器每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
        //但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        return options;
    }

    /**
     * mqtt客户端
     * @return
     */
    @Bean
    MqttPahoClientFactory receiverMqttPahoClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(this.receiverMqttConnectOptions());
        return factory;
    }

    /**
     * mqtt信息通道（消费者）
     */
    @Bean(name = CHANNEL_NAME_IN)
    MessageChannel receiverMessageChannel(){

        return new DirectChannel();
    }

    /**
     * mqtt消息订阅绑定（消费者）
     * @return
     */
    @Bean
    MessageProducer receiverMessageProducer(){
        //可以同时消费（订阅）多个topic
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                        mqttConstant.getReceiver().getClientId(),
                        this.receiverMqttPahoClientFactory(),
                        StrUtil.split(mqttConstant.getReceiver().getDefaultTopic(), ","));
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        //设置订阅通道
        adapter.setOutputChannel(this.receiverMessageChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    MessageHandler receiverMessageHandler(){
        return (message) -> {
            String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
            String msgBody = message.getPayload().toString();
            log.info("接受到消息topic: {},内容msgBody：{}", topic, msgBody);
        };
    }

}
