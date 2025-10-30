package com.watutz.app.mobileservices;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.watutz.app.R;
import com.watutz.app.SignInActivity;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.ypyproductions.utils.DirectionUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class ServiceHandler implements IWhereMyLocationConstants
{

    private JSONObject response = null;

    JSONArray json = new JSONArray();

    public final static int POST = 1;
    
    public final static int GET = 2;

    public final static int PUT = 3;

    Activity parentActivity;
    StoreAndGetFromPrefs storageObject;


   

    public ServiceHandler(Activity theActivity) {
		// TODO Auto-generated constructor stub
    	parentActivity= theActivity;
    	storageObject= new StoreAndGetFromPrefs(parentActivity);

	}

	public ServiceObject makeServiceCall( String url, int method, List<String> params, JSONObject urlParams)
    {

        try
        {
        	
        	//Certificate Pinning
            HttpParams httpParams = new BasicHttpParams();
            updateAndroidSecurityProvider();
            final SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.unregister("https");
            schemeRegistry.register(new Scheme("https", createAdditionalCertsSSLSocketFactory(), 443));





            final ThreadSafeClientConnManager clientMan = new ThreadSafeClientConnManager(httpParams,schemeRegistry);

            // HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient(clientMan, httpParams);
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            httpParams = httpClient.getParams();


            String unEncryptedParams= urlParams.toString();
			
			String encryptedParams = "";

			try {

				// AesCipher encRYPT = new AesCipher();
				encryptedParams = CypherController.encrypt(SKE, unEncryptedParams).getData();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

//			JSONObject jsonObjectParams = new JSONObject();
//			try {
//				jsonObjectParams.put("dataenc", encryptedParams);
//			} catch (JSONException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}

			StringEntity rawParams = new StringEntity(unEncryptedParams);

			Log.d("Ze Url","Ze Url"+url+unEncryptedParams+"&token="+storageObject.retrieveTokenFromPrefs());

			
			if (method == POST) 
			{
			   HttpPost httpPost = new HttpPost(url);
               httpPost.setParams(httpParams);
               httpPost.setHeader("Accept", "application/json");
               httpPost.setHeader("content-language", storageObject.retrieveLanguageFromPrefs());
               httpPost.setHeader("content-type", "application/json");
               httpPost.setHeader("COOKIE" ,storageObject.retrieveTokenCookieStringFromPrefs());


                //Verifying Identity so check identity token
               if(url.contains(USER_VERIFY_IDENTITY_URL))
               {
                   httpPost.setHeader("Authorization", "Bearer "+storageObject.retrieveUserVerifyTokenFromPrefs());

               }
               else
               {
                   httpPost.setHeader("Authorization", "Bearer "+storageObject.retrieveTokenFromPrefs());

               }
               httpPost.setEntity(rawParams);
			   httpResponse = httpClient.execute(httpPost);
			   

		    }

            else if (method == PUT)
            {
                HttpPut httpPost = new HttpPut(url);
                httpPost.setParams(httpParams);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("content-language", storageObject.retrieveLanguageFromPrefs());
                httpPost.setHeader("content-type", "application/json");
                httpPost.setHeader("COOKIE" ,storageObject.retrieveTokenCookieStringFromPrefs());


                //Verifying Identity so check identity token
                if(url.contains(USER_VERIFY_IDENTITY_URL))
                {
                    httpPost.setHeader("Authorization", "Bearer "+storageObject.retrieveUserVerifyTokenFromPrefs());

                }
                else
                {
                    httpPost.setHeader("Authorization", "Bearer "+storageObject.retrieveTokenFromPrefs());

                }
                httpPost.setEntity(rawParams);
                httpResponse = httpClient.execute(httpPost);


            }
            else if (method == GET)
			{
				
			//url+=rawParams;

			HttpGet httpGet = new HttpGet(url);
			httpGet.setParams(httpParams);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("content-language", storageObject.retrieveLanguageFromPrefs());
            httpGet.setHeader("content-type", "application/json");
            httpGet.setHeader("COOKIE" ,storageObject.retrieveTokenCookieStringFromPrefs());


            httpResponse = httpClient.execute(httpGet);




            }




            if(url.equals(USER_LOGIN_URL))
            {
                CookieStore cookieStore = httpClient.getCookieStore();
                // get Cookies
                List<Cookie> cookies = cookieStore.getCookies();
                String cookieString="";

                if (cookies.isEmpty()) {
                    //Log.d("ZeCookie","no cookies received");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        cookieString+= cookies.get(i).getName()+"="+ cookies.get(i).getValue()+";";
                    }
                }
                storageObject.saveTokenCookieStringToPrefs(cookieString);
                //Save Cookies Headers
                for (Cookie cookie:cookies) {

                    if(cookie.getName().equals("uid"))
                    {
                        storageObject.saveTokenUidToPrefs(cookie.getValue());
                    }

                    if(cookie.getName().equals("client"))
                    {
                        storageObject.saveTokenClientToPrefs(cookie.getValue());
                    }


                    if(cookie.getName().equals("access-token"))
                    {
                        storageObject.saveTokenToPrefs(cookie.getValue());
                    }
                }
            }


            httpClient = null;

            httpEntity = httpResponse.getEntity();
            
            int responseCode = httpResponse.getStatusLine().getStatusCode();



            if(responseCode == 401 && !url.contains(IWhereMyLocationConstants.USER_LOGIN_URL))
            {
                //return;
            	logOutUser();
            	
            }
            
            httpResponse = null;

            String JSONString = EntityUtils.toString( httpEntity );


            try
            {
         
                response = new JSONObject( JSONString );

            }catch
            ( JSONException e ) 
            {
            	e.printStackTrace();
            	logLongErrorResponse("Ze Responze Error","Ze Response Error URL:"+url+urlParams.toString()+"Ze Response Error:"+JSONString);

            }

            httpEntity = null;

        }catch(Exception e )
        {

            e.printStackTrace();

        }
        
        
        if(response !=null)
        {
        	ServiceObject theObject = new ServiceObject();
        	theObject.setResult(response.toString());
        	theObject.setParams(params);
        	return 	theObject;
        }
        else
        {
        	ServiceObject theObject = new ServiceObject();
        	theObject.setResult("");
        	theObject.setParams(params);
        	return 	theObject;
        }
        

    }
    
    public String fixParams(String params)
    {
    	String editedParams=params;
    	List<String> charsToDelete = new ArrayList<String>();
    	

    	for(int i=0;i<editedParams.length();i++)
    	{

    		if(editedParams.charAt(i)== ' ')
    		{
    			if(editedParams.charAt(i+1)== '&')
    			{
    				charsToDelete.add(""+i);
    		    	editedParams= replaceCharAt(editedParams, i,'~');
    				 
    				 
    			}
    		}
    		
			

    	}


    	editedParams= editedParams.replaceAll("~", "");
    	
    	
    	return editedParams;
    }
    
    public String replaceCharAt(String s, int pos, char c)
    {
    	StringBuffer buf= new StringBuffer(s);
    	buf.setCharAt(pos, c);
    	
    	return buf.toString();

    }
    
    public String removeCharAt(String s, int pos)
    {
    	StringBuffer buf= new StringBuffer(s.length()-1);
    	buf.append(s.substring(0,pos)).append(s.substring(pos+1));
    	return buf.toString();
    	
    }
    
    public static void logLongErrorResponse(String TAG, String message) {
        int maxLogSize = 5000;
        for(int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            android.util.Log.d(TAG, message.substring(start, end));
        }
    }
    
    public void logOutUser()
	  {
		  storageObject.logOutUser();

		  Intent mIntent = new Intent(parentActivity, SignInActivity.class);
	 	  mIntent.putExtra(KEY_START_FROM, "main");
          mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  DirectionUtils.changeActivity(parentActivity, R.anim.slide_in_from_left, R.anim.slide_out_to_right, true, mIntent);
		
		  return;
	  }

    protected org.apache.http.conn.ssl.SSLSocketFactory createAdditionalCertsSSLSocketFactory() {
        try {
            final KeyStore ks = KeyStore.getInstance("BKS");

            // the bks file we generated above
            final InputStream in = parentActivity.getResources().openRawResource( R.raw.lifeline);
            try {
                // don't forget to put the password used above in strings.xml/mystore_password
                ks.load(in, CP.toCharArray());
            } finally {
                in.close();
            }

            return new AdditionalKeyStoresSSLSocketFactory(ks);

        } catch( Exception e ) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Allows you to trust certificates from additional KeyStores in addition to
     * the default KeyStore
     */
    public class AdditionalKeyStoresSSLSocketFactory extends SSLSocketFactory {
        protected SSLContext sslContext = SSLContext.getInstance("TLS");

        public AdditionalKeyStoresSSLSocketFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(null, null, null, null, null, null);
            sslContext.init(null, new TrustManager[]{new AdditionalKeyStoresTrustManager(keyStore)}, new SecureRandom());

        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }



        /**
         * Based on http://download.oracle.com/javase/1.5.0/docs/guide/security/jsse/JSSERefGuide.html#X509TrustManager
         */
        public  class AdditionalKeyStoresTrustManager implements X509TrustManager {

            protected ArrayList<X509TrustManager> x509TrustManagers = new ArrayList<X509TrustManager>();


            protected AdditionalKeyStoresTrustManager(KeyStore... additionalkeyStores) {
                final ArrayList<TrustManagerFactory> factories = new ArrayList<TrustManagerFactory>();

                try {
                    // The default Trustmanager with default keystore
                    final TrustManagerFactory original = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    original.init((KeyStore) null);
                    factories.add(original);

                    for( KeyStore keyStore : additionalkeyStores ) {
                        final TrustManagerFactory additionalCerts = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                        additionalCerts.init(keyStore);
                        factories.add(additionalCerts);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }



                /*
                 * Iterate over the returned trustmanagers, and hold on
                 * to any that are X509TrustManagers
                 */
                for (TrustManagerFactory tmf : factories)
                    for( TrustManager tm : tmf.getTrustManagers() )
                        if (tm instanceof X509TrustManager)
                            x509TrustManagers.add( (X509TrustManager)tm );


                if( x509TrustManagers.size()==0 )
                    throw new RuntimeException("Couldn't find any X509TrustManagers");

            }

            /*
             * Delegate to the default trust manager.
             */
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                final X509TrustManager defaultX509TrustManager = x509TrustManagers.get(0);
                defaultX509TrustManager.checkClientTrusted(chain, authType);
            }

            /*
             * Loop over the trustmanagers until we find one that accepts our server
             */
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                for( X509TrustManager tm : x509TrustManagers ) {
                    try {
                        tm.checkServerTrusted(chain,authType);
                        return;
                    } catch( CertificateException e ) {
                        // ignore
                    }
                }
                throw new CertificateException();
            }

            public X509Certificate[] getAcceptedIssuers() {
                final ArrayList<X509Certificate> list = new ArrayList<X509Certificate>();
                for( X509TrustManager tm : x509TrustManagers )
                    list.addAll(Arrays.asList(tm.getAcceptedIssuers()));
                return list.toArray(new X509Certificate[list.size()]);
            }
        }

    }
    private void updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(parentActivity);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), parentActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

}