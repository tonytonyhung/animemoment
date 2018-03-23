package leduyhung.me.animemoment.ui.character;

import android.widget.ImageView;

import java.util.List;

import leduyhung.me.animemoment.module.character.data.CharacterInfo;
import leduyhung.me.animemoment.module.media.data.MediaInfo;

public class MessageForCharacterActivity {

    public final static int CODE_OPEN_CHARACTER_DETAIL = 400;
    public final static int CODE_OPEN_GALLERY = 401;
    public final static int CODE_OPEN_CLIP = 402;
    public final static int CODE_CLICK_BACK_CHARACTER_DETAIL = 403;


    private int code;
    private int characterId;
    private CharacterInfo dataDetail;
    private MediaInfo mediaInfo;
    private int position;

    public MessageForCharacterActivity(int code) {
        this.code = code;
    }

    public MessageForCharacterActivity(int code, CharacterInfo dataDetail) {
        this.code = code;
        this.dataDetail = dataDetail;
    }

    public MessageForCharacterActivity(int code, int characterId, int position) {
        this.code = code;
        this.characterId = characterId;
        this.position = position;
    }

    public MessageForCharacterActivity(int code, MediaInfo mediaInfo) {
        this.code = code;
        this.mediaInfo = mediaInfo;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CharacterInfo getDataDetail() {
        return dataDetail;
    }

    public int getCharacterId() {
        return characterId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public MediaInfo getMediaInfo() {
        return mediaInfo;
    }
}