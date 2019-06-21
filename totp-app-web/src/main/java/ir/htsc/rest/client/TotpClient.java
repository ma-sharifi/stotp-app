package ir.htsc.rest.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by MSH on 5/27/2019.
 */
public class TotpClient {
    private OkHttpClient client = new OkHttpClient();
    private String cardNo="6037701012345678";
    private String mobileNo="989133480144";

    private String rootPath="http://localhost:7001/v1/api/totp";


    public String register() throws IOException {
        String url= rootPath+"/register/mobiles/"+mobileNo+"/cards/"+cardNo;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public String generate() throws IOException {
        String url=rootPath+"/generate/mobiles/"+mobileNo+"/cards/"+cardNo;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public String validateWithUuid(String uuid,String totp) throws IOException {
        String url=rootPath+"/validate/uuid/"+uuid;
        Request request = new Request.Builder()
                .addHeader("Totp",totp)
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String validate(String totp) throws IOException {
        String url=rootPath+"/validate/mobiles/"+mobileNo+"/cards/"+cardNo;
        Request request = new Request.Builder()
                .addHeader("Totp",totp)
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}
