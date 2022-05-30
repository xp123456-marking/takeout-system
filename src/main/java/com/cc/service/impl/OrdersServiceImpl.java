package com.cc.service.impl;

import com.cc.pojo.Orders;
import com.cc.mapper.OrdersMapper;
import com.cc.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2022-05-30
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
