package com.ssm.service;

import com.ssm.domain.Orders;

import java.util.List;

public interface IOrdersService {
    public List<Orders> findAll() throws Exception;
    public List<Orders> findAllByPage(int page, int pageSize) throws Exception;
    public Orders findById(String ordersId) throws Exception;


}
