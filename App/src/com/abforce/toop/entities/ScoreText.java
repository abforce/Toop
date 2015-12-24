package com.abforce.toop.entities;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;

import com.abforce.toop.managers.RM;

public class ScoreText {

	Text mText;
	public int mScore = 0;
	int mToBeAdded = 0;
	boolean isUpdaterRegistered = false;
	
	public ScoreText(){
		mText = new Text(-100 * RM.SX, -100 * RM.SY, RM.fnScore, "00000", RM.VBO){
			boolean init = false;
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if(!init){
					setText("0");
					updatePosition();
					init = true;
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		mText.setAlpha(0.8f);
		mText.setScaleCenter(0, 0);
		mText.setScale(RM.SX, RM.SY);
	}
	
	public void attachToScene(Scene scene){
		scene.attachChild(mText);
	}
	
	public void addScore(int score){
		mToBeAdded += score;
		
		if(!isUpdaterRegistered){
			mText.registerUpdateHandler(new IUpdateHandler(){

				@Override
				public void onUpdate(float pSecondsElapsed) {
					if(mToBeAdded == 0){
						mText.unregisterUpdateHandler(this);
					}
					
					if(mToBeAdded > 0){
						mToBeAdded -= 1;
						mScore += 1;
						updateScore();
					}
					
					if(mToBeAdded < 0){
						mToBeAdded += 1;
						mScore -= 1;
						updateScore();
					}
				}

				@Override
				public void reset() {}
				
			});
		}
	}
	
	private void updateScore(){
		mText.setText(String.valueOf(mScore));
		updatePosition();
	}
	
	private void updatePosition(){
		float x = RM.CW / 2 - RM.SX * mText.getWidth() / 2;
		float y = RM.CH / 2 - RM.SY * mText.getHeight() / 2;
		mText.setX(x);
		mText.setY(y);
	}
}
