package in.co.mpwin.rebilling.miscellanious;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter @Setter
public class Message {

    private String infoMessage;
    private String errorMessage;
    private String warningMessage;
    //private HttpStatus httpStatus;

    public Message() {
    }

    public Message(String infoMessage, String errorMessage, String warningMessage) {
        this.infoMessage = infoMessage;
        this.errorMessage = errorMessage;
        this.warningMessage = warningMessage;
    }


}
