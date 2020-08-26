package model;

import java.util.List;

public enum InstanceType {

    DB_R4_LARGE(50, .5),
    DB_R4_XLARGE(100, 1.0),
    DB_R4_2XLARGE(200, 2.0),
    DB_R4_4XLARGE(400, 4.0),
    DB_R4_8XLARGE(800, 8.0),
    DB_R4_16XLARGE(1600, 16.0),

    DB_T2_SMALL(5, .05),
    DB_T2_MEDIUM(10, .100),
    DB_T3_SMALL(15, .150),
    DB_T3_MEDIUM(20, .200);

    private Integer iops;
    private Double hourlyOnDemandPrice;

    InstanceType(Integer iops, Double hourlyOnDemandPrice) {
        this.iops = iops;
        this.hourlyOnDemandPrice = hourlyOnDemandPrice;
    }

    public static InstanceType getInstanceTypeAccordingToIOPS(Integer iops) {
        for(InstanceType instanceType: InstanceType.values()){
            if(instanceType.iops > iops) {
                return instanceType;
            }
        }
        return null;
    }

}
