package com.rmgoncalo.laser;

import java.net.MalformedURLException;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener,
		OnClickListener, OnSeekBarChangeListener {

	private final static String tag = "Laser-MainActivity";
	private final static String HTTP_PREFIX = "http://";

	private Button btnStart, btnStop;
	private TextView xAxis, yAxis, zAxis;
	private EditText ipText;
	private EditText portText;
	private SeekBar speedBar;
	private int speed = 0;

	private boolean started = false;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Connection connection;

	private int count = 0;
	private final float alpha = (float) 0.8; // constant for our filter below
	private float[] gravity = { 0, 0, 0 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnStart = (Button) findViewById(R.id.startButton);
		btnStop = (Button) findViewById(R.id.stopButton);

		ipText = (EditText) findViewById(R.id.ipEditText);
		portText = (EditText) findViewById(R.id.portEditText);
		speedBar = (SeekBar) findViewById(R.id.seekBar);

		xAxis = (TextView) findViewById(R.id.x_axis);
		yAxis = (TextView) findViewById(R.id.y_axis);
		zAxis = (TextView) findViewById(R.id.z_axis);

		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		speedBar.setOnSeekBarChangeListener(this);

		btnStart.setEnabled(true);
		btnStop.setEnabled(false);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (started) {
			super.onResume();
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (started) {
			mSensorManager.unregisterListener(this);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (started) {
			// Isolate the force of gravity with the low-pass filter.
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

			// Remove the gravity contribution with the high-pass filter.
			// speed: mouse sensitivity
			float x = (event.values[0] - gravity[0]) * speed;
			float y = (event.values[1] - gravity[1]) * speed;
			float z = (event.values[2] - gravity[2]) * speed;

			// count++;
			// Log.d(tag, "count: " + count);

			// if (count % 3 == 0) {

			xAxis.setText(String.valueOf(x));
			yAxis.setText(String.valueOf(y));
			zAxis.setText(String.valueOf(z));

			// Create new point
			Point p = new Point(x, y, z);

			// Send point to server
			try {
				connection.sendPosition(p.toJSON());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// }
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
	
	@Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
		speed = progress;
		Log.d(tag, "speed: " + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    	Log.d(tag, "onStartTrackingTouch");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    	Log.d(tag, speed + "/" + seekBar.getMax());
    	Toast.makeText(this, speed+"/"+seekBar.getMax(), Toast.LENGTH_SHORT).show();
    }
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.startButton:
			if (ipText.length() == 0 || portText.length() == 0) {
				Toast.makeText(this, "Insert IP and Port", Toast.LENGTH_SHORT)
						.show();
				break;
			}

			// TODO: Check if ip and port are correct values
			String ip = ipText.getText().toString();
			String port = portText.getText().toString();

			// append HTTP_PREFIX to ip
			String ip_final = new StringBuilder(HTTP_PREFIX).append(ip)
					.toString();

			connection = new Connection(ip_final, port);

			btnStart.setEnabled(false);
			btnStop.setEnabled(true);
			speedBar.setEnabled(false);
			started = true;
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_NORMAL);

			Toast.makeText(this, "Sending data to " + ip_final + ":" + port,
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.stopButton:
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			speedBar.setEnabled(true);
			started = false;
			mSensorManager.unregisterListener(this);
			break;
		default:
			break;
		}
	}
}
