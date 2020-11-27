package works.drello.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Headers;
import retrofit2.http.Body;

public interface SessionApi {

    class UserPlain {
        public String name;
        public String password;
    }

    @Headers("Content-Type: application/json")
    @POST("session")
    Call<List<UserPlain>> create(@Body String body);
}
