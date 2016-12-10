package com.lsj.servlet;

import com.lsj.db.DBHelper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.org.apache.xpath.internal.operations.String;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by hdmi on 16-11-29.
 */
public class RegistServlet extends HttpServlet{

    public RegistServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        java.lang.String querySql="select 1 from customer where customid=?";
        java.lang.String insertSql="insert into customer set customid=?,cpassword=?";
        Map<java.lang.String, Object> map=null;
        List<Object> queryParmas = new ArrayList<Object>();
        List<Object> insertParmas = new ArrayList<Object>();
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out=resp.getWriter();
        req.setCharacterEncoding("utf-8");
        java.lang.String account=req.getParameter("account");
        java.lang.String password=req.getParameter("password");
        queryParmas.add(account);
        DBHelper dbHelper=new DBHelper();
        dbHelper.getConnection();
        System.out.print("regist--------");
        try {
            //查找是否存在用户名
            map=dbHelper.findSimpleResult(querySql,queryParmas);
            //如果不存在则继续执行
            if(map.get("1")==null){
                Boolean result=false;
                insertParmas.add(account);
                insertParmas.add(password);
                //插入用户名跟密码
                result=dbHelper.updateByPreparedStatement(insertSql,insertParmas);
                System.out.print("result           "+result);
                java.lang.String respMSG="true";
                out.print(respMSG);
            }else{
                //不存在则返回false跟Client
                java.lang.String respMSG="false";
                out.print(respMSG);
                System.out.print(respMSG);
            }

        } catch (SQLException e) {
            dbHelper.releaseConn();
            e.printStackTrace();
        }
        System.out.print(account+"  "+password+"\n");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
