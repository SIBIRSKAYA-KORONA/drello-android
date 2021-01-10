package works.drello.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TokenApi {
    String LOCAL_PATH = "token";

    @GET(LOCAL_PATH)
    Call<Void> get();
}