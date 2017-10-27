package com.stock.servlet.base;

import com.alibaba.fastjson.JSON;
import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.request.StockUserRegisterRequest;
import com.stock.model.response.StockUserRegisterResponse;
import com.stock.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 把输入和返回抽象出来
 */
public abstract class BaseServlet extends HttpServlet {

    public BaseServlet() {

    }

    protected <T> T getRequest(HttpServletRequest request, Class<T> clazz) {
        String data = request.getParameter("data");
        T t = JSON.parseObject(data, clazz);
        return t;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //处理注册
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String data = request.getParameter("data");
        ServiceRequest baseRequest = null;
        if (StringUtil.emptyOrNull(data)) {
            try {
                baseRequest = (ServiceRequest) getActionRequestClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            baseRequest = (ServiceRequest) getRequest(request, getActionRequestClass());
        }

        ServiceResponse baserResponse = null;
        try {
            baserResponse = (ServiceResponse) getActionResponseClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isVerification(baseRequest)) {
            baserResponse.resultMessage = "参数验证不通过";
            baserResponse.resultCode = 500;
        } else {
            try {
                //对moblie做个处理
                servletAction(baseRequest, baserResponse);
                baserResponse.resultMessage = "sucess";
                response.setStatus(200);
            } catch (Exception e) {
                e.printStackTrace();
                baserResponse.resultMessage = e.getMessage();
                response.setStatus(500);
                e.printStackTrace();
            }
        }
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(baserResponse));
        writer.flush();
    }

    protected boolean isVerification(ServiceRequest baseRequest) {
        return true;
    }


    protected abstract Class getActionRequestClass();

    protected abstract Class getActionResponseClass();

    protected abstract void servletAction(ServiceRequest registerRequest, ServiceResponse registerResponse) throws Exception;
}
