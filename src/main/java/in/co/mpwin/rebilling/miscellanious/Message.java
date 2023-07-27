package in.co.mpwin.rebilling.miscellanious;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter @Setter
public class Message {

    private String message;

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }


}
