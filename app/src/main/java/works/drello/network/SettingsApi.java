package works.drello.network;

import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PartMap;
import retrofit2.http.Body;

public interface SettingsApi {

    class User {
        public int id;
        public String name;
        public String surname;
        public String nickname;
        public String email;
        public String avatar;
    }

    String LOCAL_PATH = "settings";

    @Headers("Content-Type: application/json")
    @POST(LOCAL_PATH)
    Call<Void> create(@Body JsonObject body);

    @GET(LOCAL_PATH)
    Call<User> get();

    @Multipart
    @PUT(LOCAL_PATH)
    Call<Void> update(@PartMap Map<String, RequestBody> body);
}
