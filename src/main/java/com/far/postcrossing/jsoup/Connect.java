package com.far.postcrossing.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

/**
 * Abstraction for connections to the Postcrossing website
 *
 *
 * Created by andref on 07-03-2015.
 */
public class Connect {

    Map<String,String> cookies = null;

    Connection conn;
    Log Log;

    public Connect(Map<String, String> Cookies)
    {
        cookies = Cookies;
        Log = new Log();
    }
    /**
     * Return CSRF for POST requests
     *
     * @param URI
     * @param csrf_name
     * @return String
     */
    public String getCSRF(String URI, String csrf_name) {
        fetch(URI, "GET");
        Document doc = null;
        try {
            Log.d(this.getClass().toString(), conn.toString() + " URI " + URI.toString() + " GET ");
            if (conn != null) {
                HttpConnection.Response Exec = (HttpConnection.Response) conn.execute();
                doc = Exec.parse();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // log error
            System.exit(0);
        }
        Log.d(this.getClass().toString(), "LA VAI" + doc.getElementById(csrf_name).val().toString());

        return doc.getElementById(csrf_name).val();
    }

    public Connection fetch(String URI, String meth) {
        Log.d(this.getClass().toString(), "Connect fetch " + URI.toString() + " method " + meth);
//        System.setProperty("javax.net.ssl.trustStore", "/path/to/web2.uconn.edu.jks");
    
        conn = Jsoup.connect(URI);
        Log.d(this.getClass().toString(), "COnn " + conn.toString());

        if (cookies.size() > 0 ) {
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                conn.cookie(entry.getKey(), entry.getValue());
            }
        }

        if (meth.toLowerCase().contains("get")) {
            conn.method(Connection.Method.GET);
        } else {
            conn.method(Connection.Method.POST);
        }

        return conn;
    }

    public Connection getConnection()
    {
        return this.conn;
    }
    
}
