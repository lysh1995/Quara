package quara.test_login;

public class User {
    String name, username, password;

    public User(String name, String username, String password)
    {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.name = "";
    }

    public String getName()
    {
        return this.name;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }
}
