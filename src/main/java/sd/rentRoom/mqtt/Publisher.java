package sd.rentRoom.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Publisher {
    private String broker = "tcp://broker.mqttdashboard.com:1883";
    private String client = "RentRoom";
    private MqttClient mqttClient;
    private MqttConnectOptions mqttOptions;
    private int qos = 2;
    public Publisher() throws Exception
    {
        mqttClient = new MqttClient(broker,client,new MemoryPersistence());
        mqttOptions = new MqttConnectOptions();
        mqttOptions.setCleanSession(true);
    }

    public void sendNotification(String topic, String msg) throws Exception
    {
        mqttClient.connect();
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(this.qos);
        mqttClient.publish(topic,message);
    }

    public void mqttDisconect() throws Exception
    {
        mqttClient.disconnect();
        mqttClient.close();

    }

}
