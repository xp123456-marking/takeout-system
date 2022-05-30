package com.cc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cc.common.Result;
import com.cc.pojo.Employee;
import com.cc.service.EmployeeService;
import com.cc.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author cc
 * @since 2022-05-30
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * @param request 如果登陆成功把对象放入Session中，方便后续拿取
     * @param employee 利用@RequestBody注解来解析前端传来的Json，同时用对象来封装
     * @return
     */
    @PostMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody Employee employee) {
        String password=employee.getPassword();
        String username = employee.getUsername();
        log.info("登陆");
        //MD5加密
        MD5Util md5Util = new MD5Util();
        password=MD5Util.getMD5(password);
        //通过账户查这个员工对象，这里就不走Service层了
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Employee::getUsername, username);
        Employee empResult=employeeService.getOne(lambdaQueryWrapper);
            //判断用户是否存在
        if (!empResult.getUsername().equals(username)){
            return Result.error("账户不存在");
            //密码是否正确
        }else if (!empResult.getPassword().equals(password)){
            return Result.error("账户密码错误");
            //员工账户状态是否正常，1状态正常，0封禁
        }else if (empResult.getStatus()!=1){
            return Result.error("当前账户正在封禁");
            //状态正常允许登陆
        }else {
            log.info("登陆成功，账户存入session");
            //员工id存入session，
            request.getSession().setAttribute("employ",empResult.getId());
            return Result.success("登陆成功");
        }
    }

}
