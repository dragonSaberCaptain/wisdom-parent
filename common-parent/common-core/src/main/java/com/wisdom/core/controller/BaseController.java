package com.wisdom.core.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import com.wisdom.base.dto.ResultDto;
import com.wisdom.base.enums.DateTimeEnum;
import com.wisdom.base.enums.HttpEnum;
import com.wisdom.base.enums.ResultEnum;
import com.wisdom.core.entity.BaseEntity;
import com.wisdom.core.service.BaseService;
import com.wisdom.tools.datetime.DateUtilByZoned;
import com.wisdom.tools.map.MapUtil;
import com.wisdom.tools.object.ObjectUtil;
import com.wisdom.tools.string.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * 通用控制层
 *
 * @author captain
 * @version 1.0
 * @dateTime 2021/8/2 13:26 星期一
 */
@Slf4j
public class BaseController<M extends BaseService<T>, T extends BaseEntity> {
    @Autowired
    protected M baseService;

    protected T baseEntity;

    protected Class<T> entityClass = currentModelClass();

    protected Class<T> serviceClass = currenServiceClass();

    /**
     * 将实体类解析成 Mybatisplus 需要的QueryWrapper类
     *
     * @param entity 实体类
     * @author captain
     * @datetime 2021-10-27 10:12:37
     */
    public static <T> QueryWrapper<T> createWrapper(T entity) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        Map<String, Object> fieldMap = ObjectUtil.getDeclaredField(entity);
        for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null || "serialVersionUID".equalsIgnoreCase(key) || "SERIAL_VERSION_U_I_D".equalsIgnoreCase(key)) {
                continue;
            }
            //驼峰转大写下划线, userName -> USER_NAME
            String dbKey = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, key);

            if ("CREATE_DATE_TIME_START".equalsIgnoreCase(dbKey)) {
                wrapper.ge("CREATE_TIME", value);
            } else if ("CREATE_DATE_TIME_END".equalsIgnoreCase(dbKey)) {
                wrapper.le("CREATE_TIME", value);
            } else if ("UP_DATE_TIME_START".equalsIgnoreCase(dbKey)) {
                wrapper.ge("UPDATE_TIME", value);
            } else if ("UP_DATE_TIME_END".equalsIgnoreCase(dbKey)) {
                wrapper.le("UPDATE_TIME", value);
            } else {
                wrapper.eq(dbKey, value);
            }
        }
        return wrapper;
    }

    /**
     * 根据条件,查询列表
     */
    @ResponseBody
    @PostMapping("/findBySelective")
    public ResultDto<List<T>> findBySelective(@RequestBody Map<String, Object> paramsMap) {
        List<T> entitys = baseService.selectBySelective(paramsMap);
        return new ResultDto<>(HttpEnum.OK, entitys);
    }

    /**
     * 根据条件,分页查询,可自定义排序
     */
    @ResponseBody
    @PostMapping("/findPageBySelective")
    public ResultDto<IPage<T>> findPageBySelective(
            @RequestParam(value = "curPage", defaultValue = "1", required = false) long curPage,
            @RequestParam(value = "sizePage", defaultValue = "30", required = false) long sizePage,
            @RequestParam(value = "orderBy", required = false) List<OrderItem> orderByList,
            @RequestBody Map<String, Object> paramsMap) {
        List<T> entitys = baseService.selectBySelective(paramsMap);
        Page<T> userPage = new Page<>(curPage, sizePage);
        userPage.setOrders(orderByList);
        userPage.setRecords(entitys);
        return new ResultDto<>(HttpEnum.OK, userPage);
    }

    /**
     * 根据ID,查询
     */
    @ResponseBody
    @GetMapping("/findById")
    public ResultDto<T> findById(@RequestParam(value = "id") Serializable id) {
        T baseEntity = baseService.getById(id);
        return new ResultDto<>(HttpEnum.OK, baseEntity);
    }

    /**
     * 根据实体类条件,查询一条记录,多条抛出异常
     */
    @ResponseBody
    @PostMapping("/findOne")
    public ResultDto<T> findOne(@RequestBody T entity) {
        T baseEntity = baseService.getOne(createWrapper(entity));
        return new ResultDto<>(HttpEnum.OK, baseEntity);
    }

    /**
     * 根据批量ID,查询
     */
    @ResponseBody
    @PostMapping("/findListByIds")
    public ResultDto<List<T>> findListByIds(@RequestBody Collection<? extends Serializable> ids) {
        List<T> entitys = baseService.listByIds(ids);
        return new ResultDto<>(HttpEnum.OK, entitys);
    }

    /**
     * 根据 columnMap 条件,查询
     */
    @ResponseBody
    @PostMapping("/findListByMap")
    public ResultDto<List<T>> findListByMap(@RequestBody Map<String, Object> paramsMap) {
        Map<String, Object> queryMap = MapUtil.lowerCamelToLowerUnderscore(paramsMap);
        List<T> entitys = baseService.listByMap(queryMap);
        return new ResultDto<>(HttpEnum.OK, entitys);
    }

    /**
     * 根据条件,分页查询
     */
    @ResponseBody
    @PostMapping("/findPageListByMap")
    public ResultDto<IPage<T>> findPageListByMap(
            @RequestParam(value = "curPage", defaultValue = "1", required = false) long curPage,
            @RequestParam(value = "sizePage", defaultValue = "30", required = false) long sizePage,
            @RequestBody Map<String, Object> paramsMap) {
        T entityModel = JSON.parseObject(JSON.toJSONString(paramsMap), entityClass);
        QueryWrapper<T> wrapper = createWrapper(entityModel);
        Page<T> userPage = new Page<>(curPage, sizePage);
        IPage<T> entityPage = baseService.page(userPage, wrapper);
        return new ResultDto<>(HttpEnum.OK, entityPage);
    }

    /**
     * 根据实体对象,插入
     */
    @ResponseBody
    @PostMapping("/save")
    public ResultDto<T> save(@RequestBody T entity) {
        String nowDateUnMilli = DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_MILLI_UN);
        if (null != entity) {
            entity.setCreateDateTime(nowDateUnMilli);
            entity.setUpDateTime(nowDateUnMilli);
            boolean bool = baseService.save(entity);
            if (bool) {
                return new ResultDto<>(HttpEnum.OK, entity);
            }
        }
        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 根据实体对象,批量插入
     */
    @ResponseBody
    @PostMapping("/saveBatch")
    public ResultDto<Collection<T>> saveBatch(@RequestBody Collection<T> collection) {
        collection = createAndUpDataSet(collection);
        boolean bool = baseService.saveBatch(collection);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, collection);
        }
        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 批量插入,可定义批次大小
     */
    @ResponseBody
    @PostMapping("/saveBatchAndSize")
    public ResultDto<Collection<T>> saveBatchAndSize(
            @RequestParam(value = "batchSize", defaultValue = "1000", required = false) int batchSize,
            @RequestBody Map<String, Object> paramsMap) {

        List<T> entityList = JSON.parseArray(JSON.toJSONString(paramsMap), entityClass);

        boolean bool = baseService.saveBatch(createAndUpDataSet(entityList), batchSize);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, entityList);
        }
        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 根据id有无,批量修改或插入
     */
    @ResponseBody
    @PostMapping("/saveOrUpdateBatch")
    public ResultDto<Collection<T>> saveOrUpdateBatch(@RequestBody Collection<T> collection) {
        collection = createAndUpDataSet(collection);
        boolean bool = baseService.saveOrUpdateBatch(collection);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, collection);
        }
        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 批量修改或插入,可定义批次大小
     */
    @ResponseBody
    @PostMapping("/saveOrUpdateBatchAndSize")
    public ResultDto<Collection<T>> saveOrUpdateBatchAndSize(
            @RequestParam(value = "batchSize", defaultValue = "1000", required = false) int batchSize,
            @RequestBody Map<String, Object> paramsMap) {

        List<T> entityList = JSON.parseArray(JSON.toJSONString(paramsMap), entityClass);

        boolean bool = baseService.saveOrUpdateBatch(createAndUpDataSet(entityList), batchSize);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, entityList);
        }
        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 根据ID,删除（物理删除）
     */
    @ResponseBody
    @DeleteMapping("/removeById")
    public ResultDto<Collection<T>> remoeById(@RequestParam(value = "id") Serializable id) {
        boolean bool = baseService.removeById(id);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK);
        }
        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 根据 columnMap 条件，删除记录 （物理删除）
     */
    @ResponseBody
    @PostMapping("/removeByMap")
    public ResultDto<List<T>> removeByMap(@RequestBody Map<String, Object> paramsMap) {
        Map<String, Object> queryMap = MapUtil.lowerCamelToLowerUnderscore(paramsMap);
        boolean bool = baseService.removeByMap(queryMap);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK);
        }
        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 根据条件，删除记录 （物理删除）
     */
    @ResponseBody
    @PostMapping("/remove")
    public ResultDto<List<T>> remove(@RequestBody T entity) {
        boolean bool = baseService.remove(createWrapper(entity));
        if (bool) {
            return new ResultDto<>(HttpEnum.OK);
        }
        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 根据ID,批量删除 （物理删除）
     */
    @ResponseBody
    @PostMapping("/removeByIds")
    public ResultDto<List<T>> removeByIds(@RequestBody Collection<? extends Serializable> ids) {
        boolean bool = baseService.removeByIds(ids);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK);
        }
        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 根据ID,更新
     */
    @ResponseBody
    @PostMapping("/updateById")
    public ResultDto<T> updateById(@RequestBody T entity) {
        String nowDateUnMilli = DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_MILLI_UN);

        if (null != entity) {
            entity.setUpDateTime(nowDateUnMilli);
            boolean bool = baseService.updateById(entity);
            if (bool) {
                return new ResultDto<>(HttpEnum.OK);
            }
        }

        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 根据条件,更新记录
     */
    @ResponseBody
    @PostMapping("/update")
    public ResultDto<T> update(@RequestBody T entity) {
        return updateByWrapper(entity);
    }

    /**
     * 根据条件，更新记录
     */
    @ResponseBody
    @PostMapping("/updateByWrapper")
    public ResultDto<T> updateByWrapper(@RequestBody T entity) {
        String nowDateUnMilli = DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_MILLI_UN);

        if (null != entity) {
            entity.setUpDateTime(nowDateUnMilli);
            boolean bool = baseService.update(entity, createWrapper(entity));
            if (bool) {
                return new ResultDto<>(HttpEnum.OK);
            }
        }

        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 根据ID,批量更新
     */
    @ResponseBody
    @PostMapping("/updateBatchById")
    public ResultDto<Collection<T>> updateBatchById(@RequestBody Collection<T> collection) {
        collection = createAndUpDataSet(collection);
        boolean bool = baseService.updateBatchById(collection);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, collection);
        }
        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 根据ID,批量更新,可定义批次大小
     */
    @ResponseBody
    @PostMapping("/updateBatchByIdAndSize")
    public ResultDto<Collection<T>> updateBatchByIdAndSize(
            @RequestParam(value = "batchSize", defaultValue = "1000", required = false) int batchSize,
            @RequestBody Map<String, Object> paramsMap) {
        List<T> entityList = JSON.parseArray(JSON.toJSONString(paramsMap), entityClass);

        boolean bool = baseService.updateBatchById(createAndUpDataSet(entityList), batchSize);
        if (bool) {
            return new ResultDto<>(HttpEnum.OK, entityList);
        }
        return new ResultDto<>(ResultEnum.RESULT_ENUM_1002);
    }

    /**
     * 目前只处理了List集合，其他集合后续可以添加
     * 目的:如果id存在,则设置更新时间，反之，则设置创建时间和更新时间
     *
     * @param collection 集合
     * @author captain
     * @datetime 2021-09-28 14:02:39
     */
    private List<T> createAndUpDataSet(Collection<T> collection) {
        if (collection instanceof List<T> entityList) {//处理List集合
            String nowDateUnMilli = DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_MILLI_UN);
            for (int i = 0; i < entityList.size(); i++) {
                T entity = entityList.get(i);
                if (null != entity) { //处理继承BaseEntity的对象
                    if (StringUtil.isNotBlank(String.valueOf(entity.getId()))) {
                        entity.setUpDateTime(nowDateUnMilli);
                    } else {
                        entity.setCreateDateTime(nowDateUnMilli);
                        entity.setUpDateTime(nowDateUnMilli);
                    }
                    entityList.set(i, entity);
                }
            }
            return entityList;
        }
        return null;
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

    ///**
    // * 得到request对象
    // */
    //public HttpServletRequest getRequest() {
    //    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
    //            .getRequest();
    //    return request;
    //}
    public M getBaseService() {
        return baseService;
    }
}
