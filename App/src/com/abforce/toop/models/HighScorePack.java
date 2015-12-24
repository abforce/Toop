package com.abforce.toop.models;

import java.util.regex.Pattern;

public class HighScorePack {
	public String name;
	public int score;
	public long millis;
	public boolean isValid = false;

	public HighScorePack(String name, int score, long millis) {
		this.name = name;
		this.score = score;
		this.millis = millis;
	}

	public void normalize() {
		if(name == null){
			isValid = false;
			return;
		}
		if (score <= 0 || millis < 0 || "".equals(name.trim())) {
			isValid = false;
			return;
		}
		name = name.trim().replaceAll(Pattern.quote("\n"), "");
		if(name.length() > 20){
			name = name.substring(0, 20);
		}
		isValid = true;
	}
}
