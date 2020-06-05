package kr.co.tjoeun.colosseum_kotlin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext = this;

    public TextView activityTitletxt;
    public ImageView notificationImg;
    public ImageView logoImg;

    public abstract void setupEvents();
    public abstract void setValues();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomActionBar();
    }

    @Override
    public void setTitle(CharSequence title) {

        super.setTitle(title);
        activityTitletxt.setVisibility(View.VISIBLE);
        logoImg.setVisibility(View.GONE);
        activityTitletxt.setText(title);

    }

    public void setCustomActionBar(){

        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.action_bar);
            getSupportActionBar().setBackgroundDrawable(null);

            Toolbar toolbar = (Toolbar) getSupportActionBar().getCustomView().getParent();
            toolbar.setContentInsetsAbsolute(0,0);

            View customActionView = getSupportActionBar().getCustomView();
            activityTitletxt=customActionView.findViewById(R.id.activityTitleTxt);
            notificationImg = customActionView.findViewById(R.id.notificationImg);
            logoImg = customActionView.findViewById(R.id.logoImg);

            notificationImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myintent = new Intent(mContext,NotificationActivity.class);
                    startActivity(myintent);
                }
            });
        }
    }

}
