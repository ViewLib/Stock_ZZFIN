package com.stock.config;

/**
 * Created by xiangleiliu on 2017/9/7.
 */
public class Config {

    //定义的参数

    /**
     * 注意：
     * 测试的前缀使用
     * http://localhost:8080/zzfin/api/
     * 生成的前缀使用
     * http://115.159.31.128:8090/zzfin/api/
     */

    /**
     * 用户注册
     * http://localhost:5389/register?moblie=86_17863333330&clientId=32001000010001
     * http://115.159.31.128:8090/zzfin/api/register?moblie=86_17863333333&clientId=32001000010001
     */
    public String REGISTER = "http://localhost:8080/zzfin/api/user_register?data=%7b%22moblie%22%3a%2286_17863333330%22%2c%22clientId%22%3a%2232001000010001%22%7d";

    /**
     * 用户补全
     * <p>
     * http://115.159.31.128:8090/zzfin/api/completion?userid=10000003&moblie=86_17863333330&nickname=祥磊&area=上海&age=27
     */
    public String Completion = "http://localhost:8080/zzfin/api/user_completion?data=userid%3d10000002%26moblie%3d86_17863333330%26nickname%3d%e5%93%88%e5%93%88%26area%3d%e5%b1%b1%e4%b8%9c%e6%97%a5%e7%85%a7%26age%3d28";

    /**
     * 热搜推荐
     * http://localhost:8080/zzfin/api/search_recommend?userid=10000002&moblie=86_17863333330&nickname=哈哈&area=山东日照&age=28"
     */
    public String Search_HotSearch = "http://localhost:8080/zzfin/api/stock_hotsearch";

    /**
     * top10
     */
    public String Search_RecommendList = "http://localhost:8080/zzfin/api/stock_ranklist";


    /**
     * 排行详情
     */
    public String Search_Rank_Detail = "http://115.159.31.128:8090/zzfin/api/stock_rankdetail?data=%7b%22title%22%3a+%22%E6%B6%A8%E8%B7%8C%E5%B9%85%E5%A4%A7%E4%BA%8E7%25%E7%9A%84%E8%82%A1%E7%A5%A8%22%2c%22serch_relation%22%3a+114%7d";

    /**
     * stock_sync
     */
    public String Search_Sync = "http://localhost:8080/zzfin/api/stock_sync";


    public String GetMinuteAPI = "http://127.0.0.1:8080/zzfin/api/stock_minute?data=%7b%22stockCode%22%3a%22sz300170%22%7d";

    public String FilterList_API = "http://localhost:8080/zzfin/api/stock_rankfilter";//筛选列表

    /**
     * 券商评级
     */
    public String Grade_API = "http://127.0.0.1:8080/zzfin/api/stock_grade?data=%7b%22stockCode%22%3a%22sz300170%22%7d";

    /**
     * 公司介绍
     */
    public String API_Company_Desc = "http://115.159.31.128:8090/zzfin/api/stock_info?data=%7B%22serviceCode%22%3A2005%2C%22stockCode%22%3A%22sz300170%22%2C%22versionCode%22%3A1%7D";

    /**
     * 财务数据
     */
    public String Finicial_API = "http://115.159.31.128:8090/zzfin/api/stock_finicial?data=%7B%22serviceCode%22%3A2007%2C%22stockCode%22%3A%22sz300170%22%2C%22versionCode%22%3A1%7D";


    /**
     * 用户信息
     */
    public String UserList_API = "http://localhost:8080/zzfin/back/user_manager";


    /**
     * 重大消息
     */
    public String NEW_API = "http://127.0.0.1:8080/zzfin/api/stock_event?data=%7B%22serviceCode%22%3A2010%2C%22stockCode%22%3A%22sz300170%22%2C%22type%22%3A2%2C%22versionCode%22%3A1%7D";

    /**
     * 同行比较
     */
    public String COMPARE_API = "http://127.0.0.1:8080/zzfin/api/stock_compare?data=%7B%22serviceCode%22%3A2010%2C%22stockCode%22%3A%22sz300170%22%2C%22type%22%3A2%2C%22versionCode%22%3A1%7D";


    
}
