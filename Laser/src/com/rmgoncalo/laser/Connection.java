/*
 * Socket.io
 * 
 * @Var: String ip
 * @Var: String port
 */

package com.rmgoncalo.laser;

import java.net.MalformedURLException;

import org.json.JSONObject;

import android.util.Log;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

public class Connection {

	private final static String tag = "Laser-SocketIO";
	private String ip;
	private String port;
	private SocketIO socket;

	public Connection(String ip, String port) {
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void init() throws MalformedURLException {
		socket = new SocketIO(ip + ":" + port);
		Log.d(tag, ip +":"+ port);
		
		socket.connect(new IOCallback() {

			@Override
			public void onMessage(JSONObject arg0, IOAcknowledge arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMessage(String arg0, IOAcknowledge arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(SocketIOException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDisconnect() {
				Log.d(tag, "onDisconnect");
			}

			@Override
			public void onConnect() {
				Log.d(tag, "onConnect");
			}

			@Override
			public void on(String arg0, IOAcknowledge arg1, Object... arg2) {
				// if ("position".equals(arg0) && arg2.length > 0) {
				// Log.d(tag, "" + arg2[0]);
				// }

			}
		});

	}

	/*
	 * sends a point position using socket.io
	 */
	public void sendPosition(JSONObject jObj) {

		if (socket == null) {
			Log.d(tag, "Socket is null");
			return;
		}

		socket.emit("echo", jObj);
	}
}
