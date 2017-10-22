package com.stock.util;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.stock.model.model.StockMinuteDataModel;

/**
 * Created by hp on 2017/10/9.
 */
public class GetDayStocklData {
    public static String jsoupFetch(String url) throws Exception {
        return Jsoup.parse(new URL(url), 2 * 1000).html();
    }
    public void parse(){
        String htmlStr = "<table id=kbtable >"
                + "<tr> "
                + "<td width=123>"
                + "<div id=12>这里是要获取的数据1</div>"
                + "<div id=13>这里是要获取的数据2</div>"
                + "</td>"
                + "<td width=123>"
                + "<div id=12>这里是要获取的数据3</div>"
                + "<div id=13>这里是要获取的数据4</div>"
                + "</td>  "
                + "</tr>"
                + "</table>";
        Document doc = Jsoup.parse(htmlStr);

        // 根据id获取table
        org.jsoup.nodes.Element table = doc.getElementById("kbtable");
        System.out.println(table);
        // 使用选择器选择该table内所有的<tr> <tr/>
         org.jsoup.select.Elements trs = table.select("tr");
        //遍历该表格内的所有的<tr> <tr/>
        for (int i = 0; i < trs.size(); ++i) {
            // 获取一个tr
            org.jsoup.nodes.Element tr = trs.get(i);
            // 获取该行的所有td节点
            org.jsoup.select.Elements  tds = tr.select("td");
            // 选择某一个td节点
            for (int j = 0; j < tds.size(); ++j) {
                org.jsoup.nodes.Element  td = tds.get(j);
                // 获取td节点的所有div
                org.jsoup.select.Elements divs = td.select("div");
                // 选择一个div
                for (int k = 0; k < divs.size(); k++) {
                    org.jsoup.nodes.Element div = divs.get(k);
                    //获取文本信息
                    String text = div.text();
                    //输出到控制台
                    System.out.println(text);
                }
            }
        }
    }

  public void tabl(String urlhtml,List<StockMinuteDataModel> stockMinuteDataModels){
      Document doc = Jsoup.parse(urlhtml);
      org.jsoup.nodes.Element table = doc.getElementById("datatbl");
      org.jsoup.select.Elements trs = table.select("tr");
      Integer count=0;
      for (int i = 0; i < trs.size(); ++i) {
          // 获取一个tr
          StockMinuteDataModel stockMinuteDataModel =new StockMinuteDataModel();
          org.jsoup.nodes.Element tr = trs.get(i);
          //获取th节点  成交时间-买卖性质
          org.jsoup.select.Elements  ths = tr.select("th");
//          for (int q=0;q<ths.size();q++){
//              org.jsoup.nodes.Element th0= ths.get(q);
//              stockMinuteDataModel.dateTime=th0.text().toString();
//              System.out.print('k');
//              System.out.print(stockMinuteDataModel.dateTime+' ');
//              System.out.print('1');
//              count=count+1;
//
//          }
         // stockMinuteDataModel.dateTime=ths.get(0).text().toString();
          //stockMinuteDataModel.Property=ths.get(1).text().toString();


          //System.out.println(th.text());
          // 获取该行的所有td节点
          org.jsoup.select.Elements  tds = tr.select("td");

          for (int j = 0; j < tds.size(); ++j) {
              org.jsoup.nodes.Element td = tds.get(j);
              count=count+1;
              System.out.print(td.text()+' ');
          }
          System.out.println("");
      }
  }
    public void getMinuteDate(String urlhtml,List<StockMinuteDataModel> stockMinuteDataModels){

        Document doc = Jsoup.parse(urlhtml);
        org.jsoup.nodes.Element table = doc.getElementById("datatbl");
        org.jsoup.select.Elements trs = table.select("tr");
        Integer count=0;
        String dateTime="0";
        for (int i = 1; i < trs.size(); ++i) {
            // 获取一个tr
            StockMinuteDataModel stockMinuteDataModel = new StockMinuteDataModel();
            org.jsoup.nodes.Element tr = trs.get(i);
            //获取th节点  成交时间-买卖性质
             org.jsoup.select.Elements ths = tr.select("th");
             stockMinuteDataModel.time = ths.get(0).text().toString();

//            System.out.print(stockMinuteDataModel.dateTime);
//            System.out.print(' ');
//            System.out.print(stockMinuteDataModel.dateTime.substring(0, 5));

            if (!dateTime.equals(ths.get(0).text().substring(0, 5).toString())) {
                dateTime=stockMinuteDataModel.time.substring(0, 5).toString();
                //dateTime=stockMinuteDataModel.time.toString();
               // System.out.println("555:"+stockMinuteDataModel.time.substring(0, 5).toString());
                stockMinuteDataModel.time =ths.get(0).text().toString();

                //stockMinuteDataModel. = ths.get(1).text().toString();
              //  System.out.print(stockMinuteDataModel.time);
                System.out.print(' ');
              //  System.out.print(stockMinuteDataModel.time.substring(0, 5));
                //  System.out.print(stockMinuteDataModel.Property);

                org.jsoup.select.Elements tds = tr.select("td");
                stockMinuteDataModel.price = Float.parseFloat(tds.get(0).text().toString());

                //  System.out.print(stockMinuteDataModel.priceexChange);

               // stockMinuteDataModel.pricePercent = tds.get(1).text().toString();

                // System.out.print(stockMinuteDataModel.pricePercent);

               // stockMinuteDataModel.priceDiff = tds.get(2).text().toString();

                //System.out.print(stockMinuteDataModel.priceDiff);
                stockMinuteDataModel.volume = Integer.parseInt(tds.get(3).text().toString());

                // System.out.print(stockMinuteDataModel.excAmount);

               // stockMinuteDataModel.excMoney = tds.get(4).text().toString();

                //  System.out.print(stockMinuteDataModel.excMoney);
//            for (int j = 0; j < tds.size(); ++j) {
//                org.jsoup.nodes.Element td = tds.get(j);
//                count=count+1;
//                System.out.print(td.text()+' ');
//            }
                System.out.println("");
            }
        }
        }
    public static void main(String path[]) throws Exception {
            String str = "<tr class=\"medium\"><th>09:40:18</th><td>12.62</td><td>+1.53%</td><td>+0.03</td><td>237</td><td>299,094</td><th><h5>买盘</h5></th></tr><tr ><th>09:40:06</th><td>12.59</td><td>+1.29%</td><td>-0.01</td><td>20</td><td>25,180</td><th><h5>买盘</h5></th></tr>";
// 这是正则表达式
            String p ="(<tr.*(?=>)(.|\\n)*</tr>)"; //"(<td[^>]*>[^<]*</td>)";
            Pattern pt = Pattern.compile(p);
            Matcher m = pt.matcher(str);
            Integer count=0;
        List<StockMinuteDataModel> stockMinuteDataModels =new ArrayList<>();
        GetDayStocklData k=new GetDayStocklData();

       for(int i=1;i<14;i++) {

           String url = "http://vip.stock.finance.sina.com.cn/quotes_service/view/vMS_tradedetail.php?symbol=sh603997&page="+i;
           System.out.println(url);
           String urlhtml = k.jsoupFetch(url);
             k.getMinuteDate(urlhtml, stockMinuteDataModels);
       }

        }



}

