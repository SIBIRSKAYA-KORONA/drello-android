package works.drello.boards;

import java.util.List;

import works.drello.network.BoardsApi.Boards;

public class BoardsData {

    enum Progress {
        NONE,
        IN_PROGRESS,
        SUCCESS,
        INVALID_SESSION_ERROR,
        INTERNAL_ERROR
    }

    private Progress mProgress;
    private List<Boards> mBoards = null;

    public BoardsData(Progress progress) {
        this.mProgress = progress;
    }

    public Progress getProgress() {
        return mProgress;
    }

    public List<Boards> getBoards() {
        return mBoards;
    }

    public void setProgress(Progress progress) {
        this.mProgress = progress;
    }

    public void setBoards(List<Boards> boards) {
        this.mBoards = boards;
    }
}
