package com.octo.greenchallenge.collect.uat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoreChallengeApi {

    private String challengerID;
    private String cpuCycles;
    private String source;

    public ScoreChallengeApi() {

    }
    
    public void setChallengerID(String challengerID) {
        this.challengerID = challengerID;
    }

    public void setCpuCycles(String cpuCycles) {
        this.cpuCycles = cpuCycles;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int httpcode() throws IOException {
        return callAPI().getStatusLine().getStatusCode();
    }

    public String response() throws IOException {
        HttpEntity ent = callAPI().getEntity();
        return EntityUtils.toString(ent);
    }

    private HttpResponse callAPI() throws IOException {
        HttpClient c = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("challengerID",challengerID));
        params.add(new BasicNameValuePair("CPUCycles",cpuCycles));
        params.add(new BasicNameValuePair("source",source));
        HttpPost post = new HttpPost("http://localhost:8080/api/collect/cpu");
        post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
        return c.execute(post);
    }
}
