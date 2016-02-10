package quara.test_login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerRequests {

    ProgressDialog progressDialog;

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADDRESS = "http://quara2016.web.engr.illinois.edu/";

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

    public void getAllCourseInBackground(User user, GetCourseCallBack callBack)
    {
        progressDialog.show();
        new getAllCourseAsyncTask(user, callBack).execute();
    }

    public void getCourseDescriptionInBackground(Course course, GetDescriptionCallBack callBack)
    {
        progressDialog.show();
        new getAllCourseDescriptionAsyncTask(course, callBack).execute();
    }

    public void getQueueInBackground(Queue queue, GetQueueCallBack callBack)
    {
        progressDialog.show();
        new getAllQueueAsyncTask(queue, callBack).execute();
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
                    returnUser = new User(user.name, user.username, user.password);
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

    public class getAllCourseAsyncTask extends AsyncTask<Void, Void, Map> {
        User user;
        GetCourseCallBack CourseCallback;

        public getAllCourseAsyncTask(User user, GetCourseCallBack CourseCallback) {
            this.user = user;
            this.CourseCallback = CourseCallback;
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
        protected Map doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("username", user.username);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            Map returnCourse = new HashMap();

            try {

                URL url = new URL(SERVER_ADDRESS + "GetAllCourse.php");

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

                if (!line.equals("[]"))
                {
                    //split json string to map
                    line = line.substring(1);
                    line = line.substring(0, line.length() - 1);
                    String[] temp_list = line.split(",");
                    for (int i = 0; i < temp_list.length; i++)
                    {
                        String new_element = temp_list[i];
                        new_element = new_element.substring(1);
                        new_element = new_element.substring(0, new_element.length() - 1);
                        String temp[] = new_element.split(":");
                        String key = temp[0];
                        key = key.substring(1);
                        key = key.substring(0, key.length() - 1);
                        String value = temp[1];
                        value = value.substring(1);
                        value = value.substring(0, value.length() - 1);
                        returnCourse.put(key, value);
                    }
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
            return returnCourse;
        }

        @Override
        protected void onPostExecute(Map returnCourse) {
            progressDialog.dismiss();
            CourseCallback.done(returnCourse);
            super.onPostExecute(returnCourse);
        }
    }

    public class getAllCourseDescriptionAsyncTask extends AsyncTask<Void, Void, String> {
        Course course;
        GetDescriptionCallBack DescriptionCallback;

        public getAllCourseDescriptionAsyncTask(Course course, GetDescriptionCallBack DescriptionCallback) {
            this.course = course;
            this.DescriptionCallback = DescriptionCallback;
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
        protected String doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("course_name", course.course_name);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            String description = new String();

            try {

                URL url = new URL(SERVER_ADDRESS + "GetSelectedCourse.php");

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

                if (!line.equals("[]"))
                {
                    line = line.substring(1);
                    line = line.substring(0, line.length() - 1);
                    String[] content = line.split(":");
                    description = content[1];
                    description = description.substring(1);
                    description = description.substring(0, description.length() - 1);
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
            return description;
        }

        @Override
        protected void onPostExecute(String returnDescription) {
            progressDialog.dismiss();
            DescriptionCallback.done(returnDescription);
            super.onPostExecute(returnDescription);
        }
    }

    public class getAllQueueAsyncTask extends AsyncTask<Void, Void, Map> {
        Queue queue;
        GetQueueCallBack QueueCallback;

        public getAllQueueAsyncTask(Queue queue, GetQueueCallBack QueueCallback) {
            this.queue = queue;
            this.QueueCallback = QueueCallback;
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
        protected Map doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("course_name", queue.course_name);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            Map queue = new HashMap();

            try {

                URL url = new URL(SERVER_ADDRESS + "GetAllQueue.php");

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

                if (!line.equals("null"))
                {
                    line = line.substring(1);
                    line = line.substring(0, line.length() - 1);
                    String[] temp_list = line.split("],");
                    for (int i = 0; i < temp_list.length; i++)
                    {
                        String new_element = temp_list[i];
                        if (new_element.charAt(0) == '[')
                            new_element = new_element.substring(1);
                        if (new_element.charAt(new_element.length()-1) == ']')
                            new_element = new_element.substring(0, new_element.length() - 1);
                        new_element = new_element.substring(1);
                        new_element = new_element.substring(0, new_element.length() - 1);
                        String temp_info[] = new_element.split(",");
                        Map user_info = new HashMap();
                        for (int j = 0; j < temp_info.length; j++)
                        {
                            String new_info = temp_info[j];
                            String temp[] = new_info.split(":");
                            String key = temp[0];
                            key = key.substring(1);
                            key = key.substring(0, key.length() - 1);
                            String value = temp[1];
                            value = value.substring(1);
                            value = value.substring(0, value.length() - 1);
                            user_info.put(key, value);
                        }

                        queue.put(i, user_info);
                    }
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
            return queue;
        }

        @Override
        protected void onPostExecute(Map returnQueue) {
            progressDialog.dismiss();
            QueueCallback.done(returnQueue);
            super.onPostExecute(returnQueue);
        }
    }

}
