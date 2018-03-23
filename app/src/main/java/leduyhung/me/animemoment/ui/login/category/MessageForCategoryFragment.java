package leduyhung.me.animemoment.ui.login.category;

import java.util.List;

import leduyhung.me.animemoment.module.category.data.CategoryInfo;

public class MessageForCategoryFragment {

    public final static int CODE_GET_LIST_CATEGORY_SUCCESS = 201;
    public final static int CODE_GET_LIST_CATEGORY_FAIL = 202;
    public final static int CODE_CLICK_ITEM_CATEGORY = 203;
    public final static int NEED_TO_UPDATE = 204;

    private int code;
    private String message;
    private List<CategoryInfo> data;
    private int id;
    private String name;
    private int type;

    public MessageForCategoryFragment(int code, int id, String name, int type) {
        this.code = code;
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public MessageForCategoryFragment(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public MessageForCategoryFragment(int code, String message, List<CategoryInfo> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<CategoryInfo> getData() {
        return data;
    }

    public void setData(List<CategoryInfo> data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }
}