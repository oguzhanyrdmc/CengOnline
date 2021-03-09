package com.deuceng.cengonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    private Button buttonMessageNonReadBox;
    private Button buttonMessageReadBox;
    private Button buttonMessageSendBox;
    private Button buttonMessageNewMessage;
    private ListView listViewNonReadMessages;


    private String userId;
    private boolean nonReadMessageFlag;
    private boolean sendMessageFlag;
    private ArrayList<Message> messageArrayList;
    private Stack<String> nonReadIdStack;
    private Stack<String> readIdStack;
    private Stack<String> sendIdStack;

    private HashMap<String, String> usersNameHashMap;
    private HashMap<String, String> receiverNameHashMap;

    private final DatabaseReference databaseMessage;

    public MessageActivity() {

        this.databaseMessage = FirebaseDatabase.getInstance().getReference("Message");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        buttonMessageNonReadBox = findViewById(R.id.buttonMessageNonReadBox);
        buttonMessageReadBox = findViewById(R.id.buttonMessageReadBox);
        buttonMessageSendBox = findViewById(R.id.buttonMessageSendBox);
        buttonMessageNewMessage = findViewById(R.id.buttonMessageNewMessage);
        listViewNonReadMessages = findViewById(R.id.listViewNonReadMessages);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        messageArrayList = new ArrayList<>();
        nonReadIdStack= new Stack<>();
        readIdStack= new Stack<>();
        sendIdStack= new Stack<>();
        usersNameHashMap = new HashMap<String, String>();
        receiverNameHashMap= new HashMap<String, String>();
        nonReadMessageFlag = false;
        sendMessageFlag = false; // send box'da yer alan mesaj cevaplanamaz.

        fetchUsersName();

        buttonMessageNonReadBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonReadMessageFlag = true;
                sendMessageFlag = false;
                fetchMessageId(databaseMessage.child("ReceiveMessages").child(userId).child("nonRead"),nonReadIdStack);
            }
        });

        buttonMessageReadBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonReadMessageFlag = false;
                sendMessageFlag = false;
                fetchMessageId(databaseMessage.child("ReceiveMessages").child(userId).child("Read"),readIdStack);
            }
        });

        buttonMessageSendBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonReadMessageFlag = false;
                sendMessageFlag = true;
                fetchMessageId(databaseMessage.child("SendMessages").child(userId),sendIdStack);
            }
        });

        buttonMessageNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageDialog();
            }
        });

        listViewNonReadMessages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Message message = messageArrayList.get(position);

                if(nonReadMessageFlag){
                    changeToRead(message);
                    fetchMessageId(databaseMessage.child("ReceiveMessages").child(userId).child("nonRead"),nonReadIdStack);
                }
                if(!sendMessageFlag){
                    sendMessageDialog(message);
                }


                return false;
            }
        });
    }

    private void changeToRead(Message message){
        databaseMessage.child("ReceiveMessages").child(userId).child("nonRead").child(message.getID()).removeValue();
        databaseMessage.child("ReceiveMessages").child(userId).child("Read").child(message.getID()).setValue(true);
    }

    private void sendMessageDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_message_send, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextMessageSendName = dialogView.findViewById(R.id.editTextMessageSendName);
        final EditText editTextSendMessageText = dialogView.findViewById(R.id.editTextSendMessageText);
        final Button buttonSendSendMessage = dialogView.findViewById(R.id.buttonSendSendMessage);

        dialogBuilder.setTitle("SEND MESSAGE");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonSendSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendName = editTextMessageSendName.getText().toString();
                String text = editTextSendMessageText.getText().toString();

                if(receiverNameHashMap.containsKey(sendName)){
                    if(!TextUtils.isEmpty(text) ){
                        String messageID = databaseMessage.child("Messages").push().getKey();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");
                        String currentDate = sdf.format(new Date());
                        Message message = new Message(messageID,receiverNameHashMap.get(sendName),userId,text,currentDate);
                        sendMessage(message);
                        Toast.makeText(MessageActivity.this, "Message is sent, Successfully ", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(MessageActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MessageActivity.this, "Please enter a correct user name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendMessageDialog(final Message receiveMessage){ //answerMessageDialog

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_message_answer, null);
        dialogBuilder.setView(dialogView);

        final TextView textViewMessageTo = dialogView.findViewById(R.id.textViewMessageTo);
        final EditText editTextSendMessage = dialogView.findViewById(R.id.editTextSendMessage);
        final TextView textViewInMessage = dialogView.findViewById(R.id.textViewInMessage);
        final Button buttonSendMessage = dialogView.findViewById(R.id.buttonSendMessage);
        textViewInMessage.setText(receiveMessage.getText());
        textViewMessageTo.setText(usersNameHashMap.get(receiveMessage.getFromID()));

        dialogBuilder.setTitle("ANSWER");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sendText = editTextSendMessage.getText().toString();

                if(!TextUtils.isEmpty(sendText)){
                    String messageID = databaseMessage.child("Messages").push().getKey();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");
                    String currentDate = sdf.format(new Date());
                    Message sendMessage = new Message(messageID,receiveMessage.getFromID(),receiveMessage.getToID(),sendText,currentDate);
                    sendMessage(sendMessage);
                    Toast.makeText(MessageActivity.this, "Message is sent, Successfully ", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(MessageActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendMessage(Message message){

        databaseMessage.child("ReceiveMessages").child(message.getToID()).child("nonRead").child(message.getID()).setValue(true);
        databaseMessage.child("SendMessages").child(message.getFromID()).child(message.getID()).setValue(true);
        databaseMessage.child("Messages").child(message.getID()).setValue(message);
    }

    private void fetchUsersName(){

        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersNameHashMap.clear();
                receiverNameHashMap.clear();

                for (DataSnapshot userTypes: dataSnapshot.getChildren()){
                    for(DataSnapshot userIDs : userTypes.getChildren()){
                        String name = userIDs.child("name").getValue().toString();
                        String surname = userIDs.child("surname").getValue().toString();
                        String id = userIDs.child("id").getValue().toString();

                        usersNameHashMap.put(id,name+" "+surname);
                        receiverNameHashMap.put(name+ " "+surname,id);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchMessageId(DatabaseReference databaseReference, final Stack<String> stackIdTemp){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                stackIdTemp.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    stackIdTemp.push(snapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fetchMessage(stackIdTemp);
    }

    public void fetchMessage(final Stack<String> idStack){
        databaseMessage.child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageArrayList.clear();

                for(int i=0; i < idStack.size(); i++){
                    Message message = dataSnapshot.child(idStack.pop()).getValue(Message.class);
                    messageArrayList.add(message);
                }

                MessageList messageAdapter = new MessageList(MessageActivity.this,messageArrayList,usersNameHashMap);
                listViewNonReadMessages.setAdapter(messageAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
