package com.abforce.toop.entities;

import java.util.ArrayList;
import java.util.List;

import com.abforce.toop.entities.Ball.OnBallLeaveListener;
import com.abforce.toop.managers.SoundManager;
import com.abforce.toop.popups.GameOverWindow;
import com.abforce.toop.scenehandlers.MainSceneHandler;

public class BallPool {
	
	public List<Ball> balls = new ArrayList<Ball>();
	
	public BallPool(){
		
	}
	
	public void generateBall(){
		generateBall(1, 3);
	}
	
	public void generateBall(float vx, float vy){
		final Ball ball = new Ball(vx, vy);
		ball.setOnBallLeaveListener(new OnBallLeaveListener() {

			@Override
			public void onLeft() {
				balls.remove(ball);
				onBallLeft();
			}
		});
		MainSceneHandler.STATE.scene.attachChild(ball);
		balls.add(ball);
	}
	
	public void slowDown(){
		for(Ball ball : balls){
			ball.slowDown();
		}
	}
	
	public void setVisible(boolean v){
		for(Ball ball : balls){
			ball.setVisible(v);
		}
	}
	
	private void onBallLeft(){
		if(balls.size() == 0){
			GameOverWindow gameOverWindow = new GameOverWindow();
			gameOverWindow.attachToScene(MainSceneHandler.STATE.scene);
			MainSceneHandler.STATE.gameStarted = false;
			SoundManager.onGameOver();
		}
	}
}
