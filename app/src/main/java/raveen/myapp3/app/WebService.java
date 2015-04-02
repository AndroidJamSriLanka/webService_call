package raveen.myapp3.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;


class WebService  extends AsyncTask<String, Void, String> {

    static InputStream is = null;
    private String Content;
    private String Error = null;
    private String request_type;

    public WebService(String type)
    {
        request_type = type;
    }


    @Override
    protected String doInBackground(String... params) {
        BufferedReader reader=null;
        try {
            // Defined URL  where to send data
            String url = params[0].toString();
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();

            for (int i=1;i<params.length-1;i++) {
                pairs.add(new BasicNameValuePair(params[i].toString(),params[i+1].toString()));
            }

            DefaultHttpClient httpClient = new DefaultHttpClient();

            //if it's a GET Request
            if(request_type.equals("get")){
                HttpGet httpRequest = new HttpGet(url.toString());
                HttpResponse httpResponse = httpClient.execute(httpRequest);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "");
                }
                // Append Server Response To Content String
                Content = sb.toString();
            }
            //if it's a POST Request
            else if(request_type.equals("post")){
                HttpPost httpRequest = new HttpPost(url.toString());

                httpRequest.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse httpResponse = httpClient.execute(httpRequest);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "");
                }
                // Append Server Response To Content String
                Content = sb.toString();
            }

        }
        catch(InterruptedIOException e){
            Error = e.getMessage();
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
            Error = ex.getMessage();
        }
        finally
        {
            try
            {
                reader.close();
            }

            catch(Exception ex) {}
        }
        return Content;
    }

}

