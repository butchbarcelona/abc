package proj.abc.services;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import proj.abc.GameType;
import proj.abc.MainActivity;

public class RestCallServices {

  public interface RestServiceListener{
    public void onSuccess(RestCalls callType, String string) throws JSONException;
    public void onFailure(RestCalls callType, String string);
  }

  public String mainUrl = "http://192.168.43.150/kidsgames/";
                          //"http://livix.com.ph/kidsgames/"

  public RestCallServices(){

  }

  public void submitScore(final RestServiceListener listener, int userId, String category, int score){

    final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new NameValuePair("id",userId+""));
    params.add(new NameValuePair("category", GameType.getGameKindString(category)));
    params.add(new NameValuePair("score",score+""));

    final String getPath = mainUrl + "index.php/landing/mobile_submit";

    new RestAsyncTask(new RestAsyncTask.RestAsyncTaskListener() {

      String jsonResults;

      @Override
      public void doInBackground() {
        jsonResults = get(getPath, "POST", params);
      }

      @Override
      public void result() {

        if (jsonResults == null || jsonResults.trim().isEmpty()) {
          listener.onFailure(RestCalls.SEND_SCORE, "failed");
        } else {
          try {
            listener.onSuccess(RestCalls.SEND_SCORE, jsonResults);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

      }
    }).execute();

  }


  public void login(final RestServiceListener listener, String username, String password){

    username.replace(" ", "+");

    if(username != null && password != null
      && !username.toLowerCase().contains("delete") //since no auth need to check for possible injections
      && !password.toLowerCase().contains("delete")
      && !username.toLowerCase().contains(";")
      && !password.toLowerCase().contains(";"))
    {
      final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new NameValuePair("username",username));
      params.add(new NameValuePair("password",password));

      final String getPath = mainUrl + "index.php/landing/mobile_login";
      Log.d("butch","getPath:"+getPath);

      new RestAsyncTask(new RestAsyncTask.RestAsyncTaskListener() {

        String jsonResults;

        @Override
        public void doInBackground() {
          jsonResults = get(getPath, "POST", params);
        }

        @Override
        public void result() {

          Log.d("butch","jsonResults:"+jsonResults);
          if (jsonResults == null || jsonResults.trim().isEmpty()) {
            listener.onFailure(RestCalls.LOGIN, "failed");
          } else {
            try {
              listener.onSuccess(RestCalls.LOGIN, jsonResults);
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

        }
      }).execute();
    }
  }


  /***
   *
   * @param strUrl
   * @return string of JSON
   */
  private String get(final String strUrl, String requestMethod, final ArrayList<NameValuePair> params){


    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    try {

      URL url = new URL(strUrl);
      conn = (HttpURLConnection) url.openConnection();
      conn.setReadTimeout(40000);
      conn.setConnectTimeout(40000);
      conn.setRequestMethod(requestMethod);


      if(requestMethod.equals("POST")) {
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();
      }

      conn.connect();


      InputStream in = new BufferedInputStream(conn.getInputStream());

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));

      String line;
      while ((line = reader.readLine()) != null) {
        jsonResults.append(line);
      }


      /*InputStreamReader in = new InputStreamReader(conn.getInputStream());

      int read;
      char[] buff = new char[1024];
      while ((read = in.read(buff)) != -1) {
        jsonResults.append(buff, 0, read);
      }*/

    } catch (MalformedURLException e) {
      Log.e(MainActivity.APP_CODE, "Error processing URL", e);
      return null;
    } catch(ConnectException e){
      e.printStackTrace();
      Log.e(MainActivity.APP_CODE, "Error connecting API", e);
    } catch (IOException e) {
      e.printStackTrace();
      Log.e(MainActivity.APP_CODE, "Error connecting API", e);
      return null;
    } catch(Exception e) {
      e.printStackTrace();

      Log.e(MainActivity.APP_CODE, "Error connecting API", e);
    }  finally
    {
      if (conn != null) {
        conn.disconnect();
      }
    }

    return jsonResults.toString();

  }

  private static final ScheduledExecutorService worker =
    Executors.newSingleThreadScheduledExecutor();


  private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
    StringBuilder result = new StringBuilder();
    boolean first = true;

    for (NameValuePair pair : params)
    {
      if (first)
        first = false;
      else
        result.append("&");

      result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
      result.append("=");
      if(pair.getName().equals("timeEnd") || pair.getName().equals("timeStart") || pair.getName().equals("dateStart"))
        result.append(pair.getValue());
      else
        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
    }

    Log.d(MainActivity.APP_CODE, result.toString());
    return result.toString();
  }

}
