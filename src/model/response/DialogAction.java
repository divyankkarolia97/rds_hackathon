package model.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DialogAction {

    String type;
    String fulfillmentState;
    Map<String, String> message;
    String slotToElicit;
    ResponseCard responseCard;
}
