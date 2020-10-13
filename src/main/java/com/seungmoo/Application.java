package com.seungmoo;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 스프링 데이터 JPA 첫번째 프로젝트
 * - JDBC 샘플 프로젝트, POSTGRESQL을 Docker를 통해 셋팅하고 JDBC로 연동해보자
 */
public class Application {

    public static void main(String[] args) throws SQLException {
        // docker 서버에서 5432포트에 postgresql 포워딩되도록 설정해놨음
        String url = "jdbc:postgresql://192.168.99.100:5432/springdata";
        String username = "seungmoo";
        String password = "pass";

        // Connection과 Pstmt는 Try-with 구문으로 만들어 주는 게 close 처리하기에 좋다.
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection created : " + connection);
            // 1. 테이블 생성
            //String sql = "CREATE TABLE ACCOUNT(id int, username varchar(255), password varchar(255))";
            // 2. 데이터 insert
            String sql = "INSERT INTO ACCOUNT VALUES(1, 'seungmoo', 'pass')";
            /**
             * <Spring JPA를 써야되는 이유>
             *
             * 이렇게 SQL로 하게 되면, 테이블 생성, 매핑 (resultMap -> 타입 변환하기 등), Connection 생성 비용
             * 등등이 비싸다.
             *
             * Connection의 경우는 Connection Pool을 만들어서 사용한다. 스프링 부트는 HikariPool을 사용한다.
             * Pool을 미리 만들어 놓음으로써 Connection 생성 비용 절감
             *
             * 또한 Mysql --> PostgreSql로 바꾸게 될 경우!! --> 이렇게 일일이 SQL과 코드로 짜면 매우 힘들다.
             *
             * try-with-resource 구문 덕분에 좀 편해지긴 했지만, 그래도 반복적인 코드가 생성된다.
             *
             * JPA에서 성능 최적화 코딩이 좀 더 수월하다고 함.(Lazy Coding)
             */
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.execute();
            }
        }
    }
}
