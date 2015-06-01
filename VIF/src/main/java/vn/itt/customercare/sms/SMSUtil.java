package vn.itt.customercare.sms;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SMSUtil {

	public static String receivedData = "";
	public static boolean receivedFinished = true;
	public static String sentData = "";
	public static boolean sentFinished = true;

	public synchronized static void sendSMS(String comPort, String phoneNumber, String message)
			throws SerialPortException {
		while ((!sentFinished) || (!receivedFinished)) {
			// Wait for other process finished. GMS modem can only take one
			// action at one time.
		}
		sentFinished = false;
		final SerialPort serialPort = new SerialPort(comPort);
		serialPort.openPort();
		serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		String data = "AT+CMGF=1" + (char) 13 + "AT+CMGS=\"" + phoneNumber + "\"" + (char) 13 + message + (char) 13
				+ ((char) 26) + (char) 13;
		sentData = "";
		serialPort.addEventListener(new SerialPortEventListener() {
			
			public void serialEvent(SerialPortEvent arg0) {
				try {
					String result = new String(serialPort.readBytes(serialPort.getInputBufferBytesCount()), "ASCII");
					// System.out.print(result);
					sentData += result;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		serialPort.writeString(data);
		new SendMonitorThread(serialPort).start();
		while (!sentFinished) {
		}
	}

	public synchronized static String getUnreadSMS(String comPort) throws SerialPortException {
		while ((!sentFinished) || (!receivedFinished)) {
			// Wait for other process finished. GMS modem can only take one
			// action at one time.
		}
		receivedFinished = false;
		final SerialPort serialPort = new SerialPort(comPort);
		serialPort.openPort();
		serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		// AT+CMGL=[REC UNREAD, REC READ, STO UNSENT, STO SENT, ALL]
		String data = "AT+CMGF=1" + (char) 13 + "AT+CMGL=\"REC UNREAD\"" + (char) 13;
		receivedData = "";
		serialPort.addEventListener(new SerialPortEventListener() {

			
			public void serialEvent(SerialPortEvent arg0) {
				try {
					String result = new String(serialPort.readBytes(serialPort.getInputBufferBytesCount()), "ASCII");
					// System.out.print(result);
					receivedData += result;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		serialPort.writeString(data);
		new ReceiveMonitorThread(serialPort).start();
		while (!receivedFinished) {
		}
		return receivedData;
	}

	private static class ReceiveMonitorThread extends Thread {
		private SerialPort serialPort;

		public ReceiveMonitorThread(SerialPort serialPort) {
			this.serialPort = serialPort;
		}

		public void run() {
			while (true) {
				int count = receivedData.split("\nOK").length - 1;
				count += receivedData.split("\nERROR").length - 1;
				if (count >= 2) {
					try {
						serialPort.closePort();
					} catch (Exception ex) {
					}
					receivedFinished = true;
					break;
				}
			}
		}
	}

	private static class SendMonitorThread extends Thread {
		private SerialPort serialPort;

		public SendMonitorThread(SerialPort serialPort) {
			this.serialPort = serialPort;
		}

		public void run() {
			while (true) {
				int count = sentData.split("\nOK").length - 1;
				count += sentData.split("\nERROR").length - 1;
				if (count >= 2) {
					try {
						serialPort.closePort();
					} catch (Exception ex) {
					}
					sentFinished = true;
					System.out.println("Sent message finished.");
					break;
				}
			}
		}
	}

	public static void main(String[] args) throws SerialPortException, InterruptedException {
		// sendSMS("COM3", "+841674568689", "Message from Java code.");
		String received = getUnreadSMS("COM12");
		System.out.println("=====================");
		System.out.println("Received from SMS: ");
		System.out.println(received);
	}
}
