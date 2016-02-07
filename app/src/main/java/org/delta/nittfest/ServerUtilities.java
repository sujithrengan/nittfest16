package org.delta.nittfest;



import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public final class ServerUtilities {
	static String status="-1";

	public static void register(final Context context, final String regId) throws InterruptedException, ExecutionException {

		String serverUrl = Utilities.GCM_URL;
		Map<String, String> params = new HashMap<String, String>();
		params.put("regid", regId);// regId

		try {
            if(status!="200")
            {
					post(serverUrl, params);
            Log.e("Serverutil","Register working");
            }
            else
            	Log.d("status",String.valueOf(status));

			
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		return;
	}

	public static void unregister(final Context context, final String regId) throws InterruptedException, ExecutionException {

		String serverUrl = Utilities.GCM_URL + "/unregister";
		Map<String, String> params = new HashMap<String, String>();
		params.put("regid", regId);
		try {
			post(serverUrl, params);
		} catch (IOException e) {
			e.printStackTrace();
		}

			}

	private static  void post(String endpoint, Map<String, String> params)
			throws IOException, InterruptedException, ExecutionException {

		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		
		status=new APost().execute(body, endpoint).get();
		//Log.e("StatusVal",status.toString());

	}

    public static class APost extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            // params[0] is body, params[1] is endpoint
            byte[] bytes = params[0].getBytes();
            URL url;

            HttpURLConnection conn = null;
            try {

                url = new URL(params[1]);
                Log.e("URL", "> " + url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setFixedLengthStreamingMode(bytes.length);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");
                // post the request
                OutputStream out = conn.getOutputStream();

                out.write(bytes);

                out.close();
                // handle the response
                int status = conn.getResponseCode();

                if (status != 200) {
                    throw new IOException("Post failed with error code " + status);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;

        }

        public void execute(String serverUrl, URL url) {

        }

        @Override
        protected void onPostExecute(String result) {

        }

    }

}
