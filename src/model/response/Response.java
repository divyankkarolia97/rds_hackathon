package model.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {

    Map<String, Object> sessionAttributes;
    DialogAction dialogAction;
}
