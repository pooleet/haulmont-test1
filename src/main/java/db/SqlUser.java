package db;

import com.haulmont.model.Role;
import com.haulmont.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class SqlUser {

    private String error = "Ошибка sql SqlUser ";
    private String sql = "";
    private Connection conn = null;

    public ArrayList<User> loadUserList() {

        ArrayList<User> uList = new ArrayList<User>();
        sql = " select id, SURNAME, NAME,FNAME, ROLE from USER\n" +
                "order by role,SURNAME ";

        try {
            conn = ConnectSql.getMySQLConnection();
            Statement stmnt = conn.createStatement();
            ResultSet res = stmnt.executeQuery(sql);
            while (res.next()) {

                User usr = new User();
                usr.setId(res.getLong("ID"));
                usr.setFirstName(res.getString("NAME"));
                usr.setLastName(res.getString("SURNAME"));
                usr.setFatherName(res.getString("FNAME"));
                usr.setRole(Role.valueOf(res.getString("ROLE")));

                System.out.println(usr.getFirstName());
                uList.add(usr);


            }
            conn.close();
            return uList;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(error + "\n" + sql + "      ");
            return null;
        }


    }
}
