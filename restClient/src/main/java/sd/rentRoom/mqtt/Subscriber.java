package sd.rentRoom.mqtt;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Subscriber
{
    private String broker = "tcp://broker.mqttdashboard.com:1883";
    private String client = "";
    private MqttClient mqttClient;
    private MqttConnectOptions mqttOptions;
    private List<String> msg;
    public Subscriber(String client) throws Exception {
        mqttClient = new MqttClient(broker, client, new MemoryPersistence());
        mqttOptions = new MqttConnectOptions();
        mqttOptions.setCleanSession(true);
        msg = new ArrayList<String>();
        mqttClient.setCallback(new MqttCallback() {
                                   @Override
                                   public void connectionLost(Throwable throwable) {

                                   }

                                   @Override
                                   public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                                       msg.add(new String(mqttMessage.getPayload()));
                                   }

                                   @Override
                                   public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                                   }
                               }

        );
        mqttClient.connect(mqttOptions);

    }
    public void mqttSubscribe(String topic)throws Exception
    {
        mqttClient.subscribe(topic);
    }
    public List<String> mqttGetMsg(){
        List<String> rmsg = this.msg;
        this.msg = new ArrayList<String>();
        return rmsg;
    }

    public void mqttDisconect() throws Exception
    {
        mqttClient.disconnect();
        mqttClient.close();
    }

}
