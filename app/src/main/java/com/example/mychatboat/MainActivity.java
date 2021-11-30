package com.example.mychatboat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView chatsRV;
    private EditText userMsgEdit;
    private FloatingActionButton sendMsgFAB;
    private final String BOT_KEY="bot";
    private final String USER_KEY="user";
    private ArrayList<ChatsModel> chatsModelArrayList;
    private ChatRVAdapter chatRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatsRV=findViewById(R.id.idrbChat);
        userMsgEdit= findViewById(R.id.idEditMessage);
        sendMsgFAB=findViewById(R.id.idFabSend);
        chatsModelArrayList= new ArrayList<>();
        chatRVAdapter= new ChatRVAdapter(chatsModelArrayList,this);
        LinearLayoutManager manager= new LinearLayoutManager(this);
        chatsRV.setLayoutManager(manager);
        chatsRV.setAdapter(chatRVAdapter);

        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userMsgEdit.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(userMsgEdit.getText().toString());
                userMsgEdit.setText("");
            }
        });

    }

    private void getResponse(String message){
        chatsModelArrayList.add(new ChatsModel(message, USER_KEY));
        chatRVAdapter.notifyDataSetChanged();
        String url="http://api.brainshop.ai/get?bid=159864&key=zr64NKJAMvfRXXtZ&uid=[uid]&msg="+message;
        String BASE_URL="http://api.brainshop.ai/";
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetofitApi retofitApi= retrofit.create(RetofitApi.class)
;
        Call<MsgModel> call= retofitApi.getMessage(url);
       call.enqueue(new Callback<MsgModel>() {
           @Override
           public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
               if(response.isSuccessful()){
                   MsgModel model= response.body();
                   chatsModelArrayList.add(new ChatsModel(model.getCnt(), BOT_KEY));
                   chatRVAdapter.notifyDataSetChanged();
               }
           }

           @Override
           public void onFailure(Call<MsgModel> call, Throwable t) {

               Log.e("gert_error",t.getMessage());

               chatsModelArrayList.add(new ChatsModel("Please revert your question",BOT_KEY));
               chatRVAdapter.notifyDataSetChanged();
           }
       });
    }


}