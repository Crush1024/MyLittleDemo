package com.wendu.rongclouddemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wendu.rongclouddemo.R;
import com.wendu.rongclouddemo.bean.Friend;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

public class MainActivity extends AppCompatActivity implements RongIM.UserInfoProvider, View.OnClickListener {
    protected static final String TAG = "MainActivity";

    private static final String token1 = "r12EBzcZwVroVX/QFPZsE1C3dnRcxV6Igo5W8ic37M2G5ZJ0S7c4qt6dMbNOKom6JJtG4O/tUjs=";
    // username = 联通 userid = 10010 头像 =
    // http://img5.imgtn.bdimg.com/it/u=3461135921,2635786312&fm=21&gp=0.jpg

    private static final String token2 = "LBkUaCPQgKeO0ogIqHFRkhSTra2Qpi8FpdTExVBTeKeZA9KNp74Vj2de6MWFEd15oo3HHVvLm8Tq+7uaMY1U4g==";
    // username = 移动 userid = 10086 头像 =
    // http://img02.tooopen.com/Download/2010/5/22/20100522103223994012.jpg

    private Button mUser1, mUser2, mLoadFragment1, mLoadFragment2,mChat,mCustomerService,mListener,mSendMessage,mPushListnter,mHistoryMessage;

    private String mUserId;

    private List<Friend> userIdList;

    protected boolean temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        userIdList = new ArrayList<Friend>();
        userIdList.add(new Friend("001","工藤新一","http://192.168.203.90:8080/aaa/01.jpg"));
        userIdList.add(new Friend("002","毛利兰","http://192.168.203.90:8080/aaa/02.jpg"));
        //此处把客服头像 和 昵称 设置了
        userIdList.add(new Friend("KEFU1426079728044", "客服服务", "http://www.jf258.com/uploads/2013-07-11/135409968.jpg"));
        RongIM.setUserInfoProvider(this, true);
    }

    private void init() {
        mUser1 = (Button) findViewById(R.id.connect_10010);
        mUser2 = (Button) findViewById(R.id.connect_10086);
        mLoadFragment1 = (Button) findViewById(R.id.load1);
        mLoadFragment2 = (Button) findViewById(R.id.load2);
        mChat = (Button) findViewById(R.id.chat);
        mCustomerService = (Button) findViewById(R.id.customerservice);
        mListener = (Button) findViewById(R.id.listener);
        mSendMessage = (Button) findViewById(R.id.sendmessage);
        mPushListnter = (Button) findViewById(R.id.pushlistnter);
        mHistoryMessage = (Button) findViewById(R.id.getHistoryMessages);
        mUser1.setOnClickListener(this);
        mUser2.setOnClickListener(this);
        mLoadFragment1.setOnClickListener(this);
        mLoadFragment2.setOnClickListener(this);
        mChat.setOnClickListener(this);
        mCustomerService.setOnClickListener(this);
        mListener.setOnClickListener(this);
        mSendMessage.setOnClickListener(this);
        mPushListnter.setOnClickListener(this);
        mHistoryMessage.setOnClickListener(this);
    }

    @Override
    public UserInfo getUserInfo(String s) {
        for (Friend i : userIdList) {
            if (i.getUserId().equals(s)) {
                Log.e(TAG, i.getPortraitUri());
                return new UserInfo(i.getUserId(),i.getUserName(), Uri.parse(i.getPortraitUri()));
            }
        }
        return null;
    }
    @SuppressWarnings("static-access")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.connect_10010) {
            Log.e(TAG, "onClick: 用户1");
            connectRongServer(token1);
        } else if (v.getId() == R.id.connect_10086) {
            Log.e(TAG, "onClick: 用户2");
            connectRongServer(token2);
        } else if (v.getId() == R.id.load1) {
//            startActivity(new Intent(MainActivity.this,
//                    LoadConversationListFragment1.class));
        } else if(v.getId() == R.id.load2){
            startActivity(new Intent(MainActivity.this,
                    LoadConversationListFragment2.class));
        }else if(v.getId() == R.id.chat){
            if(mUserId!=null&&RongIM.getInstance()!=null)
                //此处聊天是写死的 实际开发中 大家应该写成动态的
                RongIM.getInstance().startPrivateChat(MainActivity.this,mUserId.equals("001") ? "002" : "001" , mUserId.equals("001") ? "工藤新一" : "毛利兰");
        }else if(v.getId() == R.id.customerservice){
            //客服 id 是您在融云开发者平台 客服 功能模块的获取
            CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
            CSCustomServiceInfo csInfo = csBuilder.nickName("融云").build();
            if(RongIM.getInstance() != null)
                RongIM.getInstance().startCustomerServiceChat(MainActivity.this,"KEFU149673400922253","客服",csInfo);
        }else if(v.getId() == R.id.listener){
            if (temp) {
                Log.e(TAG, "回调已走");
                RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {

                    @Override
                    public void onChanged(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
                        if (ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT == status) {
                            Log.e(TAG, "被顶号");
                        }
                        Log.e(TAG, "网络状态已经变化");
                    }
                });
            }
            RongIM.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {

                @Override
                public boolean onReceived(Message message, int arg1) {
                    Log.e(TAG, "接收消息的监听");
                    if (message.getContent() instanceof ImageMessage) {
                        ImageMessage imageMessage = (ImageMessage) message.getContent();
                        Log.e(TAG, "缩略路径为："+imageMessage.getThumUri());
                    }
                    return false;
                }
            });
        }else if(v.getId() == R.id.sendmessage){
            TextMessage tm = new TextMessage("测试发消息");

//            RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, mUserId.equals("10010") ? "10086" : "10010", tm, null, new RongIMClient.SendMessageCallback() {
//
//                @Override
//                public void onSuccess(Integer arg0) {
//                    Log.e(TAG, "测试发送消息回调输出成功");
//                }
//
//                @Override
//                public void onError(Integer arg0, RongIMClient.ErrorCode arg1) {
//                    Log.e(TAG, "测试发送消息回调输出失败----错误码:"+arg1.getValue());
//                }
//            });

        }else if(v.getId() == R.id.pushlistnter){
            // push 监听可以不需要实现 也不影响代码 这里只为测试  需要在IPC 和 主进程 挂掉以后去测试  也就算 程序退出以后 只有进程在后台运行的时候来消息 false 为融云处理 true 为自己处理
//            RongIM.setOnReceivePushMessageListener(new OnReceivePushMessageListener() {
//
//                @Override
//                public boolean onReceivePushMessage(PushNotificationMessage arg0) {
//                    Log.e(TAG, "push内容:"+arg0.getPushContent()+"push发送人:"+arg0.getSenderName());
//                    return false;
//                }
//            });
        }else if(v.getId() == R.id.getHistoryMessages){
            RongIM.getInstance().getRongIMClient().getHistoryMessages(Conversation.ConversationType.PRIVATE, mUserId.equals("10010") ? "10086" : "10010", -1, Integer.MAX_VALUE, new RongIMClient.ResultCallback<List<Message>>() {

                @Override
                public void onSuccess(List<Message> arg0) {
                    Toast.makeText(MainActivity.this, "历史消息数量为"+arg0.size(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "历史消息的条数："+arg0.size()+"");
                }

                @Override
                public void onError(RongIMClient.ErrorCode arg0) {
                    Log.e(TAG, "错误码:"+arg0.getValue()+"");
                }
            });
        }
    }
    /**
     * 连接融云服务器
     *
     * @param token
     */
    private void connectRongServer(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            @Override
            public void onSuccess(String userId) {
                mUserId = userId;
                if (userId.equals("001")) {
                    mUser1.setText("用户1连接服务器成功");
                    mUser2.setClickable(false);
                    mUser2.setTextColor(Color.GRAY);
                    Toast.makeText(MainActivity.this, "connet server success",
                            Toast.LENGTH_SHORT).show();
                    ;
                } else {
                    mUser2.setText("用户2连接服务器成功");
                    mUser1.setClickable(false);
                    mUser1.setTextColor(Color.GRAY);
                    Toast.makeText(MainActivity.this, "connet server success",
                            Toast.LENGTH_SHORT).show();
                    ;
                }
                Log.e(TAG, "connect success userid is :" + userId);
                temp = true;
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG,
                        "connect failure errorCode is :" + errorCode.getValue());
            }

            @Override
            public void onTokenIncorrect() {
                Log.e(TAG, "token is error , please check token and appkey ");
            }
        });
    }
}
