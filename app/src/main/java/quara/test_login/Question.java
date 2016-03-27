package quara.test_login;

/**
 * Created by ylin9 on 2/10/2016.
 */
public class Question {
    String user_name, user_pos, user_topic, course_name, ta_id;
    String user_notes;

    public Question(String user_name, String user_pos, String user_topic, String course_name)
    {
        this.course_name = course_name;
        this.user_name = user_name;
        this.user_pos = user_pos;
        this.user_topic = user_topic;
    }

    public Question(String user_name, String user_pos, String user_topic, String course_name, String notes)
    {
        this.course_name = course_name;
        this.user_name = user_name;
        this.user_pos = user_pos;
        this.user_topic = user_topic;
        this.user_notes = notes;
        this.ta_id = "";
    }

    public void setAnswering(String ta_id){
        this.ta_id = ta_id;
    }
}
