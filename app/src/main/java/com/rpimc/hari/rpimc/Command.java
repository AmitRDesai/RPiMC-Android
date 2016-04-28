package com.rpimc.hari.rpimc;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hari on 10/16/2015.
 */
public class Command implements Serializable{
	private static final long serialVersionUID = 1L;
	public int forward = 0;
	public int backward = 0;
	public int left = 0;
	public int right = 0;
	public int stop = 0;
	public ArrayList<Line> lines = new ArrayList<Line>();
	public String msg = "";
	public String data = "";

	public Command(int forward, int backward, int left, int right, int stop) {
		this.forward = forward;
		this.backward = backward;
		this.left = left;
		this.right = right;
		this.stop = stop;
	}

	public Command(String msg, ArrayList<Line> lines) {
		this.msg = msg;
		this.lines = lines;
	}

	public Command(String msg, String data) {
		this.msg = msg;
		this.data = data;
	}
}
