package quara.test_login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.games.quest.Quest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
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

    public String getEncodedData(Map<String,String> data) {
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

    public void getQueueInBackground(Question queue, GetQueueCallBack callBack)
    {
        progressDialog.show();
        new getAllQueueAsyncTask(queue, callBack).execute();
    }

    public void insertQueueInBackground(Question queue, GetQueueCallBack callBack)
    {
        progressDialog.show();
        new InsertQueueAsyncTask(queue, callBack).execute();
    }

    public void insertGradeInBackground(Grade grade, GetGradeCallBack callBack)
    {
        progressDialog.show();
        new InsertGradeAsyncTask(grade, callBack).execute();
    }

    public void getGradeInBackground(User user, GetGradeCallBack callBack)
    {
        progressDialog.show();
        new GetGradeAsyncTask(user, callBack).execute();
    }

    public void deleteQueueInBackground(Question queue, GetQueueCallBack callBack)
    {
        progressDialog.show();
        new DeleteQueueAsyncTask(queue, callBack).execute();
    }

    public void clearQueue(Question queue, GetQueueCallBack callBack){
        progressDialog.show();
        new ClearQueueAsyncTask(queue, callBack).execute();

    }
    public void editQueueInBackground(Question queue, GetQueueCallBack callBack)
    {
        progressDialog.show();
        new EditQueueAsyncTask(queue, callBack).execute();
    }

    public void answerQuestion(Question queue, GetQueueCallBack callBack)
    {
        progressDialog.show();
        new answerQuestionAsyncTask(queue, callBack).execute();
    }

    public void CheckAuthorisationInBackground(TA ta, CheckAuthorisationCallBack callBack)
    {
        progressDialog.show();
        new CheckAuthorisationAsyncTask(ta, callBack).execute();
    }

    public void setOnDutyInBackground(TA ta, UpdateDutyCallBack callBack)
    {
        progressDialog.show();
        new setOnDutyAsyncTask(ta, callBack).execute();
    }

    public void setOffDutyInBackground(TA ta, UpdateDutyCallBack callBack)
    {
        progressDialog.show();
        new setOffDutyAsyncTask(ta, callBack).execute();
    }

    public void getOnDutyTAInBackground(TA ta, getOnDutyCallBack callBack)
    {
        progressDialog.show();
        new getOnDutyTAAsyncTask(ta, callBack).execute();
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
            return ServerRequests.this.getEncodedData(data);
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
            return ServerRequests.this.getEncodedData(data);
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

                if (line.equals("[]"))
                {
                    user = null;
                }
                else
                {
                    JSONObject jObject = new JSONObject(line);
                    String name = jObject.getString("name");
                    returnUser = new User(name, user.username, user.password);

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
            return ServerRequests.this.getEncodedData(data);
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
            return ServerRequests.this.getEncodedData(data);
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
            return ServerRequests.this.getEncodedData(data);
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

    public class getAllQueueAsyncTask extends AsyncTask<Void, Void, ArrayList> {
        Question question;
        GetQueueCallBack QueueCallback;

        public getAllQueueAsyncTask(Question question, GetQueueCallBack QueueCallback) {
            this.question = question;
            this.QueueCallback = QueueCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("course_name", question.course_name);

            String encodedStr = getEncodedData(dataToSend);
            Log.i("send_check", "the data sent is: " + encodedStr);

            BufferedReader reader = null;

            ArrayList queue = new ArrayList();

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

                        queue.add(user_info);
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
        protected void onPostExecute(ArrayList returnQueue) {
            progressDialog.dismiss();
            QueueCallback.done(returnQueue);
            super.onPostExecute(returnQueue);
        }
    }

    public class InsertQueueAsyncTask extends AsyncTask<Void, Void, ArrayList> {
        Question question;
        GetQueueCallBack QueueCallback;

        public InsertQueueAsyncTask(Question question, GetQueueCallBack QueueCallback) {
            this.question = question;
            this.QueueCallback = QueueCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("user_name", question.user_name);
            dataToSend.put("user_pos", question.user_pos);
            dataToSend.put("user_topic", question.user_topic);
            dataToSend.put("user_notes", question.user_notes); // foobar
            dataToSend.put("course_name", question.course_name);
            dataToSend.put("ta_id", question.ta_id);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            ArrayList queue = new ArrayList();

            try {

                URL url = new URL(SERVER_ADDRESS + "Enqueue.php");

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
        protected void onPostExecute(ArrayList returnQueue) {
            progressDialog.dismiss();
            QueueCallback.done(returnQueue);
            super.onPostExecute(returnQueue);
        }
    }

    public class EditQueueAsyncTask extends AsyncTask<Void, Void, ArrayList> {
        Question question;
        GetQueueCallBack QueueCallback;

        public EditQueueAsyncTask(Question question, GetQueueCallBack QueueCallback) {
            this.question = question;
            this.QueueCallback = QueueCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("user_name", question.user_name);
            dataToSend.put("user_pos", question.user_pos);
            dataToSend.put("user_topic", question.user_topic);
            dataToSend.put("user_notes", question.user_notes); // foobar
            dataToSend.put("course_name", question.course_name);
            dataToSend.put("ta_id", question.ta_id);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            ArrayList queue = new ArrayList();

            try {

                URL url = new URL(SERVER_ADDRESS + "Editqueue.php");

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
        protected void onPostExecute(ArrayList returnQueue) {
            progressDialog.dismiss();
            QueueCallback.done(returnQueue);
            super.onPostExecute(returnQueue);
        }
    }

    public class ClearQueueAsyncTask extends AsyncTask<Void, Void, ArrayList> {
        Question question;
        GetQueueCallBack QueueCallback;

        public ClearQueueAsyncTask(Question question, GetQueueCallBack QueueCallback) {
            this.question = question;
            this.QueueCallback = QueueCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("course_name", question.course_name);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            ArrayList queue = new ArrayList();

            try {

                URL url = new URL(SERVER_ADDRESS + "clearQueue.php");

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
        protected void onPostExecute(ArrayList returnQueue) {
            progressDialog.dismiss();
            QueueCallback.done(returnQueue);
            super.onPostExecute(returnQueue);
        }
    }

    public class DeleteQueueAsyncTask extends AsyncTask<Void, Void, ArrayList> {
        Question question;
        GetQueueCallBack QueueCallback;

        public DeleteQueueAsyncTask(Question question, GetQueueCallBack QueueCallback) {
            this.question = question;
            this.QueueCallback = QueueCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("user_name", question.user_name);
            dataToSend.put("course_name", question.course_name);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            ArrayList queue = new ArrayList();

            try {

                URL url = new URL(SERVER_ADDRESS + "Dequeue.php");

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
        protected void onPostExecute(ArrayList returnQueue) {
            progressDialog.dismiss();
            QueueCallback.done(returnQueue);
            super.onPostExecute(returnQueue);
        }
    }

    /**
     * Insert a grade into the DB. TODO fix me
     */
    public class InsertGradeAsyncTask extends AsyncTask<Void, Void, ArrayList> {
        Grade grade;
        GetGradeCallBack GradeCallback;

        public InsertGradeAsyncTask(Grade grade, GetGradeCallBack GradeCallback) {
            this.grade = grade;
            this.GradeCallback = GradeCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("username", grade.username);
            dataToSend.put("score", grade.score);
            dataToSend.put("description", grade.description);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            ArrayList grade = new ArrayList(); //TODO fix it!

            try {

                URL url = new URL(SERVER_ADDRESS + "AddGrade.php");

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
            return grade;
        }

        @Override
        protected void onPostExecute(ArrayList returnGrade) {
            progressDialog.dismiss();
            GradeCallback.done(returnGrade);
            super.onPostExecute(returnGrade);
        }
    }

    /**
     * Get a list of all grades from the DB. TODO fix me
     */
    public class GetGradeAsyncTask extends AsyncTask<Void, Void, ArrayList> {
        User user;
        GetGradeCallBack GradeCallback;

        public GetGradeAsyncTask(User user, GetGradeCallBack GradeCallback) {
            this.user = user;
            this.GradeCallback = GradeCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("username", user.username);

            String encodedStr = getEncodedData(dataToSend);

            Log.i("user_check", "the data sent is: " + encodedStr);

            BufferedReader reader = null;

            ArrayList grades = new ArrayList();

            try {

                URL url = new URL(SERVER_ADDRESS + "GetUserGrades.php");

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

                            // If it's not a number, take off the " characters at the beginning and end
                            if (!key.equals("score")) {
                                value = value.substring(1);
                                value = value.substring(0, value.length() - 1);
                            }

                            user_info.put(key, value);
                        }

                        grades.add(user_info);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();     //Closing the reader
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return grades;
        }

        @Override
        protected void onPostExecute(ArrayList returnGrade) {
            progressDialog.dismiss();
            GradeCallback.done(returnGrade);
            super.onPostExecute(returnGrade);
        }
    }

    public class CheckAuthorisationAsyncTask extends AsyncTask<Void, Void, String> {
        TA ta;
        CheckAuthorisationCallBack TaCallback;

        public CheckAuthorisationAsyncTask(TA ta, CheckAuthorisationCallBack TaCallback) {
            this.ta = ta;
            this.TaCallback = TaCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected String doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("name", ta.name);
            dataToSend.put("course_name", ta.course_name);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            String ta_info = "";

            try {

                URL url = new URL(SERVER_ADDRESS + "CheckAuthorisation.php");

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
                    ta_info = line;
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
            return ta_info;
        }

        @Override
        protected void onPostExecute(String ta_info) {
            progressDialog.dismiss();
            TaCallback.done(ta_info);
            super.onPostExecute(ta_info);
        }
    }

    public class answerQuestionAsyncTask extends AsyncTask<Void, Void, ArrayList> {
        Question question;
        GetQueueCallBack QueueCallBack;

        public answerQuestionAsyncTask(Question question, GetQueueCallBack QueueCallBack) {
            this.question = question;
            this.QueueCallBack = QueueCallBack;
        }

        private String getEncodedData(Map<String,String> data) {
            return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("user_name", question.user_name);
            dataToSend.put("course_name", question.course_name);
            dataToSend.put("ta_id", question.ta_id);
            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            ArrayList question = new ArrayList();

            try {

                URL url = new URL(SERVER_ADDRESS + "answer.php");

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
            return question;
        }

        @Override
        protected void onPostExecute(ArrayList returnQueue) {
            progressDialog.dismiss();
            QueueCallBack.done(returnQueue);
            super.onPostExecute(returnQueue);
        }
    }

    public class setOnDutyAsyncTask extends AsyncTask<Void, Void, String> {
        TA ta;
        UpdateDutyCallBack TaCallback;

        public setOnDutyAsyncTask(TA ta, UpdateDutyCallBack TaCallback) {
            this.ta = ta;
            this.TaCallback = TaCallback;
        }

        private String getEncodedData(Map<String,String> data) {
           return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected String doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("name", ta.name);
            dataToSend.put("course_name", ta.course_name);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            String ta_info = "";

            try {

                URL url = new URL(SERVER_ADDRESS + "Onduty.php");

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
                    ta_info = line;
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
            return ta_info;
        }

        @Override
        protected void onPostExecute(String ta_info) {
            progressDialog.dismiss();
            TaCallback.done(ta_info);
            super.onPostExecute(ta_info);
        }
    }

    public class setOffDutyAsyncTask extends AsyncTask<Void, Void, String> {
        TA ta;
        UpdateDutyCallBack TaCallback;

        public setOffDutyAsyncTask(TA ta, UpdateDutyCallBack TaCallback) {
            this.ta = ta;
            this.TaCallback = TaCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected String doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("name", ta.name);
            dataToSend.put("course_name", ta.course_name);

            System.out.println("check" + ta.name);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            String ta_info = "";

            try {

                URL url = new URL(SERVER_ADDRESS + "Offduty.php");

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
                    ta_info = line;
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
            return ta_info;
        }

        @Override
        protected void onPostExecute(String ta_info) {
            progressDialog.dismiss();
            TaCallback.done(ta_info);
            super.onPostExecute(ta_info);
        }
    }

    public class getOnDutyTAAsyncTask extends AsyncTask<Void, Void, String[]> {
        TA ta;
        getOnDutyCallBack TaCallback;

        public getOnDutyTAAsyncTask(TA ta, getOnDutyCallBack TaCallback) {
            this.ta = ta;
            this.TaCallback = TaCallback;
        }

        private String getEncodedData(Map<String,String> data) {
            return ServerRequests.this.getEncodedData(data);
        }

        @Override
        protected String[] doInBackground(Void... params) {
            Map dataToSend = new HashMap();
            dataToSend.put("course_name", ta.course_name);

            String encodedStr = getEncodedData(dataToSend);

            BufferedReader reader = null;

            String[] ta_list = new String[20];

            try {

                URL url = new URL(SERVER_ADDRESS + "GetOnDutyTA.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");

                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

                writer.write(encodedStr);
                writer.flush();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                int index = 0;
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    ta_list[index] = line.toString();
                    index++;
                }
                line = sb.toString();

                Log.i("custom_check","The values received in the store part are as follows:");
                Log.i("custom_check",line);

                if (line.equals(""))
                {
                   ta_list = new String[20];
                }
                else
                {
                    
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
            return ta_list;
        }

        @Override
        protected void onPostExecute(String[] ta_list) {
            progressDialog.dismiss();
            TaCallback.done(ta_list);
            super.onPostExecute(ta_list);
        }
    }

}
