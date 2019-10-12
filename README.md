# JDBC+MySQL实现超市信息管理系统

### 1. 概述

+ 超市商品管理系统用于管理员、销售员的登录。管理员和销售员查看数据。管理员可以进行入库。销售可以销售商品

### 2. 需求分析

1. 销售员，管理员登录
2. 管理员添加商品信息，对商品信息修改
3. 销售员可以对商品进行查询，出售等

### 3. 程序设计

+ ##### 实现方式：

  + Java（JDBC）+MySQL

+ ##### 数据库表

  + 用户表（User）

    | id   | 姓名 | 用户名 | 密码 | 类型 |
    | ---- | ---- | ------ | ---- | ---- |
    |      |      |        |      |      |

  + 商品信息表（Good）

    | gid  | 商品名 | 品牌 | 进货时间 | 销售时间 | 进价 | 售价 | 剩余量 | 经办人 |
    | ---- | ------ | ---- | -------- | -------- | ---- | ---- | ------ | ------ |
    |      |        |      |          |          |      |      |        |        |

+ ##### 视图层

  + 登录界面
  + 管理员菜单界面
    + 入库（新增或者添加）
      + 商品名、品牌、进货时间、进价、进货量、经办人
    + 查询
      + 进货时间、商品名、剩余量、经办人
  + 销售员菜单界面
    + 销售（更新商品剩余量）
      + 商品名
    + 查询
      + 销售时间、商品名、品牌、售价、剩余量

+ Dao层

  + 用户表
  + 商品表

+ 流程图

  ![1570844735960](C:\Users\Tobing\AppData\Roaming\Typora\typora-user-images\1570844735960.png)

### 4. 程序实现

+ 工具
  + idea2019
  + Mysql5.5
  + jdk9.0
  + mysql驱动jar

##### 1. 准备工作

+ 创建数据库

  ```mysql
  CREATE DATABASE `test` # 创建数据库
  USE `test`;	# 使用该数据库
  
  DROP TABLE IF EXISTS `good`; 
  CREATE TABLE `good` (  # 创建商品表表
    `gid` int(11) NOT NULL AUTO_INCREMENT, 
    `goodname` varchar(20) NOT NULL, # 商品名
    `goodbrand` varchar(20) NOT NULL,# 商品品牌
    `intime` date DEFAULT NULL,      # 商品进货时间
    `saletime` date DEFAULT NULL,    # 商品销售时间
    `inprice` double DEFAULT NULL,   # 商品进价
    `saleprice` double DEFAULT NULL, # 商品售价
    `stock` int(11) DEFAULT NULL,    # 商品剩余量
    `agent` varchar(20) DEFAULT NULL,# 商品经办人
    PRIMARY KEY (`gid`)  # 主键
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
  DROP TABLE IF EXISTS `user`; 
  CREATE TABLE `user` (  # 创建用户表
    `id` int(11) NOT NULL,
    `username` varchar(20) NOT NULL, # 用户名
    `password` varchar(20) NOT NULL, # 密码
    `name` varchar(20) NOT NULL,     
    PRIMARY KEY (`id`)   # 主键
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  ```

  

+ 下载驱动jar包

  + 到`Mysql`官网下载`mysql`驱动包`<https://dev.mysql.com/downloads/connector/j/>`
  + Windows平台选择Platform Independent

+ 安装`Mysql`（省略，自行百度安装）

+ 安装JDK（省略，自行百度安装）

##### 2. 项目创建

+ 项目目录框架

  ![1570848487712](D:\Users\Tobing\OneDrive - leverage innovative users\桌面\总结文档\Java复习\assets\1570848487712.png)

+ 测试Mysql的连接情况

  + 测试代码

    ```java
    package top.tobing.test;
    
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.Statement;
    
    public class ConnTest {
        public static void main(String[] args) throws Exception {
            // 加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 获取连接对象
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
            // 定义sql 语句
            String sql = "select * from user";
            // 创建执行对象
            Statement statement = connection.createStatement();
            // 执行查询sql语句
            ResultSet resultSet = statement.executeQuery(sql);
            // 输出结果集
            System.out.println(resultSet);
        }
    }
    ```

  + 如果成功输出结果集的哈希地址，则说明成功连接上数据库

  ###### 1. 封装JDBC工具类：JDBCUtils

  + 目的，简化开发流程

    ```java
    package top.tobing.utils;
    import java.sql.*;
    public class JDBCUtils {
        static {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        /**
         * 得到数据库的连接
         */
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=UTC", "root", "root");
        }
    
        /**
         * 关闭所有打开的资源
         */
        public static void close(Connection conn, Statement stmt) {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    
        /**
         * 关闭所有打开的资源
         */
        public static void close(Connection conn, Statement stmt, ResultSet rs) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } close(conn, stmt);
        }
    }
    ```

    

  ###### 2. 编写JavaBean

  + 根据创建的数据库表，在domain包中创建对应的JavaBean类

    ```java
    package top.tobing.domain;
    // 对用用户表
    public class User {
        private int id;
        private String username;
        private String password;
        private String name;
        private boolean isadmin;
    
        public boolean isIsadmin() {
            return isadmin;
        }
        public void setIsadmin(boolean isadmin) {
            this.isadmin = isadmin;
        }
    	...省略其他getter和setter方法
    }
    // 对用商品表
    import java.util.Date;
    public class Good {
        private int gid;
        private String goodname;
        private String goodbrand;
        private Date intime;
        private Date saletime;
        private double inprice;
        private double saleprice;
        private int stock;
        private String agent;
        
        public int getGid() {
            return gid;
        }
        public void setGid(int gid) {
            this.gid = gid;
        }
    	...省略其他getter和setter方法
    }
    
    ```

  ###### 3. 登录功能

  + 登录界面：可以获取用户名，密码。根据返回的用户类型调用创建`管理类`或者`销售类`

    ```java
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
        // 数据库操作对象
        UserDao userDao =new UserDao();
    
        private void show() {
            // 获取登录账号和密码
            Scanner sc = new Scanner(System.in);
            System.out.println("------------超市商品管理系统------------");
            System.out.print("-1. 用户名：");
            String username = sc.next();
            System.out.print("-2. 密  码：");
            String password = sc.next();
            System.out.println("------------超市商品管理系统------------");
    
            // 根据登录用户名密码到数据库查询
            User login = null;
            try {
                login = userDao.login(username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(login!=null){
                System.out.println("登录成功！");
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
    ```

  + 根据用户名密码查询数据库

    ```java
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
    }
    
    ```

  ###### 4. 公共管理界面

  + 公共菜单功能

    ```java
    package top.tobing.view;
    import top.tobing.dao.GoodDao;
    import top.tobing.domain.Good;
    import top.tobing.domain.User;
    import java.util.List;
    import java.util.Scanner;
    
    public class Menu {
        User user = null;
        public Menu(User user){
            this.user = user;
        }
        GoodDao goodDao = new GoodDao();
        /**
         * 通用商品查询
         */
        public void findGoods(){
            Good g = new Good();
            while(true){
                System.out.println("-----------商品查询-----------");
                System.out.println("-1. 根据商品名称");
                System.out.println("-2. 根据商品品牌");
                System.out.println("-3. 根据商品经办人");
                System.out.println("-0. 退出");
                System.out.print("-请输入你的选择：");
                Scanner sc = new Scanner(System.in);
                int i = sc.nextInt();
                List<Good> goods =null;
                switch (i){
                    case 1:
                        System.out.print("请输入商品名称：");
                        String name = sc.next();
                        goods = goodDao.findByName(name);
                        System.out.println("--------------------------------------------");
                        System.out.println("商品名称\t\t商品品牌\t\t商品进价\t\t商品剩余\t\t商品经办人");
                        for (Good good : goods) {
                            showGood(good);
                        }
                        break;
                    case 2:
                        System.out.print("请输入商品品牌：");
                        String brand = sc.next();
                        goods = goodDao.findByBrand(brand);
                        System.out.println("--------------------------------------------");
                        System.out.println("商品名称\t\t商品品牌\t\t商品进价\t\t商品剩余\t\t商品经办人");
                        for (Good good : goods) {
                            showGood(good);
                        }
                        break;
                    case 3:
                        System.out.print("请输入商品经办人：");
                        String agent = sc.next();
                        goods = goodDao.findByAgent(agent);
                        System.out.println("--------------------------------------------");
                        System.out.println("商品名称\t\t商品品牌\t\t商品进价\t\t商品剩余\t\t商品经办人");
                        for (Good good : goods) {
                            showGood(good);
                        }
                        break;
                    case 0:
                        System.out.println("退出");
                        return;
                    default:
                        System.out.println("输入有误请重新输入");
                        break;
                }
            }
        }
    
        /**
         * 展示商品
         * @param good
         */
        public void showGood(Good good){
            System.out.println(good.getGoodname()+"\t\t"+good.getGoodbrand()+"\t\t"+good.getInprice()+"\t\t"+good.getStock()+"\t\t"+good.getAgent());
        }
    }
    ```

  ###### 5. 管理员功能

  + 管理员可以通过管理界面的选项选择对商品的操作

  + 查看商品信息（基础自公共菜单）

    ```java
    package top.tobing.view;
    
    import top.tobing.dao.GoodDao;
    import top.tobing.domain.Good;
    import top.tobing.domain.User;
    
    import java.util.List;
    import java.util.Scanner;
    
    /**
     * 管理员管理菜单
     */
    public class AdminMenu extends Menu{
    
        public AdminMenu(User user){
            super(user);
            this.show();
        }
        GoodDao goodDao = new GoodDao();
        private void show() {
            while(true){
                System.out.println("------------欢迎光临!"+user.getUsername()+"------------");
                System.out.println("-1. 商品入库");
                System.out.println("-2. 查看商品");
                System.out.println("-0. 退出");
                System.out.print("-请输入：");
                Scanner sc = new Scanner(System.in);
                int i = sc.nextInt();
                if(i==1){
                    newGoods(); // 添加商品（商品入库）
                }else if(i==2){
                    findGoods();// 查找商品
                }else if(i==0){
                    return;
                }else{
                    System.out.println("输入数字有误！");
                }
            }
        }
    
        /**
         * 商品入库
         */
        private void newGoods(){
    
            Scanner sc = new Scanner(System.in);
            System.out.println("-----------商品进货-----------");
            System.out.print("-商品名称：");
            String name = sc.next();
            System.out.print("-商品品牌：");
            String brand = sc.next();
            System.out.print("-商品数量：");
            int num = sc.nextInt();
            System.out.print("-商品进价：");
            double price = sc.nextDouble();
            List<Good> list = goodDao.findByName(name);
            if(list!=null&&(!list.isEmpty())){// 已经存在商品
                Good good = list.get(0);
                int temp = good.getStock();
                goodDao.updateByName(name,num+temp);
            }else{
                Good g = new Good();
                g.setGoodname(name);
                g.setGoodbrand(brand);
                g.setStock(num);
                g.setInprice(price);
                g.setAgent(user.getName());
                goodDao.addNewGoods(g);
            }
        }
    }
    
    
    ```

  ###### 6. 销售员功能

  + 销售员可以通过商品名称和商品数量销售商品

  + 查看商品信息（继承自菜单父类）

    ```java
    package top.tobing.view;
    
    import top.tobing.dao.GoodDao;
    import top.tobing.domain.Good;
    import top.tobing.domain.User;
    
    import java.util.List;
    import java.util.Scanner;
    
    /**
     * 销售人员管理菜单
     */
    public class SaleMenu extends Menu {
        public SaleMenu(User user){
            super(user);
            this.show();
        }
        GoodDao goodDao = new GoodDao();
        private void show() {
            while(true){
                System.out.println("------------欢迎光临!"+user.getUsername()+"------------");
                System.out.println("-1. 销售商品");
                System.out.println("-2. 查看库存");
                System.out.println("-0. 退出");
                System.out.print("-请输入：");
                Scanner sc = new Scanner(System.in);
                int i = sc.nextInt();
                if(i==1){
                    //System.out.println("商品销售");
                    saleGood();
                }else if(i==2){
                    //System.out.println("查看库存");
                    findGoods();
                }else if(i==0){
                    return;
                }else{
                    System.out.println("输入数字有误！");
                }
            }
    
        }
    
        public void saleGood(){
            Scanner sc = new Scanner(System.in);
            System.out.print("请输入要销售的商品名称：");
            String name = sc.next();
            System.out.print("请输入要销售数量：");
            int num = sc.nextInt();
            List<Good> list = goodDao.findByName(name);
            if(list!=null&&(!list.isEmpty())){
                Good good = list.get(0);
                if(good.getStock()<num){
                    System.out.println("商品数量不足！！剩余："+good.getStock());
                }else{
                    goodDao.updateByName(name,good.getStock()-num);
                }
            }else{
                System.out.println("商品名不存在，请重新选择！！！！");
            }
        }
    }
    ```

  ###### 6.商品Dao

  + 用于对good表的增删改

    ```java
    package top.tobing.dao;
    
    import top.tobing.domain.Good;
    import top.tobing.utils.GoodMapper;
    import top.tobing.utils.JDBCUtils;
    
    import java.sql.*;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;
    
    public class GoodDao {
        /**
         * 根据姓名查询商品
         * @param name
         * @return
         * @throws Exception
         */
        public List<Good> findByName(String name)  {
            Connection connection = null;
            ResultSet resultSet =null;
            Statement statement = null;
            List<Good> list =null;
            try {
                connection = JDBCUtils.getConnection();
                statement = connection.createStatement();
                String sql = "select * from good where goodname = '" + name + "'";
                resultSet = statement.executeQuery(sql);
                list = new ArrayList<>();
                while (resultSet.next()) {
                    Good good = GoodMapper.getObject(resultSet);
                    list.add(good);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return list;
        }
    
        /**
         * 根据品牌查询
         * @param brand
         * @return
         */
        public List<Good> findByBrand(String brand) {
            Connection connection = null;
            ResultSet resultSet =null;
            Statement statement = null;
            List<Good> list =null;
            try {
                connection = JDBCUtils.getConnection();
                statement = connection.createStatement();
                String sql = "select * from good where goodbrand = '" + brand + "'";
                resultSet = statement.executeQuery(sql);
                list = new ArrayList<>();
                while (resultSet.next()) {
                    Good good = GoodMapper.getObject(resultSet);
                    list.add(good);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return list;
        }
    
        /**
         * 根据经办人查询
         * @param agent
         * @return
         */
        public List<Good> findByAgent(String agent) {
            Connection connection = null;
            ResultSet resultSet =null;
            Statement statement = null;
            List<Good> list =null;
            try {
                connection = JDBCUtils.getConnection();
                statement = connection.createStatement();
                String sql = "select * from good where agent = '" + agent + "'";
                resultSet = statement.executeQuery(sql);
                list = new ArrayList<>();
                while (resultSet.next()) {
                    Good good = GoodMapper.getObject(resultSet);
                    list.add(good);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return list;
        }
    
        /**
         * 添加新商品
         * @param good
         */
        public void addNewGoods(Good good){
            Connection connection = null;
            ResultSet resultSet =null;
            Statement statement = null;
            List<Good> list =null;
            try {
                connection = JDBCUtils.getConnection();
                statement = connection.createStatement();
                String sql = "insert into good(goodname,goodbrand,intime,inprice,stock,agent) " +
                        "values('"+good.getGoodname()+"','"+good.getGoodbrand()+"','"+
                        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) +
                        "','"+good.getInprice()+"','"+good.getStock()+"','"+good.getAgent()+"')" ;
                statement.execute(sql);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    
        /**
         * 更新已经存在的商品数量
         * @param name
         * @param num
         */
        public void updateByName(String name,int num) {
            Connection connection = null;
            ResultSet resultSet =null;
            Statement statement = null;
            List<Good> list =null;
            try {
                connection = JDBCUtils.getConnection();
                statement = connection.createStatement();
                String sql = "update good set stock = "+num+" where goodname ='"+name+"'";
                System.out.println(sql);
                statement.execute(sql);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    ```

  ###### 7. 额外的类

  + 封装结果集的GoodMapper类

    ```java
    package top.tobing.utils;
    
    import top.tobing.domain.Good;
    
    import java.sql.Date;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    
    public class GoodMapper {
        public static Good getObject(ResultSet resultSet) throws SQLException {
            Good g = new Good();
            int gid = resultSet.getInt("gid");
            String goodname = resultSet.getString("goodname");
            String goodbrand = resultSet.getString("goodbrand");
            Date intime = resultSet.getDate("intime");
            Date saletime = resultSet.getDate("saletime");
            double inprice = resultSet.getDouble("inprice");
            double saleprice = resultSet.getDouble("saleprice");
            int stock = resultSet.getInt("stock");
            String agent = resultSet.getString("agent");
            g.setGid(gid);
            g.setGoodbrand(goodbrand);
            g.setGoodname(goodname);
            g.setIntime(intime);
            g.setSaleprice(saleprice);
            g.setSaletime(saletime);
            g.setInprice(inprice);
            g.setStock(stock);
            g.setAgent(agent);
            return g;
        }
    }
    ```

  + 主程序类GoodsAdminSystem

    ```java
    package top.tobing.test;
    import top.tobing.view.Login;
    public class GoodsAdminSystem {
        public static void main(String[] args) {
            Login login = new Login();
        }
    }
    ```

##### 3. 项目回顾

+ 项目实际框架

  ![1570884396730](D:\Users\Tobing\OneDrive - leverage innovative users\桌面\总结文档\Java复习\assets\1570884396730.png)

##### 4. 项目效果

+ 大概执行效果

  ```bash
  ------------超市商品管理系统------------
  -1. 用户名：admin
  -2. 密  码：root
  ------------欢迎光临!admin------------
  -1. 商品入库
  -2. 查看商品
  -0. 退出
  -请输入：2
  -----------商品查询-----------
  -1. 根据商品名称
  -2. 根据商品品牌
  -3. 根据商品经办人
  -0. 退出
  -请输入你的选择：3
  请输入商品经办人：tobing
  --------------------------------------------
  商品名称		商品品牌		商品进价		商品剩余		商品经办人
  华为Mate10		华为		1999.0		299		tobing
  华为P30		华为		3999.0		1324		tobing
  魅族16		魅族		1999.0		888		tobing
  -----------商品查询-----------
  -1. 根据商品名称
  -2. 根据商品品牌
  -3. 根据商品经办人
  -0. 退出
  -请输入你的选择：0
  退出
  ------------欢迎光临!admin------------
  -1. 商品入库
  -2. 查看商品
  -0. 退出
  -请输入：1
  -----------商品进货-----------
  -商品名称：华为Mate10
  -商品品牌：华为
  -商品数量：999
  -商品进价：1999
  ------------欢迎光临!admin------------
  -1. 商品入库
  -2. 查看商品
  -0. 退出
  -请输入：0
  ------------超市商品管理系统------------
  -1. 用户名：saler
  -2. 密  码：root
  ------------欢迎光临!saler------------
  -1. 销售商品
  -2. 查看库存
  -0. 退出
  -请输入：1
  请输入要销售的商品名称：华为P30
  请输入要销售数量：100000
  商品数量不足！！剩余：1324
  ------------欢迎光临!saler------------
  -1. 销售商品
  -2. 查看库存
  -0. 退出
  -请输入：0
  ```

  

#### 5. 总结

+ 本系统从构思到实现一共花费6hours
+ 编写本系统主要熟悉一下开发流程
+ 本系统任然存在很多缺陷
  + 没有使用数据库连接池
  + 没有使用prepareStatement
  + 没有对非法输入进行判断（负数，数据库注入等）
  + 程序的功能非常简陋
+ 此程序的功能如有其它缺陷，请自行判断修改。本人不维护。









