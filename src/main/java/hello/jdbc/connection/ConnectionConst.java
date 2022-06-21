package hello.jdbc.connection;

public abstract class ConnectionConst { // 상수설정이기때문에 객체생성이 안되도록 abstract로 처리했다.

    public static final String URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}
