package es.tetexe.flickrbrowser;

/**
 * Created by Alejandro on 21/08/2015.
 */

//Lista de estados de descarga

enum DownloadStatus {

    IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK
}


public class GetRawData {

    private String LOG_TAG = GetRawData.class.getSimpleName();
    private String mRawUrl;
    private String mData;
    private DownloadStatus mDownloadStatus;


    public GetRawData(String mRawUrl) {
        this.mRawUrl = mRawUrl;
    }
}
