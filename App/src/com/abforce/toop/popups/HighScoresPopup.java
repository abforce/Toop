package com.abforce.toop.popups;

import com.abforce.toop.R;
import com.abforce.toop.R.id;
import com.abforce.toop.R.layout;
import com.abforce.toop.R.style;
import com.abforce.toop.managers.PreferenceHelper;
import com.abforce.toop.models.HighScorePack;
import com.abforce.toop.utils.SolarCalendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class HighScoresPopup extends Dialog {

	public HighScoresPopup(Context context) {
		super(context, R.style.DialogTheme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        
        setContentView(R.layout.highscores_dialog_layout);
        
        TextView tvLocalScore = (TextView) findViewById(R.id.score_local);
        TextView tvLocalDate = (TextView) findViewById(R.id.date_local);
        TextView tvScoreGlobal = (TextView) findViewById(R.id.score_global);
        TextView tvName = (TextView) findViewById(R.id.name);
        TextView tvDateGlobal = (TextView) findViewById(R.id.date_global);
        
        HighScorePack packLocal = PreferenceHelper.getLocalHighScorePack();
        if(packLocal.millis > 0){
        	tvLocalScore.setText(String.valueOf(packLocal.score));
        	tvLocalDate.setText(SolarCalendar.getShamsiDate(packLocal.millis));
        } else {
        	tvLocalScore.setVisibility(View.GONE);
        	tvLocalDate.setText("امتیازی ثبت نشده است!");
        }
        
        HighScorePack packGlobal = PreferenceHelper.getGlobalHighScorePack();
        if(packGlobal.isValid){
        	tvScoreGlobal.setText(String.valueOf(packGlobal.score));
        	tvName.setText(packGlobal.name);
        	tvDateGlobal.setText(SolarCalendar.getShamsiDate(packGlobal.millis));
        } else {
        	tvScoreGlobal.setVisibility(View.GONE);
        	tvName.setVisibility(View.GONE);
        	tvDateGlobal.setText("اطلاعاتی موجود نیست!");
        }
	}
}
