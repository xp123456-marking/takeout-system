package com.cc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.common.Result;
import com.cc.pojo.Category;
import com.cc.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 *
 * @author cc
 * @since 2022-05-30
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增菜品分类
     * @param category 接收菜品分类对象
     * @return
     */
    @PostMapping("")
    public Result<Category> save(@RequestBody Category category){
        log.info("新增菜品分类");
        categoryService.save(category);
        return Result.success(category);
    }

    /**
     * 菜品列表功能
     * @param page 第几页
     * @param pageSize 每页查几条数据
     * @return
     */
    @GetMapping("page")
    public Result<Page> page(int page,int pageSize){
        //分页构造器
        Page pageInfo = new Page(page, pageSize);
        //过滤条件
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.orderByDesc(Category::getSort);
        categoryService.page(pageInfo,lambdaQueryWrapper);
        return Result.success(pageInfo);
    }


    @DeleteMapping()
    public Result<String> delCategory(Long id){
        categoryService.removeCategory(id);
        return Result.success("删除成功");
    }

}
