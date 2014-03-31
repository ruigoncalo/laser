package com.rmgoncalo.laser;

import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener,
		OnClickListener {

	private Button btnStart;
	private Button btnStop;
	private TextView xAxis;
	private TextView yAxis;
	private TextView zAxis;

	private boolean started = false;
	private SensorManager sensorManager;
	private ArrayList<Point> sensorData;
	private Sensor sensor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnStart = (Button) findViewById(R.id.startButton);
		btnStop = (Button) findViewById(R.id.stopButton);

		xAxis = (TextView) findViewById(R.id.x_axis);
		yAxis = (TextView) findViewById(R.id.y_axis);
		zAxis = (TextView) findViewById(R.id.z_axis);

		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnStart.setEnabled(true);
		btnStop.setEnabled(false);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

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
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (started) {
			sensorManager.unregisterListener(this);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (started) {
			double x = event.values[0];
			double y = event.values[1];
			double z = event.values[2];

			Point p = new Point(x, y, z);
			sensorData.add(p);
			
			xAxis.setText(String.valueOf(x));
			yAxis.setText(String.valueOf(y));
			zAxis.setText(String.valueOf(z));
			
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.startButton:
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);
			sensorData = new ArrayList<Point>();
			started = true;
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_FASTEST);
			break;

		case R.id.stopButton:
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			started = false;
			sensorManager.unregisterListener(this);
			break;
		default:
			break;
		}
	}
}
