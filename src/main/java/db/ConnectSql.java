package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectSql {

   public ConnectSql (){}

   public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ConnectSql test = new ConnectSql();
        test.getMySQLConnection();
      /*  if (!test.loadDriver()) return;
        if (!test.getConnection()) return;

        test.createTable();
        test.fillTable();
        test.printTable();
        test.closeConnection();*/
    }

    // Connect to MySQL
    public static Connection getMySQLConnection() throws SQLException,
            ClassNotFoundException {
       //"~/IdeaProjects/Haulmont Test Task/src/main/java/db/"
        String hostName = "./src/main/java/db/file/";//"localhost";
        String dbName = "CarRepairShop";
        String userName = "root";
        String password = "admin";

        return getMySQLConnection(hostName, dbName, userName, password);
    }

    public static Connection getMySQLConnection(String hostName, String dbName,
                                                String userName, String password)   {


        String connectionURL = "jdbc:hsqldb:file:"+ hostName  + dbName;


        Connection conn = null;
        try {
            //
            //  Driver driver = new com.mysql.cj.jdbc.Driver();
            Class.forName("org.hsqldb.jdbc.JDBCDriver");//Проверяем наличие JDBC драйвера для работы с БД
            conn = DriverManager.getConnection(connectionURL, userName,  password);//соединениесБД
         //  System.out.println("Соединение с СУБД "+dbName+" выполнено.");

            // conn.close();       // отключение от БД
            //  System.out.println("Отключение от СУБД выполнено.");


        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println(e + "Подключение к БД:  JDBC драйвер для СУБД не найден!  " );
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Подключение к БД:  Ошибка SQL !"+e );
        }
        return conn;
    }

}
