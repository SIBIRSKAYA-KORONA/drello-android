package works.drello.boards;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import works.drello.network.BoardsApi.Boards;

@SuppressWarnings("WeakerAccess")
public class BoardsViewModel extends AndroidViewModel {

    private final MediatorLiveData<BoardsData> mState = new MediatorLiveData<>();

    public BoardsViewModel(@NonNull Application application) {
        super(application);
        mState.setValue(new BoardsData(BoardsData.Progress.NONE));
    }

    public LiveData<BoardsData> getProgress() {
        return mState;
    }

    public void createBoard() {
        if (mState.getValue().getProgress() == BoardsData.Progress.IN_PROGRESS) {
            return;
        }

        BoardsData boardsData = mState.getValue();
        boardsData.setProgress(BoardsData.Progress.IN_PROGRESS);
        mState.postValue(boardsData);

        final LiveData<BoardsData> progressLiveData = BoardsRepo.getInstance(getApplication()).create();

        mState.addSource(progressLiveData, data -> {
            if (data.getProgress() == BoardsData.Progress.IN_PROGRESS) {
                return;
            }
            mState.postValue(data);
            mState.removeSource(progressLiveData);
        });
    }

    public void loadBoards() {
        if (mState.getValue().getProgress() == BoardsData.Progress.IN_PROGRESS) {
            return;
        }

        BoardsData boardsData = mState.getValue();
        boardsData.setProgress(BoardsData.Progress.IN_PROGRESS);
        mState.postValue(boardsData);

        final LiveData<BoardsData> progressLiveData = BoardsRepo.getInstance(getApplication()).get();

        mState.addSource(progressLiveData, data -> {
            if (data.getProgress() == BoardsData.Progress.IN_PROGRESS) {
                return;
            }
            mState.postValue(data);
            mState.removeSource(progressLiveData);
        });
    }
}
