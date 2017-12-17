package com.stock.controller.sql;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.stock.controller.sql.model.*;
import com.stock.controller.user.model.SearchRankPageCountRequest;
import com.stock.controller.user.model.SearchRankUpdateRequest;
import com.stock.model.viewmodel.StockSearchRankViewModel;
import com.stock.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Created by 杨蕾 on 2017/12/9.
 */
@RestController
@RequestMapping({"/sql"})
public class SqlManagerRestController {
    Logger logger = LoggerFactory.getLogger(SqlManagerRestController.class);
    private UserService userService = UserService.getInstance();

    @RequestMapping({"/search"})
    @ResponseBody
    public SearchSqlResponse searchUserPost(@RequestBody SearchSqlRequest request) {
        SearchSqlResponse result = new SearchSqlResponse();

        int pageIndex = request.getOffset() / request.getLimit() + 1;
        SearchRankPageCountRequest daoRequest = new SearchRankPageCountRequest(pageIndex, request.getLimit());
        List<StockSearchRankViewModel> daoResponse = userService.getSearchRankList(daoRequest);
        if (daoResponse == null || daoResponse.size() <= 0) {
            return result;
        }

        List<SearchRankModel> rows = Lists.transform(daoResponse, new Function<StockSearchRankViewModel, SearchRankModel>() {
            @Override
            public SearchRankModel apply(StockSearchRankViewModel viewModel) {
                SearchRankModel item = new SearchRankModel();
                item.setSearchId(viewModel.search_id);
                item.setSearchDesc(viewModel.search_desc);
                item.setSearchRelation(viewModel.search_relation);
                item.setSearchTitle(viewModel.search_title);
                item.setSearchType(viewModel.search_type);
                item.setSearchWeight(viewModel.search_weight);
                item.setShowType(viewModel.show_type);
                item.setRankSql(viewModel.stockRankSQLViewModel == null ? "" : viewModel.stockRankSQLViewModel.rank_sql);
                return item;
            }
        });

        int totalCount = userService.selectStockSearchRankCount();
        result.setRows(rows);
        result.setTotal(totalCount);
        return result;
    }

    @RequestMapping({"/update"})
    @ResponseBody
    public Object updateSql(@RequestBody SqlRequest request) {
        SqlResponse response = new SqlResponse();
        response.setResultCode(500);
        response.setShowMessage("更新失败，请稍后重试");

        if(request == null || request.getSearchId() <= 0){
            response.setResultCode(400);
            response.setShowMessage("请求不合法,SearchId不正确");
            return response;
        }

        SearchRankUpdateRequest daoRequest = this.generateDaoRequest(request);
        boolean isSuccess = userService.updateStockSearchRank(daoRequest);
        if (isSuccess) {
            response.setResultCode(200);
            response.setShowMessage("更新成功");
        }

        return response;
    }

    @RequestMapping({"/insert"})
    @ResponseBody
    public Object insertUser(@RequestBody SqlRequest request) {
        SqlResponse response = new SqlResponse();
        response.setResultCode(500);
        response.setShowMessage("新增失败，请稍后重试");

        if(request == null){
            response.setResultCode(400);
            response.setShowMessage("请求不合法，请填写必要的信息");
            return response;
        }

        SearchRankUpdateRequest daoRequest = this.generateDaoRequest(request);
        boolean isSuccess = userService.updateStockSearchRank(daoRequest);
        if (isSuccess) {
            response.setResultCode(200);
            response.setShowMessage("新增成功");
        }

        return response;
    }

    private SearchRankUpdateRequest generateDaoRequest(SqlRequest request){
        SearchRankUpdateRequest daoRequest = new SearchRankUpdateRequest();
        daoRequest.search_id = request.getSearchId();
        daoRequest.show_type = request.getShowType();
        daoRequest.search_type = request.getSearchType();
        daoRequest.search_title = request.getSearchTitle();
        daoRequest.search_desc = request.getSearchDesc();
        daoRequest.search_relation = request.getSearchRelation();
        daoRequest.search_weight = request.getSearchWeight();
        daoRequest.rank_sql = request.getRankSql();

        return daoRequest;
    }
}
