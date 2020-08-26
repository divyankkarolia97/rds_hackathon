package processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Config;
import java.util.HashMap;
import java.util.Map;
import model.response.DialogAction;
import model.InstanceType;
import model.response.Message;
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
            return new Response(getDatabaseRecommendation(inputRequest), null, new DialogAction("true", "fulfilled", "good to go."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Map<String, Object> getDatabaseRecommendation(InputRequest inputRequest) {
        // here comes the actual processing logic - based on the input request find the database configuration supported
        switch (inputRequest.getEngine()) {
            case AURORA_MYSQL:
            case AURORA_POSTGRESQL:
                return getAuroraRecommendation(inputRequest);
            case MYSQL:
            case POSTGRESQL:
            case MARIA_DB:
            case SQL_SERVER:
                return getRDSRecommendation(inputRequest);
        }
        return null;
    }

    // Recommendation functions by Engine

    public Map<String, Object> getAuroraRecommendation(InputRequest inputRequest) {
        Message responseMessage = new Message();
        Map<String, Object> values = new HashMap<>();

        values.put("engine", inputRequest.getEngine().name());
        values.put("instance_type", InstanceType.getInstanceTypeAccordingToIOPS(inputRequest.getIops()));

        Pair<Double, String> costEstimation = getAuroraCostEstimation(inputRequest, responseMessage);

        values.put("estimated_monthly_cost",costEstimation.getKey());
        values.put("cost_breakdown", costEstimation.getValue());

        values.put("comment", Config.auroraComment);
        values.put("product_url", Config.auroraProductURL);
        return values;
    }

    public Map<String, Object> getRDSRecommendation(InputRequest inputRequest) {
        Message responseMessage = new Message();
        Map<String, Object> values = new HashMap<>();

        values.put("engine", inputRequest.getEngine().name());
        values.put("instance_type", InstanceType.getInstanceTypeAccordingToIOPS(inputRequest.getIops()));

        Pair<Double, String> costEstimation = getCostEstimation(inputRequest, responseMessage);

        values.put("estimated_monthly_cost",costEstimation.getKey());
        values.put("cost_breakdown", costEstimation.getValue());

        values.put("comment", Config.comment);
        values.put("product_url", Config.productPagesByEngine.get(inputRequest.getEngine()));
        return values;
    }

    // Cost Estimation Functions

    public Pair<Double, String> getAuroraCostEstimation(InputRequest inputRequest, Message message) {

        Double estimatedMonthlyCost = 0.0;

        Double storageCost = inputRequest.getStorageInGb() * Config.auroraStoragePerGbCostPerMonth;
        Double backupCost = inputRequest.getBackUpRetentionPeriod() * inputRequest.getStorageInGb() * Config.auroraBackupStoragePerGbCostPerMonth;
        Double ioCost = (inputRequest.getIops()* Config.SECONDS_IN_A_MONTH * Config.auroraIOCostPerMonthPerMillion) / Config.MILLION;

        estimatedMonthlyCost = ioCost + storageCost + backupCost;

        String costBreakdown = String.format("IOCost: $%f, StorageCost: $%f, BackupCost: $%f", ioCost, storageCost, backupCost );

        return Pair.of(estimatedMonthlyCost, costBreakdown);
    }

    public Pair<Double, String> getCostEstimation(InputRequest inputRequest, Message message) {

        Double estimatedMonthlyCost = 0.0;

        Double storageCost = inputRequest.getStorageInGb() * Config.storagePerGbCostPerMonth;
        Double backupCost = inputRequest.getBackUpRetentionPeriod() * inputRequest.getStorageInGb() * Config.backupStoragePerGbCostPerMonth;

        estimatedMonthlyCost = storageCost + backupCost;

        String costBreakdown = String.format("StorageCost: $%f, BackupCost: $%f", storageCost, backupCost );

        return Pair.of(estimatedMonthlyCost, costBreakdown);
    }

}
