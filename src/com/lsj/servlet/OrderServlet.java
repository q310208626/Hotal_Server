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
 * Created by hdmi on 16-12-7.
 */
public class OrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        java.lang.String querySql="select firstRoom,secondRoom,thirdRoom,order_datetime," +
                "check_in_time,departure_time from _order where customid=?";
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
        queryParmas.add(account);

        try {
            resultMap=dbHelper.findModeResult(querySql,queryParmas);
            try {
                resultJsonObject.put("result",true);
                resultJsonArray.put(resultJsonObject);
                for (int i=0;i<resultMap.size();i++){
                    JSONObject menberObject=new JSONObject();
                    menberObject.put("firstRoomAmount",resultMap.get(i).get("firstRoom"));
                    menberObject.put("secondRoomAmount",resultMap.get(i).get("secondRoom"));
                    menberObject.put("thirdRoomAmount",resultMap.get(i).get("thirdRoom"));
                    menberObject.put("order_datetime",resultMap.get(i).get("order_datetime"));
                    menberObject.put("check_in_time",resultMap.get(i).get("check_in_time"));
                    menberObject.put("departure_time",resultMap.get(i).get("departure_time"));
                    resultJsonArray.put(menberObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            try {
                resultJsonObject.put("result",false);
                resultJsonArray.put(resultJsonObject);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        out.print(resultJsonArray.toString());

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
