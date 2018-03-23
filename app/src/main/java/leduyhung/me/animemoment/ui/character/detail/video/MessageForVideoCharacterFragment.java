package leduyhung.me.animemoment.ui.character.detail.video;

public class MessageForVideoCharacterFragment {

    public static final int CODE_DOWNLOAD_COMPLETE = 701;

    private int code;

    public MessageForVideoCharacterFragment(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
