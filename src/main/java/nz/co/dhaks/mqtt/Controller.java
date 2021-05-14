package nz.co.dhaks.mqtt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

import nz.co.dhaks.mqtt.client.MQTTClient;
import nz.co.dhaks.mqtt.qr.ExternalProcesses;
import nz.co.dhaks.mqtt.qr.TempFiles;

public class Controller {
	public static void main(String[] args) throws WriterException, IOException, NotFoundException, MqttException {
		// Initialize MQTT Client
		MQTTClient.initialize();
		int count = 1;
		while (true) {
			Path path = Files.createTempFile(null, ".PNG");

			// Read data through Zbarcam using STD IN
			String payload = new ExternalProcesses().getBufferedReader().readLine();

			new Thread(new Runnable() {

				@Override
				public void run() {

					System.out.println("QR Code - " + payload);

					MQTTClient.publish(payload);

				}
			}).start();

			new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						// Write to temp file
						TempFiles.writeAsQR(payload, path);
						String message = TempFiles.readQRFile(path.toAbsolutePath().toString());
//						String message="this is new message";

						// System.out.println("QR Code - "+message + ++count);
						System.out.println("QR Code - " + message);
					} catch (NotFoundException | IOException | WriterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// MQTTClient.publish(message);

				}
			});// .start();
		}

	}
}
