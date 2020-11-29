package works.drello.network;

import com.github.cliftonlabs.json_simple.JsonObject;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Headers;
import retrofit2.http.Body;

public interface SessionApi {

    String LOCAL_PATH = "session";

    @Headers("Content-Type: application/json")
    @POST(LOCAL_PATH)
    Call<Void> create(@Body JsonObject body);

    @DELETE(LOCAL_PATH)
    Call<Void> delete();
}
