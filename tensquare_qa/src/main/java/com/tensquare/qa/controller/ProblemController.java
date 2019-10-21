package com.tensquare.qa.controller;

import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ysl
 * @Date 2019/9/22 21:37
 * @Description:
 **/

@RestController
@RequestMapping("/problem")
public class ProblemController {


    @Autowired
    private ProblemService problemService;


    /**
     * 根据标签id查询最新问题列表
     * @param labelid
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/newlist/{labelid}/{page}/{size}",method = RequestMethod.GET)
    public Result findNewListByLabelId(@PathVariable String labelid,@PathVariable int page,@PathVariable int size){
        Page<Problem> pageList = problemService.findNewListByLabelId(labelid, page, size);
        PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }


    /**
     * 根据标签ID查询热门问题列表
     * @param labelId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/hotlist/{labelId}/{page}/{size}",method = RequestMethod.GET)
    public Result findHotListByLabelId(@PathVariable String labelId,@PathVariable int page,@PathVariable int size){
        Page<Problem> pageList = problemService.findHotListByLabelId(labelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }

}