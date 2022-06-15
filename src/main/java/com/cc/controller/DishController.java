package com.cc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.common.Result;
import com.cc.pojo.Dish;
import com.cc.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author cc
 * @since 2022-05-30
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 菜品分页查询展示
     * @param page 从哪页开始查
     * @param pageSize 当前页几条数据
     * @return
     */
    @GetMapping("/page")
    public Result<Page> dishPage(int page,int pageSize){
        //分页构造器
        Page pageInfo = new Page(page, pageSize);
        //分页排序条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.orderByDesc(Dish::getSort);
        //带条件分页查询
        dishService.page(pageInfo, lambdaQueryWrapper);
        //返回数据
        return Result.success(pageInfo);
    }


}
