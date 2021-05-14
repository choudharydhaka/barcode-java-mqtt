package nz.co.dhaks.mqtt.client;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MQTTClient {
	public static final String MQTT_HOST_PROTOCOL="tcp";
	public static final String MQTT_HOST_NAME="localhost";
	public static final String MQTT_HOST_PORT="1883";
	private static final String TOPIC = "qr/code";
	
 
	static IMqttClient publisher ;
	public static IMqttClient initialize() throws MqttException{
		
		String publisherId = UUID.randomUUID().toString();
		publisher = new MqttClient( MQTT_HOST_PROTOCOL + "://" + MQTT_HOST_NAME + ":" + MQTT_HOST_PORT,publisherId);
		
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		//options.setServerURIs(new String[] {MQTT_HOST_PROTOCOL+"://"+MQTT_HOST_NAME+MQTT_HOST_PORT});
		 
		
		
		//String publisherId = UUID.randomUUID().toString();
		try {
			//publisher = new MqttClient("tcp://iot.eclipse.org:1883",publisherId);
			publisher.connect(options);
			 startReceiver() ;
			return publisher;
			
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
      public static void startReceiver()  {
    	
    	  CountDownLatch receivedSignal = new CountDownLatch(100);
    	  try {
    		  
    		  // callback method
			publisher.subscribe(TOPIC, (topic, msg) -> {
			      byte[] payload = msg.getPayload();
			      // ... payload handling omitted
			      System.out.println("MQTT Client - Receive - Topic: "+ topic + ", Message: "+new String(payload));
			      receivedSignal.countDown();
			  });
			 receivedSignal.await(2, TimeUnit.SECONDS);
		} catch (MqttException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
    	 
      }
    
	     
	public static boolean publish(String data) {
		 
		   if ( !publisher.isConnected()) {
	            return false;
	        }           
	        MqttMessage msg = new MqttMessage(data.getBytes());
	        msg.setQos(0);
	        msg.setRetained(true);
	        try {
	        	System.out.println("MQTT Client - Sending - "+data);
				publisher.publish(TOPIC,msg);
				 return true;      
			} catch (MqttPersistenceException e) {
				 
				e.printStackTrace();
			} catch (MqttException e) {
				 
				e.printStackTrace();
			}        
	       
		return false;
		 
	}
	
	 
//	public static void main(String[] args) throws MqttException {
//		MQTTClient.initialize();
//		MQTTClient.publish("hello world\\n");
//	}
}
