package leduyhung.me.animemoment.ui.character.detail.gallery;

public class MessageForImageCharacterFragment {

    public final static int CODE_CLICK_DOWNLOAD_IMG = 700;

    private int code;
    private String url;

    public MessageForImageCharacterFragment(int code, String url) {
        this.code = code;
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }
}