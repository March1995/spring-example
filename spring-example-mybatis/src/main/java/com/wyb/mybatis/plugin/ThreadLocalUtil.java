package com.wyb.mybatis.plugin;

/**
 * @author Marcher丶
 * @date 2022-08-24
 **/
public class ThreadLocalUtil {

    public static ThreadLocal<Page> PAGE_INFO = new ThreadLocal<>();

    public static void startPage(int pageNum, int pageSize) {
        Page page = new Page();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        PAGE_INFO.set(page);
    }

    public static Page getPage() {
        return PAGE_INFO.get();
    }

}
