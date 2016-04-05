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

    public String getUsername()
    {
        return this.username;
    }

    public double getScore()
    {
        return this.score;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setScore(double score)
    {
        this.score = score;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
