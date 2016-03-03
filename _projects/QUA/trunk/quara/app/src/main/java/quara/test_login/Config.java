package quara.test_login;

/**
 * Created by ylin9 on 3/2/2016.
 */
public interface Config {

    // used to share GCM regId with application server - using php app server
    static final String APP_SERVER_URL = "http://quara2016.web.engr.illinois.edu/gcm/gcm.php?shareRegId=1";

    // Google Project Number
    static final String GOOGLE_PROJECT_ID = "470822730050";
    static final String MESSAGE_KEY = "message";

}
