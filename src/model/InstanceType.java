package model;

import java.util.List;

public enum InstanceType {

    DB_T2_SMALL("db.t2.small",5, .05),
    DB_T2_MEDIUM("db.t2.medium",10, .100),
    DB_T3_SMALL("db.t3.small",15, .150),
    DB_T3_MEDIUM("db.t3.medium",20, .200),

    DB_R4_LARGE("db.r4.large",50, .5),
    DB_R4_XLARGE("db.r4.xlarge",100, 1.0),
    DB_R4_2XLARGE("db.r4.2xlarge",200, 2.0),
    DB_R4_4XLARGE("db.r4.4xlarge",400, 4.0),
    DB_R4_8XLARGE("db.r4.8xlarge",800, 8.0),
    DB_R4_16XLARGE("db.r4.16xlarge",1600, 16.0);

    private String name;
    private Integer iops;
    private Double hourlyOnDemandPrice;

    InstanceType(String name, Integer iops, Double hourlyOnDemandPrice) {
        this.name = name;
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

    public String getName(){ return name;}

}
