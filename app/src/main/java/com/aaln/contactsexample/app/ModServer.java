package com.aaln.contactsexample.app;

import android.util.Log;

import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaron on 1/22/15.
 */
public class ModServer {


    static final List<WebSocket> sockets = new ArrayList<>();

//    public static void newReq() {
//        AsyncHttpPost post = new AsyncHttpPost("http://myservercom/postform.html");
//        MultipartFormDataBody body = new MultipartFormDataBody();
//
//        body.addStringPart("foo", "bar");
//        post.setBody(body);
//        AsyncHttpClient.getDefaultInstance().execute(post, new AsyncHttpClient.StringCallback() {
//            @Override
//            public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
//                if (e != null) {
//                    ex.printStackTrace();
//                    return;
//                }
//                System.out.println("Server says: " + result);
//            }
//        });
//    }
    public static void socketReq(final JSONObject message) {

        System.out.println("socketReq called");
        System.out.println(message);
        AsyncHttpClient.getDefaultInstance().websocket("ws://192.168.10.143:8080/", "message", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                webSocket.send(message.toString());
                webSocket.send("test1");
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    public void onStringAvailable(String s) {
                        System.out.println("I got a string: " + s);
                    }
                });
                webSocket.setDataCallback(new DataCallback() {
                    public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
                        System.out.println("I got some bytes!");
                        // note that this data has been read
                        byteBufferList.recycle();
                    }


                });
            }
        });
    }

    public static void newServer() {
        System.out.println("newServer called");
        AsyncHttpServer server = new AsyncHttpServer();

        server.websocket("/live", new AsyncHttpServer.WebSocketRequestCallback() {
            @Override
            public void onConnected(final WebSocket webSocket, AsyncHttpServerRequest request) {
                ModServer.sockets.add(webSocket);

                //Use this to clean up any references to your websocket
                webSocket.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        try {
                            if (ex != null)
                                Log.e("WebSocket", "Error");
                        } finally {
                            ModServer.sockets.remove(webSocket);
                        }
                    }
                });

                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        try {
                            JSONObject update = new JSONObject(s);

                            ModListener.updateView(update.getInt("id"), update.getString("key"), update.getString("value"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });

//..Sometime later, broadcast!

        // listen on port 5000
        server.listen(5000);
    }


    public static void sendMessage(String message) {
        for (WebSocket socket : ModServer.sockets) {
            System.out.println("sending");
            socket.send(message);
        }
    }

}
