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
}
