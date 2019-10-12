package top.tobing.dao;

import top.tobing.domain.User;
import top.tobing.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserDao {
    /**
     * 根据用户名密码登录
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public User login (String username,String password) throws SQLException {
        User user = null;
        Connection connection = JDBCUtils.getConnection();
        Statement statement = connection.createStatement();
        String sql = "select * from user where username = '"+username+"' and password = '"+password+"'";
        //System.out.println("sql=:"+sql);
        ResultSet resultSet = statement.executeQuery(sql);
        if(resultSet.next()){
            user = new User();
            int id = resultSet.getInt("id");
            String username1 = resultSet.getString("username");
            String name = resultSet.getString("name");
            String password1 = resultSet.getString("password");
            boolean isadmin = resultSet.getBoolean("isadmin");
            user.setId(id);
            user.setUsername(username);
            user.setPassword(password);
            user.setName(name);
            user.setIsadmin(isadmin);
        }
        return user;
    }

    /**
     *
     */

}
