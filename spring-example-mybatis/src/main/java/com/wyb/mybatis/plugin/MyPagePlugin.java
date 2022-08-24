package com.wyb.mybatis.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * https://juejin.cn/post/7131902198585229320
 *
 * @author Marcher丶
 * @date 2022-08-24
 **/
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class MyPagePlugin implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        //由于逻辑关系，只会进入一次
        if (args.length == 4) {
            //4 个参数时
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            //6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }

        /** 其实在这之上的代码都是拷贝PageHelper源码来的，下面才是重头戏，上面都是获取一些必要的参数 **/
        /**
         * 下面4行代码暂时只需要知道是用来传参数的就行，分页不是需要
         * 二个参数嘛 一个是当前页，一个是数量
         */
        Page page = ThreadLocalUtil.getPage();
        Map<String, Object> params = new HashMap<>();
        params.put("first_key", page.getPageNum());
        params.put("second_key", page.getPageSize());

        /**
         * 重点：获取数据库记录总数
         */
        //统计总数
        Long count = MyExecutorUtil.executeAutoCount(executor, ms, parameter, boundSql, rowBounds, resultHandler);

        /**
         * 重点：分页查询
         */
        List<Object> objects = MyExecutorUtil.pageQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql, cacheKey, params);
        return objects;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
