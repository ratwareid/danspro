package com.ratwareid.danspro.tool;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

public class RestBuilder {

    private final String hostURL;
    private final String contentType;
    public HashMap<String,String> parameter = new HashMap<>();
    public HashMap<String,String> header = new HashMap<>();
    private final int DEFAULT_TIMEOUT = 30000;
    public Object body;

    public RestBuilder(String url, String contentType){
        this.hostURL = url;
        this.contentType = contentType;
    }

    public void addBasicAuth(String username,String password) throws Exception{
        String auth = Base64.encodeBase64String((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        header.put(HttpHeaders.AUTHORIZATION,"Basic "+auth);
    }

    public void putParameter(String key,String value){
        parameter.put(key,value);
    }

    public void putHeader(String key,String value){
        header.put(key,value);
    }

    public void putBody(String raw){
        body = raw;
    }

    public void putBody(HashMap<String,String> formData){
        body = buildFormData(formData);
    }

    private URL buildURL() throws MalformedURLException {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(hostURL);
        if (parameter != null) {
            for (Map.Entry<String, String> par : parameter.entrySet()) {
                uriBuilder.queryParam(par.getKey(), par.getValue());
            }
        }
        URI uri = uriBuilder.build().toUri();
        if (uri.toURL().toString().indexOf("https") == 0) { trustAllHosts(); }
        return new URL(uri.toURL().toString());
    }

    private void prepareHeader(HttpURLConnection httpURLConnection) throws Exception{
        if (header != null) {
            for (Map.Entry<String, String> head : header.entrySet()) {
                httpURLConnection.setRequestProperty(head.getKey(), head.getValue());
            }
        }
        httpURLConnection.setRequestProperty("Content-Type",contentType);
    }

    private void writePayload(HttpURLConnection httpURLConnection) throws Exception {
        if (body == null) throw new Exception("Cannot write empty body !");

        if (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            httpURLConnection.getOutputStream().write((byte[]) body);
        }else if (contentType.equals(MediaType.APPLICATION_JSON_VALUE)){
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStreamWriter.write((String) body);
            outputStreamWriter.flush();
        }else{
            throw new Exception("Unsupport contentType "+contentType);
        }
    }

    public byte[] buildFormData(Map<String, String> params) {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(encodeParam(param.getKey()));
            postData.append('=');
            postData.append(encodeParam(String.valueOf(param.getValue())));
        }
        return postData.toString().getBytes(StandardCharsets.UTF_8);
    }

    public  String encodeParam(String data) {
        String result = "";
        try {
            result = URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getResponse(HttpURLConnection httpURLConnection) throws Exception{
        int responseCode = httpURLConnection.getResponseCode();
        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }else {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line).append("\n");
            }
            bufferedReader.close();
            throw new RuntimeException("(" + httpURLConnection.getResponseCode() + ") :"+response.toString());
        }
    }

    public String sendGet() throws Exception{

        URL url = buildURL();
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        SSLContext sc = SSLContext.getInstance("TLSv1.2"); //$NON-NLS-1$
        sc.init(null, null, new java.security.SecureRandom());
        if (httpURLConnection instanceof HttpsURLConnection) {
            ( (HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sc.getSocketFactory());
        }
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod(HttpMethod.GET.name());
        httpURLConnection.setConnectTimeout(DEFAULT_TIMEOUT);
        httpURLConnection.setReadTimeout(DEFAULT_TIMEOUT);
        this.prepareHeader(httpURLConnection);
        return this.getResponse(httpURLConnection);
    }

    public String sendPost() throws Exception{

        URL url = buildURL();
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        SSLContext sc = SSLContext.getInstance("TLSv1.2"); //$NON-NLS-1$
        sc.init(null, null, new java.security.SecureRandom());
        if (httpURLConnection instanceof HttpsURLConnection) {
            ( (HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sc.getSocketFactory());
        }
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod(HttpMethod.POST.name());
        httpURLConnection.setConnectTimeout(DEFAULT_TIMEOUT);
        httpURLConnection.setReadTimeout(DEFAULT_TIMEOUT);
        this.prepareHeader(httpURLConnection);
        this.writePayload(httpURLConnection);
        return this.getResponse(httpURLConnection);
    }


    public static void trustAllHosts(){
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509ExtendedTrustManager()
                    {
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers()
                        {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
                        {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
                        {

                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException
                        {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException
                        {

                        }

                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new  HostnameVerifier()
            {
                @Override
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
