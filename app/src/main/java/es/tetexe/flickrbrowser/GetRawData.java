package es.tetexe.flickrbrowser;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alejandro on 21/08/2015.
 */

//Lista de estados de descarga

enum DownloadStatus {

    IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK
}

//Esta clase va a descargar la información, el JSON sin procesar (sin parsearlo)

public class GetRawData {

    private String LOG_TAG = GetRawData.class.getSimpleName();
    private String mRawUrl;
    private String mData;
    private DownloadStatus mDownloadStatus;


    public GetRawData(String mRawUrl) {
        this.mRawUrl = mRawUrl;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }


    public void reset() {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mRawUrl = null;
        this.mData = null;
    }

    public String getmData() {
        return mData;
    }

    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }


    public void execute() {

        this.mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(mRawUrl);

        //Introducido por mi para controlar el proceso del código
        String status = getmDownloadStatus().toString();
        Log.e("miSuperStatus", status);
        Log.e("miSuperStatus", "En proceso de descarga");

    }


    //Params    Progress   Result
    public class DownloadRawData extends AsyncTask<String, Void, String> {
        //RESULT
        protected void onPostExecute(String webData) {

            mData = webData;

            Log.v(LOG_TAG, "Data returnned: " + mData);

            if (mData == null) {
                if (mRawUrl == null) {
                    mDownloadStatus = DownloadStatus.NOT_INITIALISED;
                } else {
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
                // !Pero si mData no está vacío...¡ introducido por mí para ver
                //función del símbolo "!"
            } else if (!mData.isEmpty()) {
                //succes
                mDownloadStatus = DownloadStatus.OK;
                Log.d("miSuperStatus", "Descarga finalizada");
            }

            //Mostrando la longitud del JSON descargado
            int mDataLength;

            mDataLength = mData.length();

            Log.d("miSuperStatus", "Su tamaño es de: " + mDataLength + " caracteres");

        }

        //PARAMS
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            if (params == null)
                return null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                return buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }

                }
            }

        }

    }
}
