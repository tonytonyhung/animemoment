package leduyhung.me.animemoment.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.widget.ImageView;

import com.leduyhung.loglibrary.Logg;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.ui.character.detail.gallery.MessageForGalleyCharacterFragment;

public class ImageUtil {

    public static final String LINK_LOCAL_IMAGE = Environment.getExternalStorageDirectory() + File.separator + "anime_world/image/";

    private static ImageUtil imageUtil;
    private Picasso picasso;
    private Context mContext;

    public static ImageUtil newInstance() {

        if (imageUtil == null) {

            imageUtil = new ImageUtil();
        }

        return imageUtil;
    }

    private void createFolderImage() {

        File folder = new File(LINK_LOCAL_IMAGE);
        if (!folder.exists())
            folder.mkdirs();
    }

    public ImageUtil addContext(Context mContext) {

        this.mContext = mContext;
        picasso = Picasso.with(mContext);
        picasso.setLoggingEnabled(true);
        return imageUtil;
    }

    public void showImageFromInternet(String url, ImageView img, int width, int height, Callback callback) {
        if (url != null && url != "") {

            picasso.load(url).resize(width, height).centerInside().placeholder(mContext.getResources()
                    .getDrawable(R.drawable.img_wait_image)).error(mContext.getResources()
                    .getDrawable(R.drawable.img_no_image)).into(img, callback);
        } else {

            Logg.error(getClass(), "showImageFromInternet: url is null");
        }
    }

    public void saveImage(final Bitmap bm, final Context mContext) {

        createFolderImage();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                FileOutputStream fileOutputStream;
                File f;
                Date d;

                try {

                    Looper.prepare();
                    d = Calendar.getInstance().getTime();
                    f = new File(LINK_LOCAL_IMAGE + d.getTime() + ".png");
                    fileOutputStream = new FileOutputStream(f);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(f));
                    mContext.sendBroadcast(intent);
                    EventBus.getDefault().post(new MessageForGalleyCharacterFragment(MessageForGalleyCharacterFragment.CODE_SAVE_IMG_SUCCESS, LINK_LOCAL_IMAGE));
                } catch (Throwable e) {

                    Logg.error(getClass(), "saveImage: " + e.toString());
                    EventBus.getDefault().post(new MessageForGalleyCharacterFragment(MessageForGalleyCharacterFragment.CODE_SAVE_IMG_FAIL, ""));
                }
            }
        });
        thread.start();
    }
}