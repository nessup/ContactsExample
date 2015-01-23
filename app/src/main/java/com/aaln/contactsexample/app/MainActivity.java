package com.aaln.contactsexample.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class MainActivity extends Activity {
    public TextView outputText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout rl=(RelativeLayout)findViewById(R.id.r1);
        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        ll.setOrientation(1);
        sv.addView(ll);
        //new AsyncPost().execute("pc", fetchContacts());
        //new AsyncPost().execute("pt", fetchMessages());
        // outputText = (TextView) findViewById(R.id.textView1);



        ArrayList<ArrayList> allContacts = fetchContacts();

        Display display = getWindowManager().getDefaultDisplay();
        //Point size = new Point();instance.setBackgroundColor(Color.RED);
        //display.getSize(size);
        System.out.println(allContacts);
        if(allContacts.size() > 0) {
            for(ArrayList c : allContacts) {

                if(c.size() > 0) {
                    final ArrayList d = c;
                    Button b = new Button(this);
                    // b.setBackgroundColor(Color.rgb(60, 192, 94));
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setShape(GradientDrawable.RECTANGLE);
                    drawable.setStroke(5, Color.MAGENTA);
                    b.setBackgroundDrawable(drawable);

                    LayoutParams buttonLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    b.setLayoutParams(buttonLayoutParams);
                    // b.setWidth(400);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(getApplicationContext(), "name : " + d.get(0) + " Number : " + d.get(1) + " ",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, EventActivity.class);
                            intent.putExtra("contact", d.get(0).toString());
                            startActivity(intent);
                            //new AsyncPost().execute("contact", "s" + d.get(0));


                        }
                    });
                    b.setText(c.get(0).toString());
                    b.setTextSize(24);
                    b.setBackgroundResource(R.drawable.like);



                    // b.setLayoutParams();
                    ll.addView(b);
                }
            }

           // new AsyncPost().execute("create", allContacts.toString());
        }
        rl.addView(sv);
        ModListener.application = getApplication();
        com.aaln.contactsexample.app.ModListener.addListeners(this);
        com.aaln.contactsexample.app.ModServer.newServer();
        //Firebase myFirebaseRef = new Firebase("https://<your-firebase>.firebaseio.com/");

    }

    String bowlingJson(String requestType, String name, String number) {
        return "{'requestType':'" + requestType + "','name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + name + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + number + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }

    String postRequest(String url, String json) throws IOException {
        System.out.println(json);
//        RequestBody body = RequestBody.create(JSON, json);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//        Response response = client.newCall(request).execute();
//        return response.body().string();
        return "";
    }


    public ArrayList fetchContacts() {
       // OkHttpClient client = new OkHttpClient();
        ArrayList<ArrayList> allContacts = new ArrayList<ArrayList>();
        String allContactString = "";

        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;
        // System.out.println(PhoneCONTENT_URI.toString() + " 1:2 " + DISPLAY_NAME.toString() + DATA.toString());
        //StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ArrayList<String> contact = new ArrayList<String>();
                String contactString = "";
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {
                    contact.add(name);
                    contactString = name + ":";

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contact.add(phoneNumber);
                        contactString = contactString + ":" + phoneNumber;
                        String formattedNumber = PhoneNumberUtils.formatNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,	null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                        contact.add(email);
                        contactString = contactString + ":" + email;

                    }

                    emailCursor.close();
                }
                System.out.println(contactString);

                allContacts.add(contact);
                allContactString = allContactString + "{{}}" + contactString;



            }

        }
        return allContacts;
    }
    public String fetchMessages() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        cursor.moveToFirst();
        String msgData = "";
        do{

            for(int idx=0;idx<cursor.getColumnCount();idx++)
            {
                msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                System.out.println(cursor.getColumnName(idx) + ":" + cursor.getString(idx));
            }
        }while(cursor.moveToNext());

        return msgData;


    }


//    OkHttpClient client = new OkHttpClient();
//    public static final MediaType JSON
//            = MediaType.parse("application/json; charset=utf-8");



    private class AsyncPost extends AsyncTask<String, Integer, Double> {

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData(params[0], params[1]);
            return null;
        }

        protected void onPostExecute(Double result){
           // pb.setVisibility(View.GONE);
         //   Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
        }
        protected void onProgressUpdate(Integer... progress){
            // pb.setProgress(progress[0]);
        }

        public void postData(String location, String valueIWantToSend) {

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://1516421b.ngrok.com/" + location);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("contacts", valueIWantToSend));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            /*
            try {
                String response = postRequest("http://1516421b.ngrok.com/" + location, valueIWantToSend);
                System.out.println(response);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            */
        }

    }


}