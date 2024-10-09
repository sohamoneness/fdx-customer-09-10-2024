package com.fdxUser.app.utilities.async_tasks;

import android.os.AsyncTask;
import android.util.Log;

public class RemoteAsync extends AsyncTask<String, Void, String> {
    private String string_JSON;

    /**
     * Creating an instance of the {@link AsyncResponse} interface to receive
     * {@code processFinish()}
     */
    public AsyncResponse delegate = null;
    private String url;
    public String type = "";

    // Constants
    public static final String GOOGLE_ADDRESS_PICKER = "googleAddrPicker";
    public static final String GOOGLE_LATLNG_PICKER = "googleLatlngPicker";
    public static final String GET_ADDRESS_ON_CAMERA_CHANGE = "getAddressOnCameraChange";



    /**
     * Pass the Url of the web service as a String
     * <p/>
     * url
     */
    public RemoteAsync(String url) {
        Log.e("SERVICE URL # ", url);
        this.url = url;
    }

    @Override
    protected String doInBackground(String... params) {

        if (type.equals(GOOGLE_ADDRESS_PICKER)) {
            String s=params[0];
            int p = s.indexOf("=");
            String s1 = s.substring(p);
            Log.e("loc?>>",s1);
            string_JSON= AutoCompleteConnection.autocomplete(s1);
        }
        else if (type.equals(GOOGLE_LATLNG_PICKER)) {
            HttpUrlconnection httpUrlconnection = new HttpUrlconnection();
            string_JSON = httpUrlconnection.getGetresponse(url , params[0]);
            //getGoogleLatlngPicker(params[0]);
        }else{
//            HttpConnection connection = new HttpConnection();
//            string_JSON = connection.getPostRespoonse(url, pairs[0]);
            HttpUrlconnection httpUrlconnection = new HttpUrlconnection();
            string_JSON = httpUrlconnection.getPostresponse(url , params[0]);
        }

        return string_JSON;
    }

    @Override
    protected void onPostExecute(String jsonString) {
        delegate.processFinish(type, jsonString);
    }

    private String getGoogleLatlngPicker(String place_id) {
        HttpUrlconnection conn = new HttpUrlconnection();

        Log.e("url_test>>","https://maps.googleapis.com/maps/api/place/details/json?placeid=\" + place_id + \"&key=AIzaSyDzZucI3DFyg6-JxaIFqYCNREX8FT72JAM");
        String result = conn.getGetresponse(
                "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=AIzaSyDzZucI3DFyg6-JxaIFqYCNREX8FT72JAM"
                , null);

        return result;
    }

}

