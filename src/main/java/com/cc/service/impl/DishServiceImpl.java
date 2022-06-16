package com.cc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cc.dto.DishDto;
import com.cc.pojo.Dish;
import com.cc.mapper.DishMapper;
import com.cc.pojo.DishFlavor;
import com.cc.service.DishFlavorService;
import com.cc.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author cc
 * @since 2022-05-30
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    //通过id查询口味信息
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //先把普通信息查出来
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        //搬运
        BeanUtils.copyProperties(dish, dishDto);
        //在通过dish的分类信息查口味List
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> listFlavor=dishFlavorService.list(lambdaQueryWrapper);
        //填充DishDto
        dishDto.setFlavors(listFlavor);
        return dishDto;
    }
}
