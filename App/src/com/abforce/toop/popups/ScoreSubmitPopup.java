package com.abforce.toop.popups;

import com.abforce.toop.R;
import com.abforce.toop.R.id;
import com.abforce.toop.R.layout;
import com.abforce.toop.R.style;
import com.abforce.toop.managers.NetworkHelper;
import com.abforce.toop.managers.NetworkHelper.OnTaskFinishedListener;
import com.abforce.toop.models.HighScorePack;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreSubmitPopup extends Dialog implements View.OnClickListener {

	int mScore;
	
	public ScoreSubmitPopup(Context context) {
		super(context, R.style.DialogTheme);
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);

        setContentView(R.layout.submit_dialog_layout);
        
		TextView tv = (TextView) findViewById(R.id.score);
		tv.setText(String.valueOf(mScore));
		
		findViewById(R.id.submit).setOnClickListener(this);
    }
	
	public void show(int score) {
		mScore = score;
		show();
	}

	@Override
	public void onClick(final View view) {
		EditText name = (EditText) findViewById(R.id.name);
		final TextView tvError = (TextView) findViewById(R.id.error);
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.wait);		
		
		String text = name.getText().toString();
		if(text.length() > 20){
			tvError.setVisibility(View.VISIBLE);
			tvError.setText("حداکثر طول 20 حرف است");
			return;
		}
		
		if("".equals(text.trim())){
			tvError.setVisibility(View.VISIBLE);
			tvError.setText("نام وارد شده معتبر نیست");
			return;
		}
		
		tvError.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		
		NetworkHelper.asyncSetHighScorePack(new HighScorePack(text, mScore, 0), new OnTaskFinishedListener<Integer>() {
			
			@Override
			public void onFinished(Integer result) {
				switch (result) {
				case NetworkHelper.UNKNOWN_ERROR:
					view.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					tvError.setVisibility(View.VISIBLE);
					tvError.setText("عملیات دچار خطا شد!");
					break;

				case NetworkHelper.INVALID_HIGH_SCORE:
					view.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					tvError.setVisibility(View.VISIBLE);
					tvError.setText("امتیازی بالاتر از شما قبلاً ثبت شده است");
					break;
					
				case NetworkHelper.OKAY:
					dismiss();
					Toast.makeText(getContext(), "ثبت شد!", Toast.LENGTH_SHORT).show();
					break;
				}
				
			}
		});
	}
	
}
