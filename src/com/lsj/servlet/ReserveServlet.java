package com.lsj.servlet;

import com.lsj.db.DBHelper;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hdmi on 16-12-4.
 */
public class ReserveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //查询订单中预定时间包含所选时间
        java.lang.String queryNotInRoomid="select distinct(room_order.roomid),roomtype from room,room_order" +
                ",_order where _order.order_datetime=room_order.order_datetime and room_order.roomid=room.roomid and " +
                "(_order.check_in_time>=? and _order.check_in_time<=?) or " +
                "(_order.departure_time>=? and _order.departure_time<=?)";

        java.lang.String queryAllRoomid="select roomid,roomtype from room";

        java.lang.String insertOrder="insert into _order(customid,firstRoom,secondRoom,thirdRoom," +
                "order_datetime,check_in_time,departure_time) values(?,?,?,?,?,?,?);";
        java.lang.String insertDetailOrder="insert into room_order(customid,roomid,order_datetime)" +
                "values(?,?,?);";
        java.lang.String roomPriceSql="select price,deposit,typeid from roomtype;";

        Boolean respMSG=false;
        List<Object> queryParmas = new ArrayList<Object>();
        List<Object> insertOrderParmas = new ArrayList<Object>();
        List<Object> insertDetailOrderParmas = new ArrayList<Object>();
        List<Map<String, Object>> resultMap=new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> allRoomMap=new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> roomPriceMap=new ArrayList<Map<String,Object>>();
        List firstRoom =new ArrayList();
        List secondRoom =new ArrayList();
        List thirdRoom =new ArrayList();
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out=resp.getWriter();
        req.setCharacterEncoding("utf-8");
        String account=req.getParameter("account");
        String startTime=req.getParameter("startTime");
        String finishTime=req.getParameter("finishTime");
        String firstRoomAmount=req.getParameter("firstRoomAmount");
        String secondRoomAmount=req.getParameter("secondRoomAmount");
        String thirdRoomAmount=req.getParameter("thirdRoomAmount");
        System.out.print(account+" "+startTime+" "+finishTime+" "+firstRoomAmount+" "+secondRoomAmount+" "+thirdRoomAmount);
        DBHelper dbHelper=new DBHelper();
        dbHelper.getConnection();
        try {
            queryParmas.add(startTime);
            queryParmas.add(finishTime);
            queryParmas.add(startTime);
            queryParmas.add(finishTime);
            resultMap=dbHelper.findModeResult(queryNotInRoomid,queryParmas);
            allRoomMap=dbHelper.findModeResult(queryAllRoomid,null);
            allRoomMap.removeAll(resultMap);
            for (int i=0;i<allRoomMap.size();i++){
                System.out.print(allRoomMap.get(i).get("roomid"));
            }

            for(int i=0;i<allRoomMap.size();i++){
                int roomType=Integer.parseInt(allRoomMap.get(i).get("roomtype").toString());
                String roomId=allRoomMap.get(i).get("roomid").toString();

                if(roomType==1){
                    firstRoom.add(roomId);
                }else if(roomType==2){
                    secondRoom.add(roomId);
                }else if(roomType==3){
                    thirdRoom.add(roomId);
                }
            }
            System.out.print("一:"+firstRoom.size()+"   二："+secondRoom.size()+"    三："+thirdRoom.size());
            JSONArray resultArray=new JSONArray();
            JSONObject resultJson=new JSONObject();
            JSONObject detailJson=new JSONObject();
            if(Integer.parseInt(firstRoomAmount)<=firstRoom.size() && Integer.parseInt(secondRoomAmount)<=secondRoom.size() && Integer.parseInt(thirdRoomAmount)<=thirdRoom.size()){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String orderDate=df.format(new Date());
                System.out.print(orderDate);
                insertOrderParmas.add(account);
                insertOrderParmas.add(firstRoomAmount);
                insertOrderParmas.add(secondRoomAmount);
                insertOrderParmas.add(thirdRoomAmount);
                insertOrderParmas.add(orderDate);
                insertOrderParmas.add(startTime);
                insertOrderParmas.add(finishTime);
                dbHelper.updateByPreparedStatement(insertOrder,insertOrderParmas);
                System.out.print("_order finish");
                for (int i=0;i<Integer.parseInt(firstRoomAmount);i++){
                    insertDetailOrderParmas.clear();
                    insertDetailOrderParmas.add(account);
                    insertDetailOrderParmas.add(firstRoom.get(i));
                    insertDetailOrderParmas.add(orderDate);
                    dbHelper.updateByPreparedStatement(insertDetailOrder,insertDetailOrderParmas);
                }
                for (int i=0;i<Integer.parseInt(secondRoomAmount);i++){
                    insertDetailOrderParmas.clear();
                    insertDetailOrderParmas.add(account);
                    insertDetailOrderParmas.add(secondRoom.get(i));
                    insertDetailOrderParmas.add(orderDate);
                    dbHelper.updateByPreparedStatement(insertDetailOrder,insertDetailOrderParmas);
                }
                for (int i=0;i<Integer.parseInt(thirdRoomAmount);i++){
                    insertDetailOrderParmas.clear();
                    insertDetailOrderParmas.add(account);
                    insertDetailOrderParmas.add(thirdRoom.get(i));
                    insertDetailOrderParmas.add(orderDate);
                    dbHelper.updateByPreparedStatement(insertDetailOrder,insertDetailOrderParmas);
                }
                roomPriceMap=dbHelper.findModeResult(roomPriceSql,null);
                respMSG=true;

                try {
                    detailJson.put("result",respMSG);
                    detailJson.put("firstPrice",roomPriceMap.get(0).get("price"));
                    detailJson.put("firstDeposit",roomPriceMap.get(0).get("deposit"));
                    detailJson.put("secondPrce",roomPriceMap.get(1).get("price"));
                    detailJson.put("secondDeposit",roomPriceMap.get(1).get("deposit"));
                    detailJson.put("thirdPrice",roomPriceMap.get(2).get("price"));
                    detailJson.put("thirdDeposit",roomPriceMap.get(2).get("deposit"));
                    detailJson.put("firstRoomAmount",firstRoomAmount);
                    detailJson.put("secondRoomAmount",secondRoomAmount);
                    detailJson.put("thirdRoomAmount",thirdRoomAmount);
                    detailJson.put("startTime",startTime);
                    detailJson.put("finishTime",finishTime);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    detailJson.put("result",respMSG);
                    //剩余房间数
                    detailJson.put("firstRoomAmount",firstRoom.size());
                    detailJson.put("secondRoomAmount",secondRoom.size());
                    detailJson.put("thirdRoomAmount",thirdRoom.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            out.print(detailJson.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
