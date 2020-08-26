package model.response;

import lombok.Data;
import model.Engine;
import model.InstanceType;

@Data
@Deprecated
public class Message {
    Engine engine;
    InstanceType instanceType;
    Double estimatedMonthlyCost;
    String costBreakdown;
    String comment; // Mentioning that the above price is for default configuration, and the price will increase with addition of more resources eg. additional reader or enabling backtrack.
    String productUrl; // Head over to the following URL to explore more.
}
