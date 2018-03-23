package leduyhung.me.animemoment.ui.character.detail;

import leduyhung.me.animemoment.module.media.data.MediaInfo;
import leduyhung.me.animemoment.module.media.data.MediaResponse;

public class MessageForDetailCharacterFragment {

    public final static int CODE_LOAD_MEDIA_IMG_SUCCESS = 500;
    public final static int CODE_LOAD_MEDIA_CLIP_SUCCESS = 501;
    public final static int CODE_LOAD_MEDIA_IMG_FAIL = 502;
    public final static int CODE_LOAD_MEDIA_CLIP_FAIL = 503;
    public final static int CODE_CLICK_GALLERY = 504;

    private int code;
    private String message;
    private MediaResponse data;
    private MediaInfo dataInfo;

    public MessageForDetailCharacterFragment(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public MessageForDetailCharacterFragment(int code, MediaResponse data) {
        this.code = code;
        this.data = data;
    }

    public MessageForDetailCharacterFragment(int code, MediaInfo dataInfo) {
        this.code = code;
        this.dataInfo = dataInfo;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MediaResponse getData() {
        return data;
    }

    public void setData(MediaResponse data) {
        this.data = data;
    }

    public MediaInfo getDataInfo() {
        return dataInfo;
    }

    public void setDataInfo(MediaInfo dataInfo) {
        this.dataInfo = dataInfo;
    }
}
