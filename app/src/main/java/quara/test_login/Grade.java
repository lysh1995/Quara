package quara.test_login;

/**
 * Created by Mitchell on 3/15/2016.
 */
public class Grade {

    String username;
    double score; //stored in SQL as float, equivalent to Java double (not float)
    String description;

    public Grade(String username, double score, String description)
    {
        this.username = username;
        this.score = score;
        this.description = description;
    }

    public Grade()
    {
        this.username = "";
        this.score = -1;
        this.description = "";
    }
}
