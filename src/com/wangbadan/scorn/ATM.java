package com.wangbadan.scorn;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private Account loginAcc;

    //菜单开始界面
    public void start() {
        while (true) {
            System.out.println("===欢迎来到ATM系统操作界面===");
            System.out.println("1.用户登录");
            System.out.println("2.用户注册");
            System.out.println("请选择您的操作：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    login();
                    break;
                case 2:
                    creatAccount();
                    break;
                default:
                    System.out.println("不存在该操作！");
            }
        }
    }

    public void login() {
        System.out.println("==系统登录==");
        //1.先判断是否存在账户，不存在则直接退出
        if (accounts.size() == 0) {
            System.out.println("系统中尚不存在用户，请您先开户！");
            return;
        }
        //2.存在用户则开始登录操作
        while (true) {
            System.out.println("请输入您的登录卡号：");
            String cardId = sc.next();
            Account acc = getAccountByCardId(cardId);
            if (acc == null) {
                System.out.println("不存在该卡号！");
            } else {
                while (true) {
                    System.out.println("请输入密码：");
                    String passWord = sc.next();
                    if (acc.getPassWord().equals(passWord)) {
                        loginAcc = acc;
                        System.out.println("恭喜你，" + acc.getUserName() + "成功登录系统，您的卡号是：" + acc.getCardId());
                        showUserCommand();
                        return;
                    } else {
                        System.out.println("您输入的密码有误！");
                    }
                }
            }
        }
    }

    private void showUserCommand() {
        while (true) {
            System.out.println(loginAcc.getUserName() + "您可以选择如下功能进行账户管理====");
            System.out.println("1.查询账户");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.密码修改");
            System.out.println("6.退出");
            System.out.println("7.注销当前账户");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    showLoginAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    drawMoney();
                    break;
                case 4:
                    transferMoney();
                    break;
                case 5:
                    updatePassWord();
                    return;
                case 6:
                    System.out.println(loginAcc.getUserName() + "您成功退出系统！");
                    return;
                case 7:
                    if(deleteAccount())
                        return;
                    break;
                default:
                    System.out.println("您选择的操作不存在！");
            }
        }
    }

    //密码修改
    private void updatePassWord() {
        while (true) {
            System.out.println("==账号密码修改操作==");
            System.out.println("请输入原密码:");
            String passWord=sc.next();
            if(loginAcc.getPassWord().equals(passWord)){
                while (true) {
                    System.out.println("请您输入新密码：");
                    String newPassWord=sc.next();
                    System.out.println("请您再次输入新密码：");
                    String okynewPassWord=sc.next();
                    if(newPassWord.equals(okynewPassWord)){
                        loginAcc.setPassWord(okynewPassWord);
                        System.out.println("修改密码成功！");
                        return;
                    }
                    else{
                        System.out.println("两次输入密码不一致!");
                    }
                }
            }else{
                System.out.println("输入密码错误！");
            }
        }
    }

    //销户操作
    private boolean deleteAccount() {
        System.out.println("==进行销户操作==");
        System.out.println("确认要销户吗？y/n");
        String command=sc.next();
        switch (command){
            case "y":
                if(loginAcc.getMoney()==0){
                    accounts.remove(loginAcc);
                    System.out.println("销户成功！");
                    return true;
                }
                else{
                    System.out.println("您的账户还有余额，不允许销户！");
                    return false;
                }
            default:
                System.out.println("好的，您的账户保留！");
                return false;
        }
    }

    private void transferMoney() {
        System.out.println("==用户转账==");
        //1.判断系统是否存在其他账户
        if(accounts.size()<2){
            System.out.println("当前系统只有您一个用户，无法进行转账！");
            return;
        }
        //2.判断账户是否有余额转账
        if(loginAcc.getMoney()==0){
            System.out.println("您自己都没钱，别转账了！");
            return;
        }
        while (true) {
            System.out.println("请输入对方卡号：");
            String cardId=sc.next();
            Account acc=getAccountByCardId(cardId);
            if(acc==null){
                System.out.println("您输入的卡号不存在！");
            }
            else{
                String name="*"+acc.getUserName().substring(1);
                System.out.println("请输入【"+name+"】的姓氏：");
                String preName=sc.next();
                if(acc.getUserName().startsWith(preName)){
                    while (true) {
                        System.out.println("请输入转账金额：");
                        double money= sc.nextDouble();
                        if(loginAcc.getMoney()>=money){
                            loginAcc.setMoney(loginAcc.getMoney()-money);
                            acc.setMoney(acc.getMoney()+money);
                            System.out.println("转账成功！您现在的余额是："+loginAcc.getMoney());
                            return;
                        }
                        else{
                            System.out.println("余额不足，您账户的余额是："+loginAcc.getMoney());
                        }
                    }
                }
                else{
                    System.out.println("您输入的姓氏有误！");
                }
            }
        }
    }

    private void drawMoney() {
        System.out.println("==取款操作==");
        if(loginAcc.getMoney()<100){
            System.out.println("余额不足100元，不允许取钱！");
            return;
        }
        while (true) {
            System.out.println("请输入取款金额：");
            double money= sc.nextDouble();
            if(loginAcc.getMoney()>=money){
                //进行取钱操作
                if(loginAcc.getLimit()>=money){
                    loginAcc.setMoney(loginAcc.getMoney()-money);
                    System.out.println("取款成功，您现在的余额是："+loginAcc.getMoney());
                    return;
                }
                else{
                    System.out.println("您当前取款超过每次限额，您每次最多可取："+loginAcc.getLimit());
                }
            }
            else{
                System.out.println("对不起，余额不足，您的余额是："+loginAcc.getMoney());
            }
        }
    }

    private void depositMoney() {
        System.out.println("==存钱操作==");
        System.out.println("请您输入存款金额：");
        double money=sc.nextDouble();
        loginAcc.setMoney(loginAcc.getMoney()+money);
        System.out.println("恭喜您，您存钱："+money+"成功，存款后余额为："+loginAcc.getMoney());
    }

    private void showLoginAccount() {
        System.out.println("==当前您的账户信息如下：==");
        System.out.println("卡号：" + loginAcc.getCardId());
        System.out.println("户主：" + loginAcc.getUserName());
        System.out.println("余额：" + loginAcc.getMoney());
        System.out.println("性别：" + loginAcc.getSex());
        System.out.println("每次提取额度：" + loginAcc.getLimit());
    }

    private void creatAccount() {

        System.out.println("===系统开户===");
        Account acc = new Account();
        //确认账号名称
        System.out.println("请输入您的账户名称：");
        String name = sc.next();
        acc.setUserName(name);
        //确认号主性别
        while (true) {
            System.out.println("请输入您的性别：");
            char sex = sc.next().charAt(0);
            if (sex == '男' || sex == '女') {
                acc.setSex(sex);
                break;
            } else {
                System.out.println("您输入的性别有误！");
            }
        }
        //确认账号密码
        while (true) {
            System.out.println("请输入您的账号密码：");
            String passWord = sc.next();
            System.out.println("请确认您的密码：");
            String okypassWord = sc.next();
            if (okypassWord.equals(passWord)) {
                acc.setPassWord(passWord);
                break;
            } else {
                System.out.println("两次密码不一致，请重新输入！");
            }
        }
        //设置存款额度
        System.out.println("请输入您的取款额度：");
        double limit = sc.nextDouble();
        acc.setLimit(limit);
        //系统自动生成8位不重复卡号
        String CardId = creatCardId();
        acc.setCardId(CardId);
        accounts.add(acc);
        System.out.println("恭喜你，" + acc.getUserName() + "开户成功！" + "您的卡号是：" + acc.getCardId());
    }

    //创建卡号
    private String creatCardId() {
        while (true) {
            String id = "";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                int data = r.nextInt(10);
                id += data;
            }
            Account a = getAccountByCardId(id);
            if (a == null)
                return id;
        }
    }

    //根据卡号来寻找账号对象
    private Account getAccountByCardId(String id) {
        for (int i = 0; i < accounts.size(); i++) {
            Account a = accounts.get(i);
            if (a.getCardId().equals(id)) {
                return a;
            }
        }
        return null;
    }
}
