package com.example.matti.myapplication;

        import android.content.Context;
        import android.widget.Toast;

        import java.util.Date;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.FileInputStream;
        import org.json.JSONObject;

public class JsonManager {

    private Context current_context;
    private File user_profile;

    // get user info and save it to local json file
    // return value: true --> operation done, false --> operation failed
    public Boolean registerUser(String name, String surname, String blood)
    {

        try
        {
            JSONObject root = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("name", name);
            user.put("surname", surname);
            user.put("blood", blood);
            root.put("user", user);

            // write to file
            // -->
            FileOutputStream stream = new FileOutputStream(user_profile);
            try {
                stream.write(root.toString().getBytes());
            }
            finally {
                stream.close();
            }
            // <--
        }
        catch (Throwable t)
        {
            return false;
        }
        return true;

    }

    public String readUser()
    {
        try {
            int length = (int) user_profile.length();
            if (length == 0)
                return new String("");
            byte[] bytes = new byte[length];

            FileInputStream in = new FileInputStream(user_profile);
            try {
                in.read(bytes);
            } finally {
                in.close();
            }
            String result =  new String(bytes);
            JSONObject user = new JSONObject(result);
            return user.getJSONObject("user").toString();
        }
        catch (Throwable t)
        {
            return new String("");
        }
    }

    public Boolean isUserRegistered()
    {
        if (readUser().isEmpty())
            return false;
        else
            return true;
    }

    public String getJSONStringToSend(double latitude, double longitude, String type, int priority_code,  String description, String contact)
    {
        try {
            // create the root node
            JSONObject root = new JSONObject();

            // create and append the user node
            JSONObject user = new JSONObject(readUser());
            root.put("user", user);

            // create and append alert information node
            JSONObject alert = new JSONObject();
            alert.put("latitude", latitude);
            alert.put("longitude", longitude);
            alert.put("type", type);
            alert.put("priority_code", priority_code);
            alert.put("description", description);
            alert.put("contact", contact);
            alert.put("timestamp", (new Date()).getTime()/1000);
            root.put("alert", alert);
            Toast.makeText(current_context, root.toString(), Toast.LENGTH_SHORT).show();
            return root.toString();
        }
        catch (Throwable t)
        {
            return new String("");
        }
    }

    public String getJSONStringToSend(double latitude, double longitude, String description, String contact)
    {
        return getJSONStringToSend(latitude, longitude, "Generic", 0,  description, contact);
    }

    public String getJSONStringToSend(double latitude, double longitude, String contact)
    {
        return getJSONStringToSend(latitude, longitude, "Generic", 0,  "Generic aid request from user", contact);
    }

    // for test purpouse only, automatically initialize every alert field
    public String getJSONStringToSend_test()
    {
        return getJSONStringToSend(45.0, 7.0, "Generic", 0,  "Generic aid request from user", "118");
    }

    // the constructor needs your app context, that you can get with a call to the API getApplicationContext() from your activity
    public JsonManager(Context c)
    {
        current_context = c;
        File path = c.getFilesDir();
        user_profile = new File(path, "user_profile.json");
    }

	/*
	* usage example:
	* create the object instance
	* JsonManager my_jsm = new JsonManager(getApplicationContext());
	* register the user
	* my_jsm.registerUser("pinco","pallo","a");
	* get a json string
	* my_jsm.getJSONStringToSend_test();
	*
	 */

}