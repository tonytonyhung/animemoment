package leduyhung.me.animemoment.ui.character.list;

import leduyhung.me.animemoment.module.character.data.CharacterInfo;
import leduyhung.me.animemoment.module.character.data.CharacterResponse;

public class MessageForListCharacterFragment {

    public final static int CODE_LOAD_LIST_SUCCESS = 301;
    public final static int CODE_LOAD_LIST_FAIL = 302;
    public final static int CODE_SEARCH_LIST_SUCCESS = 303;
    public final static int CODE_SEARCH_LIST_FAIL = 304;

    private int code;
    private String message;
    private CharacterResponse data;
    private int categoryId;

    public MessageForListCharacterFragment(int code, String message, int categoryId) {
        this.code = code;
        this.message = message;
        this.categoryId = categoryId;
    }

    public MessageForListCharacterFragment(int code, CharacterResponse data, int categoryId) {
        this.code = code;
        this.data = data;
        this.categoryId = categoryId;
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

    public CharacterResponse getData() {
        return data;
    }

    public void setData(CharacterResponse data) {
        this.data = data;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}