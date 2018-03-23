package leduyhung.me.animemoment.ui.login;

public class MessageForLoginActivity {

    public final static int CODE_FACEBOOK_BUTTON_CLICK = 101;
    public final static int CODE_DEMO_BUTTON_CLICK = 102;
    public final static int CODE_LOGIN_FACEBOOK_SUCCESS = 103;
    public final static int CODE_LOGIN_FACEBOOK_CANCEL = 104;
    public final static int CODE_LOGIN_FACEBOOK_FAIL = 105;

    private int code;
    private String message;
    private int idCategory;

    public MessageForLoginActivity(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public MessageForLoginActivity(int code, String message, int idCategory) {
        this.code = code;
        this.message = message;
        this.idCategory = idCategory;
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

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }
}