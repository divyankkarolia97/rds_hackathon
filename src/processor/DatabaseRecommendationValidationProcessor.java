package processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.emory.mathcs.backport.java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import model.Engine;
import model.InputRequest;
import model.response.DialogAction;
import model.response.Intent;
import model.response.Response;

public class DatabaseRecommendationValidationProcessor implements RequestHandler<Map<String, Object>, Object> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object handleRequest(Map<String, Object> input, Context context) {

        try {
            Map<String, Object> currentIntent = (Map<String, Object>) input.get("currentIntent");
            Map<String, Object> slotValues = (Map<String, Object>) currentIntent.get("slots");
            InputRequest inputRequest = objectMapper.convertValue(slotValues, InputRequest.class);

            validateInput(inputRequest);

            List<Intent> recentIntentSummaryView = new ArrayList<>();

            if(!Objects.isNull(inputRequest.getEngine()) && inputRequest.getEngine().equals(Engine.AURORA)) {
                Intent intent = new Intent("engine_version");
                recentIntentSummaryView.add(intent);
            }


            return new Response(null, recentIntentSummaryView , new DialogAction("true", "fulfilled", "good to go."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    boolean validateInput(InputRequest inputRequest) {

        // all the validations go here
        if (Engine.values().inputRequest.getEngine())

    }

}
