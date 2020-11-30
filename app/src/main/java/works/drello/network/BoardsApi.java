package works.drello.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BoardsApi {

    class Board {
        public int id;
        public String title;
        public List<SettingsApi.User> members;
        public List<SettingsApi.User> admins;
    }

    class Boards {
        List<Board> member;
        List<Board> admins;
    }

    String LOCAL_PATH = "boards";

    @POST(LOCAL_PATH)
    Call<Void> create();

    @GET(LOCAL_PATH)
    Call<List<Boards>> get();

}
