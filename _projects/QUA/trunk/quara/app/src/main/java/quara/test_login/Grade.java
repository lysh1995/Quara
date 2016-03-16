package quara.test_login;

/**
 * Created by Mitchell on 3/15/2016.
 */
public class Grade {

    String username;
    float score;
    String description;

    public Grade(String username, float score, String description)
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
