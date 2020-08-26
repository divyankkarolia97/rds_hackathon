package model;

import lombok.Data;

@Data
public class InputRequest {
    Engine engine;
    Integer iops;
    Integer storageInGb;
    Integer backUpRetentionPeriod;

}
