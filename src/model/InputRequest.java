package model;

import lombok.Data;

@Data
public class InputRequest {

    String engineType;
    String auroraEdition;
    Integer iops;
    Integer storageInGb;
    Integer backUpRetentionPeriod;

}