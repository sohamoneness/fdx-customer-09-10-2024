package com.fdxUser.app.utilities.async_tasks;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class HttpStatusHandeling
{
    /**
     * This will give the type off status code and the corresponding message in a json format String.
     *
     * @param type The type of http status code.
     * @return Json format String
     * @throws Exception
     */
    public static String getJsonObject(int type) throws Exception
    {
        JSONObject json = null;

        switch (type)
        {
            case HttpURLConnection.HTTP_ACCEPTED:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_ACCEPTED\"}");
                break;
            case HttpURLConnection.HTTP_OK:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_OK\"}");
                break;
            case HttpURLConnection.HTTP_BAD_GATEWAY:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"BAD_GAteWay\"}");
                break;
            case HttpURLConnection.HTTP_BAD_REQUEST:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_BAD_REQUEST\"}");
                break;
            case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_CLIENT_TIMEOUT\"}");
                break;
            case HttpURLConnection.HTTP_CONFLICT:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_CONFLICT\"}");
                break;

            case HttpURLConnection.HTTP_ENTITY_TOO_LARGE:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_ENTITY_TOO_LARGE\"}");
                break;
            case HttpURLConnection.HTTP_FORBIDDEN:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_FORBIDDEN\"}");
                break;
            case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_GATEWAY_TIMEOUT\"}");
                break;
            case HttpURLConnection.HTTP_GONE:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_GONE\"}");
                break;

            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_INTERNAL_ERROR\"}");
                break;
            case HttpURLConnection.HTTP_LENGTH_REQUIRED:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_LENGTH_REQUIRED\"}");
                break;
            case HttpURLConnection.HTTP_MOVED_PERM:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_MOVED_PERM\"}");
                break;
            case HttpURLConnection.HTTP_MOVED_TEMP:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_MOVED_TEMP\"}");
                break;

            case HttpURLConnection.HTTP_MULT_CHOICE:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_MULT_CHOICE\"}");
                break;
            case HttpURLConnection.HTTP_NO_CONTENT:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_NO_CONTENT\"}");
                break;
            case HttpURLConnection.HTTP_NOT_ACCEPTABLE:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_NOT_ACCEPTABLE\"}");
                break;
            case HttpURLConnection.HTTP_NOT_AUTHORITATIVE:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_NOT_AUTHORITATIVE\"}");
                break;
/***************************************************************/

            case HttpURLConnection.HTTP_NOT_FOUND:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_NOT_FOUND\"}");
                break;
            case HttpURLConnection.HTTP_NOT_IMPLEMENTED:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_NOT_IMPLEMENTED\"}");
                break;
            case HttpURLConnection.HTTP_NOT_MODIFIED:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_NOT_MODIFIED\"}");
                break;
            case HttpURLConnection.HTTP_PARTIAL:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_PARTIAL\"}");
                break;

            case HttpURLConnection.HTTP_PAYMENT_REQUIRED:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_PAYMENT_REQUIRED\"}");
                break;
            case HttpURLConnection.HTTP_PRECON_FAILED:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_PRECON_FAILED\"}");
                break;
            case HttpURLConnection.HTTP_PROXY_AUTH:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_PROXY_AUTH\"}");
                break;
            case HttpURLConnection.HTTP_REQ_TOO_LONG:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_REQ_TOO_LONG\"}");
                break;

            case HttpURLConnection.HTTP_RESET:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_RESET\"}");
                break;
            case HttpURLConnection.HTTP_SEE_OTHER:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_SEE_OTHER\"}");
                break;
/*			case HttpURLConnection.HTTP_S:
				json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_SERVER_ERROR\"}");
				break;*/
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_UNAUTHORIZED\"}");
                break;

            case HttpURLConnection.HTTP_UNAVAILABLE:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_UNAVAILABLE\"}");
                break;
            case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_UNSUPPORTED_TYPE\"}");
                break;
            case HttpURLConnection.HTTP_USE_PROXY:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_USE_PROXY\"}");
                break;
            case HttpURLConnection.HTTP_VERSION:
                json = new JSONObject("{\"status\":\"0\",\"msg\":\"HTTP_VERSION\"}");
                break;
            default:
                break;
        }

        return json.toString();
    }

}
