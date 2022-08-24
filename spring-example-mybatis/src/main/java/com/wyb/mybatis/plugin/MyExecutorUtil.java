package com.wyb.mybatis.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Marcher丶
 * @date 2022-08-24
 **/
public class MyExecutorUtil {

    private static final List<ResultMapping> EMPTY_RESULT_MAPPING = new ArrayList<ResultMapping>(0);

    public static <E> List<E> pageQuery(Executor executor, MappedStatement ms, Object parameter,
                                        RowBounds rowBounds, ResultHandler resultHandler,
                                        BoundSql boundSql, CacheKey cacheKey, Map<String, Object> params) throws SQLException {

        executor.clearLocalCache();

        //生成分页的缓存 key
        CacheKey pageKey = cacheKey;
        //处理参数对象
        if (params.size() < 0 || params.size() > 2) {
            System.out.println("参数错误");
        }
        //获取sql
        String pageSql = getPageSql(params.size(), boundSql);
        List<ParameterMapping> mappingList = new ArrayList<>();
        mappingList.add(new ParameterMapping.Builder(ms.getConfiguration(), "first_key", Integer.class).build());
        mappingList.add(new ParameterMapping.Builder(ms.getConfiguration(), "second_key", Integer.class).build());
        //实例化新的BoundSql对象
        BoundSql pageBoundSql = new BoundSql(ms.getConfiguration(), pageSql, mappingList, params);
        //执行分页查询
        return executor.query(ms, params, RowBounds.DEFAULT, resultHandler, pageKey, pageBoundSql);
    }


    public static Long executeAutoCount(Executor executor, MappedStatement countMs,
                                        Object parameter, BoundSql boundSql,
                                        RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        //改变MapperStatement中的ID属性
        String countMsId = countMs.getId() + "_COUNT";
        countMs = newCountMappedStatement(countMs, countMsId);
        //创建 count 查询的缓存 key
        CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
        //调用获取count的sql
        String countSql = "select count(0) from student";
        //重新封装BoundSql对象
        BoundSql countBoundSql = new BoundSql(countMs.getConfiguration(), countSql, boundSql.getParameterMappings(), parameter);
        //执行 count 查询
        List<Long> countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        Long count = countResultList.get(0);
        return count;
    }

    public static MappedStatement newCountMappedStatement(MappedStatement ms, String newMsId) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), newMsId, ms.getSqlSource(), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        //count查询返回值int
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), Long.class, EMPTY_RESULT_MAPPING).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    private static String getPageSql(int paramsLength, BoundSql boundSql) {
        StringBuilder str = new StringBuilder();
        str.append(boundSql.getSql());
        if (paramsLength == 1) {
            str.append(" LIMIT ?");
        } else {
            str.append(" LIMIT ?,? ");
        }
        return str.toString();
    }
}
