package com.example.chatapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chatapp.HelperClasses.MessageAdapter;
import com.example.chatapp.HelperClasses.CardAdapter;
import com.example.chatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class chatroom extends AppCompatActivity implements TextWatcher {

    private String name,mobileno, itemname, datetimetxt;
    private WebSocket webSocket;
    private RelativeLayout layout;
    private String SERVER_PATH = "ws://192.168.1.3:3000";  //"ws://SERVER-IP-HERE:PORT-NUMBER-HERE";
    private EditText messageEdit;
    private View sendBtn, pickImgBtn;
    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;
    public static boolean isSocketConnected = true;

    // this method gets called when the activity went to pause state and socket connection will be disconnected
    @Override
    protected void onPause() {
        super.onPause();
        webSocket.close(1000,"disconnects on pausing the screen");
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(this,"on resume is called", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initiateSocketConnection();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        layout = findViewById(R.id.relative);

        name = getIntent().getStringExtra("name");
        mobileno = getIntent().getStringExtra("mobile");
        itemname = getIntent().getStringExtra("item");
        datetimetxt = getIntent().getStringExtra("date");
        initiateSocketConnection();
        initializeView();
        createpopupwindow();
    }

    // method to create popup window for the chatting activity
    private void createpopupwindow() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*.8), (int) (height*.5));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        params.dimAmount = 0.85f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(params);
    }

//    @Override
//    public void onBackPressed() {
//
//        //Toast.makeText(this, name+" is offline",Toast.LENGTH_SHORT).show();
//        AlertDialog.Builder alert = new AlertDialog.Builder(chatroom.this);
//        alert.setTitle("Info");
//        alert.setMessage(name +", Are you sure you want to go offline");
//        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(chatroom.this, name+" is offline",Toast.LENGTH_SHORT).show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent i = new Intent(chatroom.this,orderdetails.class);
//                        startActivity(i);
//                        finish();
//                    }
//                }, 2000);
//            }
//        });
//        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//        AlertDialog alertDialog = alert.create();
//        alertDialog.setCanceledOnTouchOutside(true);
//        alertDialog.show();
//    }

    // method used to start the socket connection
    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
    }

    //This method is called to notify you that, within s, the count characters beginning at start are about to be replaced by new text with length after.
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    //This method is called to notify you that, within s, the count characters beginning at start have just replaced old text that had length before
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
    //This method is called to notify you that, somewhere within s, the text has been changed.
    @Override
    public void afterTextChanged(Editable s) {
        String str = s.toString().trim();
        if(str.isEmpty()){
            resetMessageEdit();
        }
        else{
            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
        }
    }

    // If user click outside the views(eg: inside the edittext), keyboard will be hided based on the touch event
    // method to handle user touch events
    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if(getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    private class SocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            runOnUiThread(() -> {
                Toast.makeText(chatroom.this, "You are online 😃", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {

                try {
                    JSONObject jsonObject = new JSONObject(text);
                    jsonObject.put("isSent", false);

                    messageAdapter.addItem(jsonObject);
                    // the below line will make auto scroll of recyclerview and -1 is used for arrays start from 0
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosed(webSocket, code, reason);
            runOnUiThread(() -> {
                isSocketConnected = false;
                Toast.makeText(chatroom.this, name+" disconnects the socket connection 😞", Toast.LENGTH_SHORT).show();
            });
//            isSocketConnected = true;
//            webSocket.close(code,reason);
        }
//        @Override
//        public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
//            super.onClosing(webSocket, code, reason);
//            runOnUiThread(() -> {
//                Toast.makeText(chatroom.this, name+" going to disconnect the socket connection", Toast.LENGTH_SHORT).show();
//            });
//
//        }

//        @Override
//        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
//            super.onFailure(webSocket, t, response);
//            runOnUiThread(() -> {
//                Toast.makeText(chatroom.this, t.getMessage().toString()+" "+response.message(), Toast.LENGTH_SHORT).show();
//            });
//        }
    }

    // in this method, we're removing the textchanged listener coz we don't want these functions to call while emptying it.
    // and finally we will add tectchanged listener after completing other functionalities
    private void resetMessageEdit() {
        messageEdit.removeTextChangedListener(this);
        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);
        messageEdit.addTextChangedListener(this);
    }

    // In this method, we're storing the reference of all the items we have in out chatroom activity
    private void initializeView() {

        messageEdit = findViewById(R.id.edit1);
        sendBtn = findViewById(R.id.txtview);
        pickImgBtn = findViewById(R.id.imgview);
        recyclerView = findViewById(R.id.recyclerview1);
        messageAdapter = new MessageAdapter(getLayoutInflater());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject2.put("name", name);
            jsonObject2.put("message", name+"\n"+mobileno+"\n"+name+" ordered "+ Arrays.asList(CardAdapter.productnames.toString().replaceAll("(\\[|\\])", "")+" mobile"+"\n"+"Estimated delivery date is: "+ datetimetxt));
                    //replace("[", "").replace("]", ""))
            //jsonObject2.put("itemname", itemname);
            //jsonObject2.put("datetimetxt", datetimetxt);
            //jsonObject2.put("message", messageEdit.getText().toString());

            webSocket.send(jsonObject2.toString());

            jsonObject2.put("isSent", true);
            messageAdapter.addItem(jsonObject2);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

            resetMessageEdit();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        messageEdit.addTextChangedListener(this);
        // when clicking send btn, we will get the text from edittext and put that msg in json object and send it to server
        sendBtn.setOnClickListener(v -> {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", name);
                jsonObject.put("message", messageEdit.getText().toString());

                webSocket.send(jsonObject.toString()); // send JSON object to server

                jsonObject.put("isSent", true);
                messageAdapter.addItem(jsonObject);

                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        pickImgBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            //noinspection deprecation
            startActivityForResult(Intent.createChooser(intent, "Pick image"),
                    IMAGE_REQUEST_ID); // startactivityforresult is used to give all kind of options to which user select the image.

        });

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {

            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void sendImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", name);
            jsonObject.put("image", base64String);

            webSocket.send(jsonObject.toString());

            jsonObject.put("isSent", true);

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}