package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // connection 얻기
            con = getConnection();

            // db에 전달할 sql과 파라미터로 전달할 데이터를 담기위해 pstmt를 얻는다
            pstmt = con.prepareStatement(sql);

            // pstmt에 파라미터 바인딩
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());

            // 실행
            pstmt.executeUpdate();

            return member;
        } catch (SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally { // 반드시 pstmt와 con을 닫아줘야한다! 외부 TCP/IP를 사용하는거기때문에 꼭 닫아줘야한다. 사용한 역순으로 닫아주면 된다.
            close(con, pstmt, null);
        }

    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));

                return member;
            } else {
                throw new NoSuchElementException("member not found memberId = " + memberId);
            }

        } catch (SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }

    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money = ? where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();

            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("update resultSize = {}", resultSize);
        } catch (SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }


    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("delete resultSize = {}", resultSize);
        } catch (SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }

    }


    // pstmt, rs, con은 반드시 닫아줘야하는데, 닫는데도 exception이 발생할 수 있기때문에 닫는코드 하나하나를 try-catch로 감싸야한다.
    // 하나하나 감싸다보면 코드가 너무 길어지고 여러군데서 사용하기때문에 하나의 메소드로 만들어서 사용한다.
    // 닫을땐 반드시 역순으로 닫아줘야한다. rs -> stmt -> con
    private void close(Connection con, Statement stmt, ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("rs close ERROR", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("stmt close ERROR", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("con close ERROR", e);
            }
        }
    }

    private Connection getConnection() { // 다른곳에서도 계속 사용할거기때문에(save에서만 사용하는게 아니라서) 따로 메소드로 빼냈다 -> 단축키 : command + option + M
        return DBConnectionUtil.getConnection();
    }
}
