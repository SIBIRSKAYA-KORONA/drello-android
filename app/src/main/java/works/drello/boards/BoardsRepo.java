package works.drello.boards;

import android.util.Log;
import android.content.Context;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import works.drello.common.ApplicationModified;
import works.drello.network.ApiRepo;
import works.drello.network.BoardsApi;

@SuppressWarnings("WeakerAccess")
public class BoardsRepo {

    private final MutableLiveData<BoardsData> mData = new MutableLiveData<>(new BoardsData(BoardsData.Progress.NONE));
    private final ApiRepo mApiRepo;

    public BoardsRepo(ApiRepo apiRepo) {
        mApiRepo = apiRepo;
    }

    @NonNull
    public static BoardsRepo getInstance(Context context) {
        return ApplicationModified.from(context).getBoardsRepo();
    }

    public LiveData<BoardsData> create() {
        BoardsData data = mData.getValue();
        if (data.getProgress() == BoardsData.Progress.IN_PROGRESS) {
            return mData;
        }
        data.setProgress(BoardsData.Progress.IN_PROGRESS);
        data.setBoards(null);
        mData.setValue(data);

        mApiRepo.getBoardsApi()
                .create()
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call,
                                           @NotNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            data.setProgress(BoardsData.Progress.SUCCESS);
                        } else {
                            Log.w("boards onResponse", response.toString());
                            if (response.code() == 401) {
                                data.setProgress(BoardsData.Progress.INVALID_SESSION_ERROR);

                            } else {
                                data.setProgress(BoardsData.Progress.INTERNAL_ERROR);
                            }
                        }
                        mData.postValue(data);
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.w("boards onFailure", t.getMessage());
                        data.setProgress(BoardsData.Progress.INTERNAL_ERROR);
                        mData.postValue(data);
                    }
                });

        return mData;
    }

    public LiveData<BoardsData> get() {
        BoardsData data = mData.getValue();
        if (data.getProgress() == BoardsData.Progress.IN_PROGRESS) {
            return mData;
        }
        data.setProgress(BoardsData.Progress.IN_PROGRESS);
        data.setBoards(null);
        mData.setValue(data);

        mApiRepo.getBoardsApi()
                .get()
                .enqueue(new Callback<List<BoardsApi.Boards>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<BoardsApi.Boards>> call,
                                           @NotNull Response<List<BoardsApi.Boards>> response) {
                        if (response.isSuccessful()) {
                            List<BoardsApi.Boards> boards = response.body();
                            data.setProgress(BoardsData.Progress.SUCCESS);
                            data.setBoards(boards);
                        } else {
                            Log.w("boards onResponse", response.toString());
                            if (response.code() == 401) {
                                data.setProgress(BoardsData.Progress.INVALID_SESSION_ERROR);

                            } else {
                                data.setProgress(BoardsData.Progress.INTERNAL_ERROR);
                            }
                        }
                        mData.postValue(data);
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<BoardsApi.Boards>> call, @NotNull Throwable t) {
                        Log.w("boards onFailure", t.getMessage());
                        data.setProgress(BoardsData.Progress.INTERNAL_ERROR);
                        mData.postValue(data);
                    }
                });

        return mData;
    }

}
