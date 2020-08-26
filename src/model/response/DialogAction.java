package model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.response.Message;

@Data
@AllArgsConstructor
public class DialogAction {
    String type;
    String fulfilementState;
    String message;
}
