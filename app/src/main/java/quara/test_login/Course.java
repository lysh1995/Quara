package quara.test_login;

/**
 * Created by ylin9 on 2/6/2016.
 */
public class Course {
    String course_name, description;

    public Course(String course_name, String description)
    {
        this.course_name = course_name;
        this.description = description;
    }

    public String getCourse_name()
    {
        return this.course_name;
    }

    public String getDescription()
    {
        return this.description;
    }

}
