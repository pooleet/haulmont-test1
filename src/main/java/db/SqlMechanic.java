package db;


import com.haulmont.model.Mechanic;
import com.haulmont.model.Role;
import com.haulmont.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlMechanic {
    private String sql = "";
    private Connection conn = null;
    public SqlMechanic() {
    }

    public ArrayList<Mechanic> loadMechanicList() throws SQLException {
        ArrayList<Mechanic> mList = new ArrayList<Mechanic>();

        sql = "select a1.ID, a1.NAME, a1.SURNAME,a1.FNAME, a1.ROLE, a2.HPRICE, " +
                "case when b1.IDO is null then 0 else b1.IDO end IDO \n" +
                "from user a1 \n" +
                "    inner join MECHANIC a2 on a1.ID=a2.IDM \n" +
                "    left join (select IDM, count(ido) ido from ORDER1 group by IDM) b1 on a2.IDM=b1.IDM ";
        try {
            conn = ConnectSql.getMySQLConnection();
            Statement stmnt = conn.createStatement();
            ResultSet res = stmnt.executeQuery(sql);
            while (res.next()) {

                User user = new User();
              //  System.out.println(res.getString("SURNAME"));
                user.setId(res.getLong("ID"));
                user.setFirstName(res.getString("NAME"));
                user.setLastName(res.getString("SURNAME"));
                user.setFatherName(res.getString("FNAME"));
                user.setRole(Role.valueOf(res.getString("ROLE").trim())); //


                mList.add(new Mechanic(user, res.getDouble("HPRICE"), res.getInt("IDO")));
                conn.close();
            }

        } catch (SQLException |  ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("ошибки при заполнении "+sql +"      ");
        }


        return mList;
    }
// удалить строку
    public boolean deleteMechanic(Mechanic mechanic) {

        sql = "delete from user where id= " + mechanic.getName().getId() + "";
// поиск ид
        try {
            conn = ConnectSql.getMySQLConnection();
            Statement stmnt = conn.createStatement();
            stmnt.executeUpdate(sql);
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
          //  System.out.println("ошибки при заполнении " + sql);
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
// обновить строку
    public boolean UpdateMechanic(Mechanic mechanic) {
        sql = "UPDATE  USER set NAME='" + mechanic.getName().getFirstName() + "', " +
                "SURNAME='" + mechanic.getName().getLastName() + "',FNAME='" + mechanic.getName().getFatherName() + "' " +
                "where id='" + mechanic.getName().getId() + "' ";

        String sql2 = "UPDATE MECHANIC set HPRICE='" + mechanic.getPrice() + "' where IDM='" + mechanic.getName().getId() + "'";
        try {
            conn = ConnectSql.getMySQLConnection();
            Statement stmnt = conn.createStatement();

            conn.setAutoCommit(false);
            stmnt.executeUpdate(sql);
            stmnt.executeUpdate(sql2);
            conn.commit();

            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ошибки при заполнении " + sql + "\n      " + sql2);
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
// Создать строку
    public boolean CreateMechanic(Mechanic mechanic) {
        Connection conn = null;
        sql = " INSERT into USER (NAME,SURNAME,FNAME,ROLE) " +
                "   values ('" + mechanic.getName().getFirstName() + "'," +
                "'" + mechanic.getName().getLastName() + "'," +
                "'" + mechanic.getName().getFatherName() + "','механик')";
// поиск ид
        String sql2 = "Insert INTO MECHANIC  (IDM,HPRICE) values ((select  max(a1.ID) from user a1\n" +
                "left join MECHANIC a2 on a1.ID=a2.IDM \n" +
                "where a1.role like  'механик' and HPRICE is null),'" + mechanic.getPrice() + "')";


        try {
            conn = ConnectSql.getMySQLConnection();
            Statement stmnt = conn.createStatement();


            conn.setAutoCommit(false);
            stmnt.executeUpdate(sql);
            stmnt.executeUpdate(sql2);
            conn.commit();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ошибки при заполнении " + sql + "\n      " + sql2);
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
