package com.ssm.controller;

import com.github.pagehelper.PageInfo;
import com.ssm.domain.Orders;
import com.ssm.service.IOrdersService;
import com.ssm.service.impl.IOrdersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private IOrdersService ordersService;

    //未分页
//    @RequestMapping("/findAll.do")
//    public ModelAndView findAll() throws Exception {
//        List<Orders> ordersList = ordersService.findAll();
//        ModelAndView mv = new ModelAndView();
//        mv.setViewName("orders-list");
//        mv.addObject("ordersList", ordersList);
//        return mv;
//    }
    //分页
    @RequestMapping("/findAll.do")
    public ModelAndView findAll(@RequestParam(name = "page", required = true, defaultValue = "1")
                                        Integer page, @RequestParam(name = "pageSize", required = true, defaultValue = "10") Integer
                                        pageSize) throws Exception {
        List<Orders> ordersList = ordersService.findAllByPage(page, pageSize);
        PageInfo pageInfo = new PageInfo(ordersList);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("orders-page-list");
        mv.addObject("pageInfo", pageInfo);
        return mv;
    }

    @RequestMapping("/findById.do")
    public ModelAndView findById(@RequestParam(name = "id",required = true) String ordersId) throws Exception {
        ModelAndView mv = new ModelAndView();
        Orders order = ordersService.findById(ordersId);
        mv.addObject("orders",order);
        mv.setViewName("orders-show");
        return mv;
    }

}

