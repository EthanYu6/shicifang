package com.tensquare.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tensquare.user.pojo.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.Admin;
import com.tensquare.user.service.AdminService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", adminService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", adminService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Admin> pageList = adminService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Admin>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", adminService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param admin
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param admin
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Admin admin, @PathVariable String id) {
        admin.setId(id);
        adminService.update(admin);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        //获取头信息
        String authHeader = request.getHeader("Authorization");
        if (authHeader==null){
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        if (!authHeader.startsWith("Bearer ")){
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        //提取token
        String token = authHeader.substring(7);
        Claims claims = jwtUtil.parseJWT(token);
        if (claims==null){
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        if (!"admin".equals(claims.get("roles"))){
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }

        adminService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 管理员登录（t_admin表）
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Map map) {
        Admin admin = adminService.login((String) map.get("loginname"), (String) map.get("password"));
        if (admin == null) {
            //生成token
            String token = jwtUtil.createJWT(admin.getId(), admin.getLoginname(), "admin");
            Map<String,Object> rsMap = new HashMap<>();
            rsMap.put("admin",admin);
            rsMap.put("token",token);
            return new Result(true, StatusCode.OK, "管理员登录成功", rsMap);
        }else {
            return new Result(false, StatusCode.ERROR, "管理员登录失败");
        }
    }
}
