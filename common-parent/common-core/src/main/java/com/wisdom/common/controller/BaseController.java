package com.wisdom.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.common.entity.BaseEntity;
import com.wisdom.common.service.BaseService;
import com.wisdom.config.dto.ResultDto;
import com.wisdom.config.enums.HttpEnum;
import com.wisdom.config.enums.ResultEnum;
import com.wisdom.tools.datetime.DateUtilByZoned;
import com.wisdom.tools.map.MapUtil;
import com.wisdom.tools.mybatisplus.MybatisplusUtil;
import com.wisdom.tools.string.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @description 通用控制层
 * @date 2021/8/2 13:26 星期一
 */
@Slf4j
public class BaseController<M extends BaseService<T>, T> {
    @Autowired
    protected M baseService;

    protected T baseEntity;

    protected Class<T> entityClass = currentModelClass();

    protected Class<T> serviceClass = currenServiceClass();

    @ResponseBody
    @PostMapping("/findBySelective")
    @ApiOperation(value = "根据条件")
    public ResultDto<List<T>> findBySelective(@RequestBody Map<String, Object> paramsMap) {
        List<T> entitys = baseService.selectBySelective(paramsMap);
        return new ResultDto<>(HttpEnum.OK, entitys);
    }

//    @ResponseBody
//    @PostMapping("/findPageBySelective")
//    @ApiOperation(value = "根据条件")
//    public ResultDto<List<T>> findPageBySelective(
//            @RequestParam(value = "curPage", defaultValue = "1", required = false) Serializable curPage,
//            @RequestParam(value = "sizePage", defaultValue = "30", required = false) Serializable sizePage,
//            @RequestBody Map<String, Object> paramsMap) {
//        Map<String, Object> queryMap = MapUtil.lowerCamelToLowerUnderscore(paramsMap);
//        Page<T> userPage = new Page<>(curPage, sizePage);
////        List<T> entitys = baseService.selectBySelective(queryMap);
//        baseService.page(userPage);
//        return new ResultDto<>(HttpEnum.OK, entitys);
//    }

    @ResponseBody
    @PostMapping("/findById")
    @ApiOperation(value = "根据 ID 查询")
    public ResultDto<T> findById(@RequestParam(value = "id") Serializable id) {
        T baseEntity = baseService.getById(id);
        return new ResultDto<>(HttpEnum.OK, baseEntity);
    }


    @ResponseBody
    @PostMapping("/findOne")
    @ApiOperation(value = "根据实体类条件,查询一条记录", notes = "结果集，如果是多个会抛出异常")
    public ResultDto<T> findOne(@RequestBody T entity) {
        T baseEntity = baseService.getOne(MybatisplusUtil.createWrapper(entity));
        return new ResultDto<>(HttpEnum.OK, baseEntity);
    }

    @ResponseBody
    @PostMapping("/findListByIds")
    @ApiOperation(value = "查询(根据ID 批量查询)")
    public ResultDto<List<T>> findListByIds(@RequestBody Collection<? extends Serializable> ids) {
        List<T> entitys = baseService.listByIds(ids);
        return new ResultDto<>(HttpEnum.OK, entitys);
    }

    @ResponseBody
    @PostMapping("/findListByMap")
    @ApiOperation(value = "查询(根据条件)")
    public ResultDto<List<T>> findListByMap(@RequestBody Map<String, Object> paramsMap) {
        List<T> entitys = baseService.listByMap(paramsMap);
        return new ResultDto<>(HttpEnum.OK, entitys);
    }

    @ResponseBody
    @PostMapping("/save")
    @ApiOperation(value = "插入一条记录(选择字段,策略插入)")
    public ResultDto<T> save(@RequestBody T entity) {
        String nowDateUnMilli = DateUtilByZoned.getNowDateUnMilli();
        if (entity instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) entity;
            baseEntity.setCreateDateTime(nowDateUnMilli);
            baseEntity.setUpDateTime(nowDateUnMilli);
            boolean bool = baseService.save(entity);
            if (bool) {
                return new ResultDto<>(HttpEnum.OK, entity);
            }
        }
        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/saveBatch")
    @ApiOperation(value = "插入(批量)")
    public ResultDto<Collection<T>> saveBatch(@RequestBody Collection<T> collection) {
        if (collection instanceof List) {
            List<T> entityList = (List) collection;

            collection = createAndUpDataSet(entityList);
        }

        boolean bool = baseService.saveBatch(collection);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, collection);
        }
        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/saveBatchAndSize")
    @ApiOperation(value = "插入(批量)")
    public ResultDto<Collection<T>> saveBatchAndSize(@RequestBody Map<String, Object> paramsMap) {
        int batchSize = Integer.parseInt(String.valueOf(paramsMap.get("batchSize")));
        Object data = paramsMap.get("data");

        List<T> entityList = JSONObject.parseArray(JSON.toJSONString(data), entityClass);

        boolean bool = baseService.saveBatch(createAndUpDataSet(entityList), batchSize);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, entityList);
        }
        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value = "批量修改或插入")
    public ResultDto<Collection<T>> saveOrUpdateBatch(@RequestBody Collection<T> collection) {
        if (collection instanceof List) {
            List<T> entityList = (List) collection;

            collection = createAndUpDataSet(entityList);
        }

        boolean bool = baseService.saveOrUpdateBatch(collection);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, collection);
        }
        return new ResultDto<>(ResultEnum.FAIL);
    }


    @ResponseBody
    @PostMapping("/saveOrUpdateBatchAndSize")
    @ApiOperation(value = "批量修改或插入")
    public ResultDto<Collection<T>> saveOrUpdateBatchAndSize(@RequestBody Map<String, Object> paramsMap) {
        int batchSize = Integer.parseInt(String.valueOf(paramsMap.get("batchSize")));
        Object data = paramsMap.get("data");

        List<T> entityList = JSONObject.parseArray(JSON.toJSONString(data), entityClass);

        boolean bool = baseService.saveOrUpdateBatch(createAndUpDataSet(entityList), batchSize);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, entityList);
        }
        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/removeById")
    @ApiOperation(value = "根据 ID 删除", notes = "物理删除")
    public ResultDto<Collection<T>> remoeById(@RequestParam(value = "id") Serializable id) {
        boolean bool = baseService.removeById(id);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK);
        }
        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/removeByMap")
    @ApiOperation(value = "根据条件，删除记录", notes = "物理删除")
    public ResultDto<List<T>> removeByMap(@RequestBody Map<String, Object> paramsMap) {
        boolean bool = baseService.removeByMap(paramsMap);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK);
        }
        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/remove")
    @ApiOperation(value = "根据条件，删除记录", notes = "物理删除")
    public ResultDto<List<T>> remove(@RequestBody T entity) {
        boolean bool = baseService.remove(MybatisplusUtil.createWrapper(entity));
        if (bool) {
            return new ResultDto<>(HttpEnum.OK);
        }
        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/removeByIds")
    @ApiOperation(value = "删除(根据ID 批量删除)", notes = "物理删除")
    public ResultDto<List<T>> removeByIds(@RequestBody Collection<? extends Serializable> ids) {
        boolean bool = baseService.removeByIds(ids);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK);
        }
        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/updateById")
    @ApiOperation(value = "根据 ID 选择更新")
    public ResultDto<T> updateById(@RequestBody T entity) {
        String nowDateUnMilli = DateUtilByZoned.getNowDateUnMilli();

        if (entity instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) entity;
            baseEntity.setUpDateTime(nowDateUnMilli);
            boolean bool = baseService.updateById(entity);
            if (bool) {
                return new ResultDto<>(HttpEnum.OK);
            }
        }

        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/update")
    @ApiOperation(value = "根据条件,更新记录 ")
    public ResultDto<T> update(@RequestBody T entity) {
        String nowDateUnMilli = DateUtilByZoned.getNowDateUnMilli();

        if (entity instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) entity;
            baseEntity.setUpDateTime(nowDateUnMilli);
            boolean bool = baseService.update(MybatisplusUtil.createWrapper(entity));
            if (bool) {
                return new ResultDto<>(HttpEnum.OK);
            }
        }

        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/updateByWrapper")
    @ApiOperation(value = "根据条件，更新记录")
    public ResultDto<T> updateByWrapper(@RequestBody T entity) {
        String nowDateUnMilli = DateUtilByZoned.getNowDateUnMilli();

        if (entity instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) entity;
            baseEntity.setUpDateTime(nowDateUnMilli);
            boolean bool = baseService.update(entity, MybatisplusUtil.createWrapper(entity));
            if (bool) {
                return new ResultDto<>(HttpEnum.OK);
            }
        }

        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/updateBatchById")
    @ApiOperation(value = "根据ID 批量更新")
    public ResultDto<Collection<T>> updateBatchById(@RequestBody Collection<T> collection) {
        if (collection instanceof List) {
            List<T> entityList = (List) collection;
            collection = createAndUpDataSet(entityList);
        }

        boolean bool = baseService.updateBatchById(collection);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, collection);
        }
        return new ResultDto<>(ResultEnum.FAIL);
    }

    @ResponseBody
    @PostMapping("/updateBatchByIdAndSize")
    @ApiOperation(value = "根据ID 批量更新")
    public ResultDto<Collection<T>> updateBatchByIdAndSize(@RequestBody Map<String, Object> paramsMap) {
        int batchSize = Integer.parseInt(String.valueOf(paramsMap.get("batchSize")));
        Object data = paramsMap.get("data");

        List<T> entityList = JSONObject.parseArray(JSON.toJSONString(data), entityClass);

        boolean bool = baseService.updateBatchById(createAndUpDataSet(entityList), batchSize);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, entityList);
        }
        return new ResultDto<>(ResultEnum.FAIL);
    }

    private List<T> createAndUpDataSet(List<T> entityList) {
        for (int i = 0; i < entityList.size(); i++) {
            String nowDateUnMilli = DateUtilByZoned.getNowDateUnMilli();
            T entity = entityList.get(i);
            if (entity instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) entity;
                if (StringUtil.isNotBlank(String.valueOf(baseEntity.getId()))) {
                    baseEntity.setUpDateTime(nowDateUnMilli);
                } else {
                    baseEntity.setCreateDateTime(nowDateUnMilli);
                    baseEntity.setUpDateTime(nowDateUnMilli);
                }
                entityList.set(i, (T) baseEntity);
            }
        }
        return entityList;
    }

    public M getBaseService() {
        return baseService;
    }

    public T getBaseEntity() {
        return baseEntity;
    }

    protected Class<T> currenServiceClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
    }

    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

}
