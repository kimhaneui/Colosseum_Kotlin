package kr.co.tjoeun.colosseum_kotlin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import kr.co.tjoeun.colosseum_kotlin.databinding.ActivityProfileBinding;
import kr.co.tjoeun.colosseum_kotlin.utils.ContextUtil;

public class profileActivity extends BaseActivity {

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("로그아웃");
                alert.setMessage("정말 로그아웃 하시겠습니까?");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContextUtil.setLoginUserToken(mContext, "");

                        Intent myIntent = new Intent(mContext, LoginActivity.class);
                        startActivity(myIntent);

                        finish();
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "로그아웃을 취소했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();




            }
        });

    }

    @Override
    public void setValues() {

    }
}
