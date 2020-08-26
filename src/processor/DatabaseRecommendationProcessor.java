package processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Config;
import java.util.HashMap;
import java.util.Map;
import model.response.DialogAction;
import model.InstanceType;
import model.InputRequest;
import model.response.Response;
import org.apache.commons.lang3.tuple.Pair;

public  class DatabaseRecommendationProcessor implements RequestHandler <Map<String, Object>, Object> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object handleRequest(Map<String, Object> input, Context context) {

        try {
            Map<String, Object> currentIntent = (Map<String, Object>) input.get("currentIntent");
            Map<String, Object> slotValues = (Map<String, Object>) currentIntent.get("slots");
            InputRequest inputRequest = objectMapper.convertValue(slotValues, InputRequest.class);


            Map<String, Object> databaseRecommendation = getDatabaseRecommendation(inputRequest);

            Map<String, String> message = new HashMap<String, String>() {{
                put("content", String.format("Here is your recommended configuration: \n Engine: %s \n Instance Type: %s \n Estimated Monthly Cost: $%.2f \n CostBreakdown: %s \n ProductUrl: %s \n  If you want us to create the database, then please "
                        + "type “launch”. ",
                        databaseRecommendation.get("engine"),
                        databaseRecommendation.get("instanceType"),
                        databaseRecommendation.get("estimatedMonthlyCost"),
                        databaseRecommendation.get("costBreakdown"),
                        databaseRecommendation.get("productUrl")));
                put("contentType", "CustomPayload");

            }};

            return new Response(databaseRecommendation, new DialogAction("Close", "Fulfilled", message, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Map<String, Object> getDatabaseRecommendation(InputRequest inputRequest) {
        // here comes the actual processing logic - based on the input request find the database configuration supported
        if(inputRequest.getEngineType().equals("aurora")) {
            return getAuroraRecommendation(inputRequest);
        } else {
            return getRDSRecommendation(inputRequest);
        }
    }

    // Recommendation functions by Engine

    public Map<String, Object> getAuroraRecommendation(InputRequest inputRequest) {
        Map<String, Object> values = new HashMap<>();

        values.put("engine", inputRequest.getEngineType());
        values.put("instanceType", InstanceType.getInstanceTypeAccordingToIOPS(inputRequest.getIops()).getName());

        Pair<Double, String> costEstimation = getAuroraCostEstimation(inputRequest);

        values.put("estimatedMonthlyCost",costEstimation.getKey());
        values.put("costBreakdown", costEstimation.getValue());

        values.put("comment", Config.auroraComment);
        values.put("productUrl", Config.auroraProductURL);
        return values;
    }

    public Map<String, Object> getRDSRecommendation(InputRequest inputRequest) {
        Map<String, Object> values = new HashMap<>();

        values.put("engine", inputRequest.getEngineType());
        values.put("instanceType", InstanceType.getInstanceTypeAccordingToIOPS(inputRequest.getIops()).getName());

        Pair<Double, String> costEstimation = getCostEstimation(inputRequest);

        values.put("estimatedMonthlyCost",costEstimation.getKey());
        values.put("costBreakdown", costEstimation.getValue());

        values.put("comment", Config.comment);
        values.put("productUrl", Config.productPagesByEngine.get(inputRequest.getEngineType()));
        return values;
    }

    // Cost Estimation Functions

    public Pair<Double, String> getAuroraCostEstimation(InputRequest inputRequest) {

        Double estimatedMonthlyCost = 0.0;

        Double storageCost = inputRequest.getStorageInGb() * Config.auroraStoragePerGbCostPerMonth;
        Double backupCost = inputRequest.getBackUpRetentionPeriod() * inputRequest.getStorageInGb() * Config.auroraBackupStoragePerGbCostPerMonth;
        Double ioCost = (inputRequest.getIops()* Config.SECONDS_IN_A_MONTH * Config.auroraIOCostPerMonthPerMillion) / Config.MILLION;

        estimatedMonthlyCost = ioCost + storageCost + backupCost;

        String costBreakdown = String.format("IOCost: $%.2f, StorageCost: $%.2f, BackupCost: $%.2f", ioCost, storageCost, backupCost );

        return Pair.of(estimatedMonthlyCost, costBreakdown);
    }

    public Pair<Double, String> getCostEstimation(InputRequest inputRequest) {

        Double estimatedMonthlyCost = 0.0;

        Double storageCost = inputRequest.getStorageInGb() * Config.storagePerGbCostPerMonth;
        Double backupCost = inputRequest.getBackUpRetentionPeriod() * inputRequest.getStorageInGb() * Config.backupStoragePerGbCostPerMonth;

        estimatedMonthlyCost = storageCost + backupCost;

        String costBreakdown = String.format("StorageCost: $%.2f, BackupCost: $%.2f", storageCost, backupCost );

        return Pair.of(estimatedMonthlyCost, costBreakdown);
    }

}
