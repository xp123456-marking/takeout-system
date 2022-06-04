package com.cc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
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
import java.time.LocalDateTime;

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
            request.getSession().setAttribute("employee",empResult.getId());
            return Result.success("登陆成功");
        }
    }

    /**
     * @param request 删除request作用域中的session对象，就按登陆的request.getSession().setAttribute("employ",empResult.getId());删除employee就行
     * @return
     */
    @PostMapping("/logout")
    public Result login(HttpServletRequest request) {
        //尝试删除
        try {
            request.getSession().removeAttribute("employee");
        }catch (Exception e){
            //删除失败
            return Result.error("登出失败");
        }
        return Result.success("登出成功");
    }


    /**
     * @param httpServletRequest 获取当前操作人员的session id用
     * @param employee 将员工的数据解析为employee对象
     *                 前端json{name: "", phone: "", sex: "", idNumber: "", username: ""}
     * @return
     */
    @PostMapping("")
    public Result addEmployee(HttpServletRequest httpServletRequest,@RequestBody Employee employee) {
        //设置默认密码，顺手加密了
        employee.setPassword(MD5Util.getMD5("123456"));
        //设置修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //账户默认状态0
        employee.setStatus(0);
        //获取当前新增操作人员的id
        Long empId= (Long) httpServletRequest.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        //MP自动CRUD的功能，封装好了save方法
        employeeService.save(employee);
        return Result.success("插入成功");
    }

}
