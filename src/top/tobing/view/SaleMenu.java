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
