package leduyhung.me.animemoment.ui.character.detail.gallery;

public class MessageForGalleyCharacterFragment {

    public final static int CODE_SAVE_IMG_SUCCESS = 600;
    public final static int CODE_SAVE_IMG_FAIL = 601;

    private int code;
    private String message;

    public MessageForGalleyCharacterFragment(int code, String message) {
        this.code = code;
        this.message = message;
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
}