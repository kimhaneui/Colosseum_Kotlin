package kr.co.tjoeun.colosseum_kotlin;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.tjoeun.colosseum_kotlin.adapters.TopicReReplyAdapter;
import kr.co.tjoeun.colosseum_kotlin.databinding.ActivityViewReplyBinding;
import kr.co.tjoeun.colosseum_kotlin.datas.TopicReply;
import kr.co.tjoeun.colosseum_kotlin.utils.ServerUtil;

public class ViewReplyActivity extends BaseActivity {

    ActivityViewReplyBinding binding;

    int replyId = -1;
    TopicReply mReplyData;

    TopicReReplyAdapter reReplyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_reply);
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        binding.findTopicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(mContext,MainActivity.class);
                startActivity(myintent);
            }
        });
        binding.userListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(mContext,UserListActivity.class);
                startActivity(myintent);
            }
        });
        binding.replyPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = binding.replyEdt.getText().toString();

                if (input.length()<10){
                    Toast.makeText(mContext,"답글은 최소 10자 이상이여야 한다",Toast.LENGTH_SHORT).show();
                    return;
                }
                ServerUtil.postRequestReReply(mContext, replyId, input, new ServerUtil.JsonResponseHandler() {
                    @Override
                    public void onResponse(JSONObject json) {
                        Log.d("대댓글응답",json.toString());

                        getReplyDataServer();
                    }
                });
            }
        });

    }

    @Override
    public void setValues() {
//            대댓글 목록을 뿌릴때 필요한 진영 정보
//        tra = new TopicReplyAdapter(mContext,R.layout.topic_reply_list_item,mReplyData.getreplyList())
        replyId=getIntent().getIntExtra("replyId",-1);
        if(replyId !=-1){
//            서버에서 의견의 상세정보를 불러오자
            getReplyDataServer();
        }
    }
    void  getReplyDataServer(){
        ServerUtil.getRequestTopicReplyById(mContext, replyId, new ServerUtil.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {

                Log.d("의견상세", json.toString());

                try {
                    JSONObject data = json.getJSONObject("data");
                    JSONObject reply = data.getJSONObject("reply");

                    mReplyData = TopicReply.getTopicReplyFromJson(reply);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUiByReplyData();
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    void setUiByReplyData(){
        binding.nickNameTxt.setText(mReplyData.getWriter().getNickName());
        binding.contentTxt.setText(mReplyData.getContent());

        binding.sideTitleTxt.setText(mReplyData.getSelectedSide().getTitle());

        reReplyAdapter  = new TopicReReplyAdapter(mContext,R.layout.topic_re_reply_list_item,mReplyData.getReplyList());
        binding.replyListView.setAdapter(reReplyAdapter );
        binding.replyEdt.setText("");
    }
}

