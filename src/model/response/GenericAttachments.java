package model.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericAttachments {
    String title;
    String subTitle;
    String imageUrl;
    String attachmentLinkUrl;
    List<Map<String, String>> buttons;
}
