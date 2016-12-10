package com.lsj.servlet;

import com.lsj.db.DBHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by hdmi on 16-12-8.
 */
public class OrderDetailServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        java.lang.String querySql="select room_order.roomid,roomtype.typename,price,deposit from room_order,roomtype,room where " +
                "roomtype.typeid=room.roomtype and room.roomid=room_order.roomid and customid=? and order_datetime=?;";

        List<Object> queryParmas = new ArrayList<Object>();
        List<Map<String, Object>> resultMap=new ArrayList<Map<String,Object>>();
        DBHelper dbHelper=new DBHelper();
        JSONArray resultJsonArray=new JSONArray();
        JSONObject resultJsonObject=new JSONObject();
        dbHelper.getConnection();
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();
        request.setCharacterEncoding("utf-8");

        java.lang.String account=request.getParameter("account");
        java.lang.String order_datetime=request.getParameter("order_datetime");
        queryParmas.add(account);
        queryParmas.add(order_datetime);

        try {
            resultMap=dbHelper.findModeResult(querySql,queryParmas);

            try {
                resultJsonObject.put("result",true);

            resultJsonArray.put(resultJsonObject);
            for (int i=0;i<resultMap.size();i++){
                JSONObject menberObject=new JSONObject();
                menberObject.put("roomid",resultMap.get(i).get("roomid"));
                menberObject.put("typename",resultMap.get(i).get("typename"));
                menberObject.put("price",resultMap.get(i).get("price"));
                menberObject.put("deposit",resultMap.get(i).get("deposit"));
                resultJsonArray.put(menberObject);
                System.out.print("");
            }
                System.out.print(resultJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            try {
                resultJsonObject.put("result",false);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            resultJsonArray.put(resultJsonObject);
            e.printStackTrace();
        }

        out.print(resultJsonArray.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
