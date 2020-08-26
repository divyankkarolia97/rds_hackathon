package processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Objects;
import model.Engine;
import model.InputRequest;
import model.response.DialogAction;
import model.response.Response;

public class DatabaseRecommendationValidationProcessor implements RequestHandler<Map<String, Object>, Object> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object handleRequest(Map<String, Object> input, Context context) {

        try {
            Map<String, Object> currentIntent = (Map<String, Object>) input.get("currentIntent");
            Map<String, Object> slotValues = (Map<String, Object>) currentIntent.get("slots");
            InputRequest inputRequest = objectMapper.convertValue(slotValues, InputRequest.class);

            return validateInput(inputRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    Response validateInput(InputRequest inputRequest) {

        // all the validations go here

        Boolean flagForEngineMatch = false;
        for (Engine engine : Engine.values()) {
            if (engine.getName().equals(inputRequest.getEngineType())) {
                flagForEngineMatch = true;
            }
        }

        if (!flagForEngineMatch) {
            return new Response(objectMapper.convertValue(inputRequest, Map.class), new DialogAction("false", "fulfilled", null, null));
        }

        if (!Objects.isNull(inputRequest.getEngineType()) && inputRequest.getEngineType().equals(Engine.AURORA)) {

        }

        return new Response(objectMapper.convertValue(inputRequest, Map.class), new DialogAction("ElicitSlot", null, null, "engine-version"));


    }

}
