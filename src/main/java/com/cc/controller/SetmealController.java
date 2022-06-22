package com.cc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.common.Result;
import com.cc.dto.SetmealDto;
import com.cc.mapper.SetmealMapper;
import com.cc.pojo.Dish;
import com.cc.pojo.Setmeal;
import com.cc.pojo.SetmealDish;
import com.cc.service.SetmealDishService;
import com.cc.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author cc
 * @since 2022-05-30
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Resource
    private SetmealMapper setmealMapper;
    /**
     * 新增套餐
     * @param setmealDto 用套餐Dto对象接收参数
     * @return
     */
    @PostMapping()
    public Result<String> saveSetmeal(@RequestBody SetmealDto setmealDto) {
        log.info(setmealDto.toString());
        //因为是两张表关联查询，所以MP直接查是不可以的，自己写一个，把两个信息关联起来存储
        setmealService.saveWithDish(setmealDto);
        return Result.success("保存成功");
    }

    /**
     * 套餐模块的分页查询，因为是多表查询，所以直接MP的分页是不行的
     * 所以这里自己写的Mapper文件，一个SQL+标签动态SQL解决的
     * @param page 查第几页
     * @param pageSize 每页条数
     * @param name 模糊查询
     * @return
     */
    @GetMapping("page")
    public Result<Page> pageList(int page, int pageSize, String name) {
        Page page1 = new Page<>();
        //传入是page是从0页开始的，所以要-1
        List<SetmealDish> resultList=setmealMapper.listSetmeal(page-1, pageSize, name);
        //将分页对象setRecords，不然的话前端不识别。
        page1.setRecords(resultList);
        return Result.success(page1);
    }


    /**
     * 拿到套餐信息，回填前端页面，为后续套餐更新做准备，调用Service层写
     * @param id ResultFul风格传入参数，接收套餐id对象，用@PathVariable来接收同名参数
     * @return 返回套餐对象
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> getSetmal(@PathVariable("id") Long id){
        log.info("获取套餐Id"+id);
        SetmealDto setmealDto=setmealService.getSetmealData(id);
        return Result.success(setmealDto);
    }

    /**
     * 删除套餐操作
     * 删除的时候，套餐下的关联关系也需要删除掉，要同时处理两张表
     * @param ids 接收多个id，id可以单个也可以多个，批量删或者单个删都可，毕竟走的都是遍历删除
     * @return
     */
    @DeleteMapping()
    public Result<String> deleteSetmeal(@RequestParam List<Long> ids){
        log.info("ids:{}", ids);
        setmealService.removeWithDish(ids);
        return Result.success("删除成功");
    }


    /**
     * 这俩都是更新状态操作，一个启售一个停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public Result<String> startSale(Long ids){
        Setmeal setmeal=setmealService.getById(ids);
        setmeal.setStatus(0);
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Setmeal::getId, ids);
        setmealService.update(setmeal, lambdaQueryWrapper);
        return Result.success("更新状态为启售");
    }
    @PostMapping("/status/1")
    public Result<String> stopSale(Long ids){
        Setmeal setmeal=setmealService.getById(ids);
        setmeal.setStatus(1);
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Setmeal::getId, ids);
        setmealService.update(setmeal, lambdaQueryWrapper);
        return Result.success("更新状态为停售");
    }
}
