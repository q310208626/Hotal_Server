package com.lsj.servlet;


import com.lsj.db.DBHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hdmi on 16-12-2.
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        java.lang.String querySql="select 1 from customer where customid=? and cpassword=?";
        List<Object> queryParmas = new ArrayList<Object>();
        Map<String, Object> map=null;
        DBHelper dbHelper=new DBHelper();
        dbHelper.getConnection();
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out=resp.getWriter();
        req.setCharacterEncoding("utf-8");

        java.lang.String account=req.getParameter("account");
        java.lang.String password=req.getParameter("password");
        System.out.print(account);
        System.out.print(password);
        queryParmas.add(account);
        queryParmas.add(password);

        try {
            System.out.print("beforequery------------");
            map=dbHelper.findSimpleResult(querySql,queryParmas);
            System.out.print(map.get("1"));
            if(map.get("1")==null){
                System.out.print("登录失败");
                java.lang.String respMSG="false";
                out.print(respMSG);
            }else{
                System.out.print("登录成功");
                java.lang.String respMSG="true";
                out.print(respMSG);

            }
        } catch (SQLException e) {
            dbHelper.releaseConn();
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

}
