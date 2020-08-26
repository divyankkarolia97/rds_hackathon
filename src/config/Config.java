package config;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import model.Engine;

public class Config {

    public static Integer HOURS_IN_A_MONTH = 720;
    public static Integer SECONDS_IN_A_MONTH = 2592000;
    public static Integer MILLION = 1000000;

    // Aurora
    public static Double auroraStoragePerGbCostPerMonth = 0.10;
    public static Double auroraIOCostPerMonthPerMillion = 0.20;
    public static Double auroraBackupStoragePerGbCostPerMonth = 0.021;
    public static String auroraProductURL = "https://aws.amazon.com/rds/aurora/";
    public static String auroraComment = "Aurora offers additional features like global replication, multi master and backtrack, which increase your monthly cost.";

    // Common configs
    public static Double storagePerGbCostPerMonth = 0.115;
    public static Double backupStoragePerGbCostPerMonth = 0.095;
    public static String comment = "Head over to the porduct page to learn more.";

    public static Map<Engine, String> productPagesByEngine = new HashMap<Engine, String>() {{
        put(Engine.MYSQL, "https://aws.amazon.com/rds/mysql/");
        put(Engine.POSTGRESQL, "https://aws.amazon.com/rds/postgresql/");
        put(Engine.MARIA_DB, "https://aws.amazon.com/rds/mariadb/");
        put(Engine.SQL_SERVER, "https://aws.amazon.com/rds/sqlserver/");
    }};
}
