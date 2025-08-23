package com.example.app.longpresspopwindow;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button btnSend;
    private Button btnGenerate;
    private MessageAdapter adapter;
    private List<Message> messageList;
    private Random random = new Random();

    // 随机消息内容
    private String[] randomMessages = {
        "555",
        "在忙什么呢？",
        "哈哈，这个太有趣了！",
        "周末有什么计划吗？",
        "64564",
        "今天心情特别好！",
        "你最近怎么样？",
        "1",
        "这个想法很棒！",
        "3",
        "今天学到了很多新东西",
        "工作还顺利吗？",
        "晚上一起吃饭吧",
        "这个项目进展如何？",
        "5",
        "明天见！",
        "谢谢你的帮助",
        "这个建议很有用",
        "8888888",
        "保持联系哦！",
        "今天真是充实的一天",
        "你有什么想法？",
        "87",
        "让我们继续努力",
        "加油！你可以的！",
        "这个问题很有趣",
        "我需要想想",
        "你说得对",
        "我明白了",
        "这确实是个好主意"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // 设置窗口边距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupRecyclerView();
        setupClickListeners();
        
        // 添加一些初始消息
        addInitialMessages();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        btnSend = findViewById(R.id.btnSend);
        btnGenerate = findViewById(R.id.btnGenerate);
        messageList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        // 设置消息操作监听器
        adapter.setOnMessageActionListener(new MessageAdapter.OnMessageActionListener() {
            @Override
            public void onDeleteMessage(int position) {
                adapter.removeMessage(position);
                Toast.makeText(MainActivity.this, "消息已删除", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReplyMessage(Message message) {
                editTextMessage.setText("回复: " + message.getContent());
                editTextMessage.requestFocus();
                Toast.makeText(MainActivity.this, "准备回复消息", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        btnSend.setOnClickListener(v -> sendMessage());
        
        btnGenerate.setOnClickListener(v -> generateRandomMessage());
        
        // 按回车键发送消息
        editTextMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(messageText)) {
            String currentTime = getCurrentTime();
            Message message = new Message(messageText, currentTime, true);
            adapter.addMessage(message);
            editTextMessage.setText("");
            
            // 滚动到底部
            recyclerView.smoothScrollToPosition(messageList.size());
            
            // 模拟对方回复
            simulateReply();
        }
    }

    private void generateRandomMessage() {
        String randomMessage = randomMessages[random.nextInt(randomMessages.length)];
        String currentTime = getCurrentTime();
        Message message = new Message(randomMessage, currentTime, false);
        adapter.addMessage(message);
        
        // 滚动到底部
        recyclerView.smoothScrollToPosition(messageList.size());
        
        Toast.makeText(this, "生成了随机消息", Toast.LENGTH_SHORT).show();
    }

    private void simulateReply() {
        // 延迟1-3秒后自动回复
        recyclerView.postDelayed(() -> {
            String replyMessage = randomMessages[random.nextInt(randomMessages.length)];
            String currentTime = getCurrentTime();
            Message reply = new Message(replyMessage, currentTime, false);
            adapter.addMessage(reply);
            
            // 滚动到底部
            recyclerView.smoothScrollToPosition(messageList.size());
        }, 1000 + random.nextInt(2000));
    }

    private void addInitialMessages() {
        // 添加一些初始的欢迎消息
        String[] welcomeMessages = {
            "6",
            "我是你的聊天伙伴",
            "77",
            "你也可以输入消息和我聊天哦！你也可以输入消息和我聊你也可以输入消息和我聊天哦！天哦你也可以输入消你也可以输入消息和我聊天哦！息和我聊天哦！！",
            "555",
            "在忙什么呢？",
            "哈哈，这个太有趣了你也可以输入消息和我聊天哦！你也可以输你也可以输入消息和我聊天哦！入消息你也可以输入消息和我聊天哦！和我聊天哦！！",
            "周末有什么计划吗？你也可以输入消息和我聊天哦！你你也可以输入消息和我聊天哦！也可以输入消息和我聊天哦！",
            "64564",
            "今天心情特别好！",
            "你最近怎么样？",
            "1",
            "这个想法很棒！",
            "3"
        };
        
        for (String msg : welcomeMessages) {
            String currentTime = getCurrentTime();
            Message message = new Message(msg, currentTime, false);

            Message reply = new Message(msg, currentTime, true);
            adapter.addMessage(message);
            adapter.addMessage(reply);
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}