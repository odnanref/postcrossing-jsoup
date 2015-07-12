package com.far.postcrossing.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import com.far.postcrossing.jsoup.Data.Account;

/**
 * Created by andref on 01-03-2015.
 */
public class Auth {

    HashMap<String, String> cookies = new HashMap<String, String>();

    private static final String PST_LOGIN = "?";

    private Account account;

    private boolean isAuthenticated = false;

    Connection.Response res;

    String username;

    String pass;

    Log Log;

    public Auth(String username, String password) {
        this.username = username;
        this.pass = password;
        Log = new Log();
    }

    public void goAuth() {
        try {
            Connect connect = new Connect(cookies);
            String CSRF = connect.getCSRF("https://" + PostCrossing.URI, "signin__csrf_token");
            res = connect.fetch("https://" + PostCrossing.URI, "POST")
                    .data("signin[_csrf_token]", CSRF)
                    .data("signin[username]", this.username)
                    .data("signin[password]", this.pass)
                    .data("signin[remember]", "1")
                    //.timeout(3000)
                    .execute();
            cookies.putAll(res.cookies());
            isAuthenticated();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAuthenticated()
    {
        if (isAuthenticated == true ) {
            return true;
        }
        Log.d("isAuth", "checking!");
        Document doc = null;
        try {
            doc = res.parse();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("isAuth", "Fail!");
            return false;
        }

        Elements webLoginContainer = doc.select("div#profileAbout");

        if (webLoginContainer.size() <= 0 || webLoginContainer.isEmpty()) {
            // FIXME issue error alert and activity must pick it up
            //Toast tsError = Toast.makeText(getApplicationContext(), "Invalid username or password.", Toast.LENGTH_LONG);
            //tsError.setGravity(Gravity.CENTER, 0, 0);
            //tsError.show();
            isAuthenticated = false;
            Log.d("isAuth", "Failed auth not completed!");
            return false; // failed auth
        } else {
            Log.d("isAuth", "Success true!");
            isAuthenticated = true;
            updateAccountData(1, doc);
            return true;
        }
    }

    public Account updateAccountData(int number, Document doc)
    {
        Log.d(this.getClass().toString() + "isAuth", "updateAccountData!");
        account = new Account();
        account.sent = doc.getElementsByTag("h4").get(0).getElementsByTag("span").html();
        account.received = doc.getElementsByTag("h4").get(1).getElementsByTag("span").html();
        Log.d(this.getClass().toString() + "isAuth", "updateAccountData! " + account.sent + "/" + account.received );

        return account;
    }

    /**
     * Return cookies from already authed state
     *
     * @return Map<String, String>
     */
    public Map<String, String> getCookies() {
        return this.cookies;
    }

    public Account getAccountInformation()
    {
        return account;
    }

    public String getUsername() {
        return this.username;
    }

}
