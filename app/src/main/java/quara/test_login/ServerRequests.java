package quara.test_login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerRequests {

    ProgressDialog progressDialog;

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADDRESS = "http://monkeyking.web.engr.illinois.edu/";

    public ServerRequests(Context context)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallBack callBack)
    {
        progressDialog.show();
        new StoreUserDateAsyncTask(user, callBack).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallBack callBack)
    {
        progressDialog.show();
        new fetchUserDateAsyncTask(user, callBack).execute();
    }

    public void checkUserDataInBackground(User user, GetUserCallBack callBack)
    {
        progressDialog.show();
        new checkUserDateAsyncTask(user, callBack).execute();
    }

    public class StoreUserDateAsyncTask extends AsyncTask<Void, Void, Void>
    {
        User user;
        GetUserCallBack userCallback;

        public StoreUserDateAsyncTask(User user, GetUserCallBack userCallback)
        {
            this.user = user;
            this.userCallback = userCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            StringBuilder sb = new StringBuilder();
            for(String key : data.keySet()) {
                String value = null;
                try {
                    value = URLEncoder.encode(data.get(key), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(sb.length()>0)
                    sb.append("&");

                sb.append(key + "=" + value);
            }
            return sb.toString();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Map dataToSend = new HashMap();
            dataToSend.put("name", user.name);
            dataToSend.put("username", user.username);

            dataToSend.put("password", user.password);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "Register.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");

                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

                writer.write(encodedStr);

                writer.flush();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                line = sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();     //Closing the
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progressDialog.dismiss();
            userCallback.done(null);

            super.onPostExecute(aVoid);
        }
    }

    public class fetchUserDateAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack userCallback;

        public fetchUserDateAsyncTask(User user, GetUserCallBack userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            StringBuilder sb = new StringBuilder();
            for(String key : data.keySet()) {
                String value = null;
                try {
                    value = URLEncoder.encode(data.get(key), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(sb.length()>0)
                    sb.append("&");

                sb.append(key + "=" + value);
            }
            return sb.toString();
        }

        @Override
        protected User doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("username", user.username);
            dataToSend.put("password", user.password);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            User returnUser = null;

            try {

                URL url = new URL(SERVER_ADDRESS + "FetchUserData.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");

                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

                writer.write(encodedStr);
                writer.flush();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                line = sb.toString();

                Log.i("custom_check","The values received in the store part are as follows:");
                Log.i("custom_check",line);

                JSONObject jObject = new JSONObject(line);

                if (jObject.length() == 0)
                {
                    user = null;
                }
                else
                {
                    String name = jObject.getString("name");
                    returnUser = new User(name, user.username, user.username);

                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();     //Closing the
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return returnUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }

    public class checkUserDateAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack userCallback;

        public checkUserDateAsyncTask(User user, GetUserCallBack userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            StringBuilder sb = new StringBuilder();
            for(String key : data.keySet()) {
                String value = null;
                try {
                    value = URLEncoder.encode(data.get(key), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(sb.length()>0)
                    sb.append("&");

                sb.append(key + "=" + value);
            }
            return sb.toString();
        }

        @Override
        protected User doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("username", user.username);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            User returnUser = null;

            try {

                URL url = new URL(SERVER_ADDRESS + "CheckUserData.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");

                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

                writer.write(encodedStr);
                writer.flush();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                line = sb.toString();

                Log.i("custom_check","The values received in the store part are as follows:");
                Log.i("custom_check",line);

                if (line.equals("[]"))
                {
                    returnUser = new User(user.name, user.username, user.username);
                }
                else
                {
                    user = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();     //Closing the
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return returnUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }
}
