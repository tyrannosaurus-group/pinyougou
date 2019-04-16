package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.FindOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import entity.PageResult;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.bind.annotation.*;
import vo.OrderCountVo;
import vo.OrderVo;
import vo.UserOrderVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单管理
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private FindOrderService findOrderService;

    //提交订单
    @RequestMapping("/search")
    public PageResult search(Integer pageNo, Integer pageSize, @RequestBody OrderVo orderVo) {
        PageResult search = findOrderService.search(pageNo, pageSize,orderVo);
        return search;
    }

    @RequestMapping("/getdata")
    public String getdata(){
        //模拟日期   周一到周日
        //需要的数据   data1  每天的订单数量  放到一个集合中
        //            data2  每天订单所产生的总金额
         //获取data1  订单数量

        HashMap<Object, Object> map = new HashMap<>();

        List<Integer>PriceList = new ArrayList<>();
        List<Integer>NumList = new ArrayList<>();
        Map<String,List<Order>>orders = findOrderService.findOrders();
        //System.out.println(orders);
        if (orders!=null&&orders.size()>0){
            Set<String> keySet = orders.keySet();
              if (keySet!=null&&keySet.size()>0){
                  for (String s : keySet) {
                      List<Order> orders1 = orders.get(s);
                          if (orders1!=null&&orders1.size()>0){
                              Integer count = 0;


                              for (Order order : orders1) {
                                  int i = order.getPayment().intValue();
                                  count +=i;
                              };
                              NumList.add(orders1.size());
                              PriceList.add(count);
                          }
                  }
              }


        };

         map.put("num",NumList);
         map.put("price",PriceList);

         return  JSON.toJSONString(map);















//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(120);
//        list.add(210);
//        list.add(445);
//        list.add(656);
//        list.add(367);
//        list.add(478);
//        list.add(589);
//        String s = JSON.toJSONString(list);
//        return s;
    }

    @RequestMapping("/orderCount")
    public OrderCountVo orderCount() {
        return findOrderService.orderCount();
    }


    @RequestMapping("/searchOrder")
    public PageResult download(Integer pageNo, Integer pageSize,@RequestBody UserOrderVo vo) {
        PageResult pageResult = findOrderService.download(pageNo, pageSize, vo);
        return pageResult;
    }


    @RequestMapping(value = "/download", method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public void exportReportStaticsData(
                                        HttpServletRequest request, HttpServletResponse response) {
        String mether =request.getMethod();


        HSSFWorkbook wb = new HSSFWorkbook();//声明工
        Sheet sheet = wb.createSheet("用户数据导出表");//新建表
        sheet.setDefaultColumnWidth(15);//设置表宽
        HSSFCellStyle style = wb.createCellStyle();
        org.apache.poi.hssf.usermodel.HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 12);
        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(headerFont);

        CellRangeAddress cra0 = new CellRangeAddress(0, 1,0,9);
        sheet.addMergedRegion(cra0);
        sheet.setDefaultColumnWidth((short) 15);
        Row row = sheet.createRow(0);
        Cell cell1 = row.createCell(0);

        cell1.setCellValue("用户数据导出表");
        cell1.setCellStyle(headerStyle);
        //设置字体样式
        org.apache.poi.hssf.usermodel.HSSFFont titleFont = wb.createFont();
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(titleFont);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Row row1 = sheet.createRow(2);
        Cell cell = row1.createCell(0);
        cell.setCellValue("用户id");
        cell.setCellStyle(style);
        cell = row1.createCell(1);
        cell.setCellValue("订单id");
        cell.setCellStyle(style);
        cell = row1.createCell(2);
        cell.setCellValue("订单状态");
        cell.setCellStyle(style);
        cell = row1.createCell(3);
        cell.setCellValue("商品名称");
        cell.setCellStyle(style);
        cell = row1.createCell(4);
        cell.setCellValue("商品价格");
        cell.setCellStyle(style);
        cell = row1.createCell(5);
        cell.setCellValue("购买数量");
        cell.setCellStyle(style);
        cell = row1.createCell(6);
        cell.setCellValue("总费用");
        cell.setCellStyle(style);

        //时间转字符串的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        int s = 0;

        List<UserOrderVo> list=findOrderService.findAllUserOrders();

        for (int i = 0, imax = list.size(); i < imax; i++) {
            /*row1 = sheet.createRow(i + 3);*/
            /*if (list.get(i).getUserId()== null||"".equals(list.get(i).getUserId())) {
                row1.createCell(0).setCellValue("-");
            } else {
                row1.createCell(0).setCellValue(list.get(i).getUserId());
            }*/




            List<Order> orderList = list.get(i).getOrderList();

            /*if (null == orderList || orderList.size() == 0) {
                row1 = sheet.createRow(i + 3);


                if (list.get(i).getUserId()== null||"".equals(list.get(i).getUserId())) {
                    row1.createCell(0).setCellValue("-");
                } else {
                    row1.createCell(0).setCellValue(list.get(i).getUserId());
                }

            }*/

            for (int i1 = 0; i1 < orderList.size(); i1++) {



                /*row1 = sheet.createRow(i1+i + 3);*/

                /*if (orderList.get(i1).getOrderId() == null ||"".equals(orderList.get(i1).getOrderId())) {
                    row1.createCell(1).setCellValue("-");
                } else {
                    row1.createCell(1).setCellValue(orderList.get(i1).getOrderId().toString());
                }

                if (orderList.get(i1).getStatus() == null ||"".equals(orderList.get(i1).getStatus())) {
                    row1.createCell(2).setCellValue("-");
                } else {
                    row1.createCell(2).setCellValue(orderList.get(i1).getStatus());
                }
*/
                List<OrderItem> orderItemList = orderList.get(i1).getOrderItemList();
                if (null == orderItemList || orderItemList.size() == 0) {
                    Row row2 = sheet.createRow(3+s);

                    s++;

                     if (list.get(i).getUserId()== null||"".equals(list.get(i).getUserId())) {
                    row2.createCell(0).setCellValue("-");
                    } else {
                    row2.createCell(0).setCellValue(list.get(i).getUserId());
                    }

                    if (orderList.get(i1).getOrderId() == null ||"".equals(orderList.get(i1).getOrderId())) {
                        row2.createCell(1).setCellValue("-");
                    } else {
                        row2.createCell(1).setCellValue(orderList.get(i1).getOrderId().toString());
                    }

                    if (orderList.get(i1).getStatus() == null ||"".equals(orderList.get(i1).getStatus())) {
                        row2.createCell(2).setCellValue("-");
                    } else {
                        row2.createCell(2).setCellValue(orderList.get(i1).getStatus());
                    }

                }
                for (int i2 = 0; i2 < orderItemList.size(); i2++) {

                    row1 = sheet.createRow(3+s);

                    if (list.get(i).getUserId() == null || "".equals(list.get(i).getUserId())) {
                        row1.createCell(0).setCellValue("-");
                    } else {
                        row1.createCell(0).setCellValue(list.get(i).getUserId());
                    }


                    if (orderList.get(i1).getOrderId() == null ||"".equals(orderList.get(i1).getOrderId())) {
                        row1.createCell(1).setCellValue("-");
                    } else {
                        row1.createCell(1).setCellValue(orderList.get(i1).getOrderId().toString());
                    }

                    if (orderList.get(i1).getStatus() == null ||"".equals(orderList.get(i1).getStatus())) {
                        row1.createCell(2).setCellValue("-");
                    } else {
                        row1.createCell(2).setCellValue(orderList.get(i1).getStatus());
                    }


                    if (orderItemList.get(i2).getTitle() == null ||"".equals(orderItemList.get(i2).getTitle())) {
                        row1.createCell(3).setCellValue("-");
                    } else {
                        row1.createCell(3).setCellValue(orderItemList.get(i2).getTitle());
                    }

                    if (orderItemList.get(i2).getPrice() == null ||"".equals(orderItemList.get(i2).getPrice())) {
                        row1.createCell(4).setCellValue("-");
                    } else {
                        row1.createCell(4).setCellValue(orderItemList.get(i2).getPrice().toString());
                    }

                    if (orderItemList.get(i2).getNum() == null ||"".equals(orderItemList.get(i2).getNum())) {
                        row1.createCell(5).setCellValue("-");
                    } else {
                        row1.createCell(5).setCellValue(orderItemList.get(i2).getNum());
                    }

                    if (orderItemList.get(i2).getTotalFee() == null ||"".equals(orderItemList.get(i2).getTotalFee())) {
                        row1.createCell(6).setCellValue("-");
                    } else {
                        row1.createCell(6).setCellValue(orderItemList.get(i2).getTotalFee().toString());
                    }
                    s++;

                }

            }

        }
        response.reset();
        response.setContentType("application/msexcel;charset=UTF-8");
        try {
            SimpleDateFormat newsdf=new SimpleDateFormat("yyyyMMddHHmmss");
           String date = newsdf.format(new Date());
          response.addHeader("Content-Disposition", "attachment;filename=\""
                    + new String(("用户数据导出表" + date + ".xls").getBytes("GBK"),
                    "ISO8859_1") + "\"");
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "导出失败!");
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "导出失败!");
            e.printStackTrace();
        }
    }



}
