package model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseCard {
    Integer version;
    String contentType;
    List<GenericAttachments> genericAttachments;
}
