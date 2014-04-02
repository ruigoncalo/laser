/*
 * Class that represents each coordinate point (x,y,z)
 * Method toJSON() translates the point into a JSON object
 * 
 */

package com.rmgoncalo.laser;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Point {
	
	private final static String tag = "Laser-Point";
	
	// Strings used on the JSON object
	private final static String XAXIS = "x";
	private final static String YAXIS = "y";
	private final static String ZAXIS = "z";
	
	private float x;
	private float y;
	private float z;
	
	// Constructor
	public Point(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// Getters and Setters
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	// translating point object into JSON object
	public JSONObject toJSON(){
		JSONObject jObj = new JSONObject();
		
		try {
			jObj.put(XAXIS, String.valueOf(x));
			jObj.put(YAXIS, String.valueOf(y));
			jObj.put(ZAXIS, String.valueOf(z));
		} catch(JSONException e){
			e.printStackTrace();
			Log.d(tag, "JSONException: " + e);
		}
		
		return jObj;
	}

}
