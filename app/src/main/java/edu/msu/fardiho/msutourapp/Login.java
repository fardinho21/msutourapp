package edu.msu.fardiho.msutourapp;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import edu.msu.fardiho.msutourapp.Server.Server;

public class Login {
    /**
     * Set true if we want to cancel
     */
    private volatile boolean cancel = false;

    private String username = "defaultUser";
    private String password = "deafultPass";
    private MainActivity mainActivity = null;


    Login(String username, String password, MainActivity mainActivity) {
        this.username = username;
        this.password = password;
        this.mainActivity = mainActivity;
    }
    public void onCreateDialog() {

        cancel = false;

        new Thread(new Runnable() {

            @Override
            public void run() {

                Server server = new Server();
                InputStream stream = server.UserLogin(username, password);
                //TODO: Change XML parser to JSON parser
                boolean fail = stream == null;
                if(!fail) {
                    try {

                        if(cancel) {
                            return;
                        }

                        XmlPullParser xml = Xml.newPullParser();
                        xml.setInput(stream, "UTF-8");

                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "msutour");
                        String status = xml.getAttributeValue(null, "status");
                        if(status.equals("ok")) {
                            mainActivity.setLoginSuccessful(true);
                        } else {
                            fail = true;
                            mainActivity.setLoginSuccessful(false);
                        }

                    } catch(IOException ex) {
                        fail = true;
                        mainActivity.setLoginSuccessful(false);
                    } catch(XmlPullParserException ex) {
                        fail = true;
                        mainActivity.setLoginSuccessful(false);
                    } finally {
                        try {
                            stream.close();
                        } catch(IOException ex) {
                        }
                    }
                }

            }


        }).start();




    }
}