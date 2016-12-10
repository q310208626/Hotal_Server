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

/**
 * Created by hdmi on 16-12-9.
 */
public class UnsubscribeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //删除详细订单
        java.lang.String deleteDetailOrderSql="delete from room_order where customid=? and order_datetime=?;";
        //删除大订单
        java.lang.String deleteOrderSql="delete from _order where customid=? and order_datetime=?;";
        List<Object> queryParmas = new ArrayList<Object>();
        Boolean detailorderResult=null;
        Boolean orderResult=null;
        Boolean result=false;
        DBHelper dbHelper=new DBHelper();
        dbHelper.getConnection();
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();
        request.setCharacterEncoding("utf-8");

        java.lang.String account=request.getParameter("account");
        java.lang.String order_datetime=request.getParameter("order_dateTime");
        System.out.print(account);
        System.out.print(order_datetime);
        queryParmas.add(account);
        queryParmas.add(order_datetime);

        try {
            detailorderResult=dbHelper.updateByPreparedStatement(deleteDetailOrderSql,queryParmas);
            orderResult=dbHelper.updateByPreparedStatement(deleteOrderSql,queryParmas);
            System.out.print("\n"+detailorderResult);
            System.out.print(" "+orderResult);
            if(detailorderResult&&orderResult){
                result=true;
            }
        } catch (SQLException e) {
            result=false;
            e.printStackTrace();
            System.out.print("  Exception");
        }

        out.print(Boolean.toString(result));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
