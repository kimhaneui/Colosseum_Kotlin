package kr.co.tjoeun.colosseum_kotlin.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import kr.co.tjoeun.colosseum_kotlin.R;
import kr.co.tjoeun.colosseum_kotlin.ViewReplyActivity;
import kr.co.tjoeun.colosseum_kotlin.datas.TopicReply;
import kr.co.tjoeun.colosseum_kotlin.datas.TopicSide;
import kr.co.tjoeun.colosseum_kotlin.utils.ServerUtil;

public class TopicReplyAdapter extends ArrayAdapter<TopicReply> {

//    주제에서 선택 가능한 진영들의 id들이 담긴 배열
    TopicSide[] topicSideArr;

    Context mContext;
    List<TopicReply> mList;
    LayoutInflater inf;

    public TopicReplyAdapter(@NonNull Context context, int resource, @NonNull List<TopicReply> objects) {
        super(context, resource, objects);
        mContext = context;
        mList = objects;
        inf = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = inf.inflate(R.layout.topic_reply_list_item, null);
        }

        TextView contentTxt = row.findViewById(R.id.contentTxt);
        TextView writerNickNameTxt = row.findViewById(R.id.writerNickNameTxt);
        TextView sideTxt = row.findViewById(R.id.sideTxt);
        TextView createdAtTxt = row.findViewById(R.id.createdAtTxt);

        Button replycountBtn = row.findViewById(R.id.replycountBtn);
        Button likecountBtn = row.findViewById(R.id.likecountBtn);
        Button dislikecountBtn = row.findViewById(R.id.dislikecountBtn);

        final TopicReply data = mList.get(position);

        contentTxt.setText(data.getContent());
        writerNickNameTxt.setText(data.getWriter().getNickName());


//        int sideIndex = 0;
//        for (int i = 0; i < topicSideArr.length ; i++) {
//            if (topicSideArr[i].getId() == data.getSide_id()) {
//                sideIndex = i;
//            }
//        }
//
//        if (sideIndex == 0) {
//            sideTxt.setBackgroundResource(R.drawable.red_border_box);
//            sideTxt.setTextColor(Color.RED);
//
//        }
//        else {
//            sideTxt.setBackgroundResource(R.drawable.blue_border_box);
//            sideTxt.setTextColor(Color.BLUE);
//        }

        sideTxt.setText(data.getSelectedSide().getTitle());

//        언제 댓글을 남겼는지 표시. => 의견에 있는 기능 활용
        createdAtTxt.setText(data.getFormattedTimeAgo());


//        좋아요 싫어요 갯수
        likecountBtn.setText(String.format("좋아요%,d",data.getLikeCount()));
        dislikecountBtn.setText(String.format("싫어요%,d",data.getDislikeCount()));

        replycountBtn.setText(String.format("답글 %,d",data.getReplyCount()));

       if (data.isMylike()){
            likecountBtn.setBackgroundResource(R.drawable.red_border_box);
            likecountBtn.setTextColor(Color.RED);
        }
        else {
            likecountBtn.setBackgroundResource(R.drawable.grey_border_box);
            likecountBtn.setTextColor(mContext.getResources().getColor(R.color.grey));
        }

        if(data.isMyDislike()){
            dislikecountBtn.setBackgroundResource(R.drawable.blue_border_box);
            dislikecountBtn.setTextColor(Color.BLUE);
        }
        else{
            dislikecountBtn.setBackgroundResource(R.drawable.grey_border_box);
            dislikecountBtn.setTextColor(mContext.getResources().getColor(R.color.grey));
        }
        likecountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerUtil.postRequestReplyLike(mContext, data.getId(), true, new ServerUtil.JsonResponseHandler() {
                    @Override
                    public void onResponse(JSONObject json) {
                        Log.d("좋아요 누름",json.toString());

                        try {
                            JSONObject dataObj = json.getJSONObject("data");
                            JSONObject reply = dataObj.getJSONObject("reply");

                            data.setLikeCount(reply.getInt("like_count"));
                            data.setMylike(reply.getBoolean("my_like"));
                            data.setDislikeCount(reply.getInt("dislike_count"));
                            data.setMyDislike(reply.getBoolean("my_dislike"));

                            new Handler(Looper.getMainLooper()).post(new Runnable(){

                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        dislikecountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerUtil.postRequestReplyLike(mContext, data.getId(), false, new ServerUtil.JsonResponseHandler() {
                    @Override
                    public void onResponse(JSONObject json) {
                        Log.d("싫어요 누름",json.toString());

                        try {
                            JSONObject dataObj = json.getJSONObject("data");
                            JSONObject reply = dataObj.getJSONObject("reply");

                            data.setLikeCount(reply.getInt("like_count"));
                            data.setMylike(reply.getBoolean("my_like"));
                            data.setDislikeCount(reply.getInt("dislike_count"));
                            data.setMyDislike(reply.getBoolean("my_dislike"));

                            new Handler(Looper.getMainLooper()).post(new Runnable(){

                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        replycountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(mContext, ViewReplyActivity.class);
                myintent.putExtra("replyId",data.getId());
//                주제 목록을 넘겨줄 방법
                mContext.startActivity(myintent);
            }
        });
       return row;
    }
}
