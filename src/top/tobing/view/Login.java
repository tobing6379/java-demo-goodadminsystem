package top.tobing.view;

import top.tobing.dao.UserDao;
import top.tobing.domain.User;

import java.sql.SQLException;
import java.util.Scanner;

public class Login {
    public Login(){
        while(true){
            this.show();
        }
    }
    UserDao userDao =new UserDao();

    private void show() {
        // 获取登录密码
        Scanner sc = new Scanner(System.in);
        System.out.println("------------超市商品管理系统------------");
        System.out.print("-1. 用户名：");
        String username = sc.next();
        System.out.print("-2. 密  码：");
        String password = sc.next();
        //System.out.println("------------超市商品管理系统------------");

        // 根据登录用户名密码到数据库查询
        User login = null;
        try {
            login = userDao.login(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(login!=null){
            //System.out.println("登录成功！");
            if(login.isIsadmin()){
                //System.out.println("你好，管理员");
                new AdminMenu(login);
            }else{
                //System.out.println("你好，销售员");
                new SaleMenu(login);
            }
            //System.out.println(login);
        }else{
            System.out.println("Error！账号或密码有误，请重新输入！");
        }


    }
}
