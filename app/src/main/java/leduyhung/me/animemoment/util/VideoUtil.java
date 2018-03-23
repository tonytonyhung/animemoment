package leduyhung.me.animemoment.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import leduyhung.me.animemoment.ui.character.detail.video.MessageForVideoCharacterFragment;

public class VideoUtil {

    public static final String LINK_LOCAL_VIDEO = Environment.getExternalStorageDirectory() + File.separator + "anime_world/video/";

    public void download(Context mContext, String url) {

        createFolderVideo();
        DownloadVideo downloadVideo = new DownloadVideo(mContext);
        downloadVideo.execute(url);
    }

    private void createFolderVideo() {

        File folder = new File(LINK_LOCAL_VIDEO);
        if (!folder.exists())
            folder.mkdirs();
    }

    private class DownloadVideo extends AsyncTask<String, Integer, String> {

        private Context context;

        public DownloadVideo(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... link) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(link[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(LINK_LOCAL_VIDEO + "video.mkv");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            EventBus.getDefault().post(new MessageForVideoCharacterFragment(MessageForVideoCharacterFragment.CODE_DOWNLOAD_COMPLETE));
        }
    }
}
