package com.abforce.toop.popups;

import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.abforce.toop.entities.SSprite;
import com.abforce.toop.managers.RM;

public class GetReady implements IOnSceneTouchListener {

	Scene mChild;
	Scene mParent;
	
	public GetReady(OnDisposeListener listener){
		mListener = listener;
	}
	
	public void attachToScene(Scene scene){
		float x = RM.CW / 2 - RM.SX * RM.txGetReady.getWidth() / 2;
		float y = RM.CH / 2 - RM.SY * RM.txGetReady.getHeight() / 2;
		
		SSprite sprite = new SSprite(x, y, RM.txGetReady);
		mChild = new Scene();
		mChild.setBackgroundEnabled(false);
		mChild.attachChild(sprite);
		
		scene.setChildScene(mChild, false, true, true);
		mChild.setOnSceneTouchListener(this);
		mParent = scene;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown()){
			
			Sprite ent = (Sprite) mChild.getChildByIndex(0);
			
			float from = ent.getY();
			float to = -ent.getHeight(); 
			
			MoveYModifier modifier = new MoveYModifier(0.2f, from, to){
				protected void onModifierFinished(org.andengine.entity.IEntity pItem) {
					mParent.clearChildScene();
					mListener.onDisposed();
				};
			};
			ent.registerEntityModifier(modifier);
			return true;
			
		}
		return false;
	}

	OnDisposeListener mListener;
	
	public interface OnDisposeListener{
		public void onDisposed();
	}
	
}
