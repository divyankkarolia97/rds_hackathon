package model;

public enum Engine {
    AURORA("aurora"),

    MYSQL("mysql"),
    POSTGRESQL("postgesql"),
    MARIA_DB("mariadb"),
    SQL_SERVER("sqlserver");

    String name;

    Engine(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
