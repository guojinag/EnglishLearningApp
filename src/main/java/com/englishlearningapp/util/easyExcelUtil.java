package com.englishlearningapp.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.englishlearningapp.dao.WordDAO;
import com.englishlearningapp.model.WordData;

import java.sql.SQLException;
import java.util.List;

public class easyExcelUtil {
    // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
    //@Slf4j
    public static class DemoDataListener implements ReadListener<WordData> {

        /**
         * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
         */
        private static final int BATCH_COUNT = 200;
        /**
         * 缓存的数据
         */
        private List<WordData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        /**
         * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
         */
        private WordDAO wordDAO;

        public DemoDataListener() {
            // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
            wordDAO = new WordDAO();
        }

        /**
         * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
         *
         * @param demoDAO
         */
        public DemoDataListener(WordDAO demoDAO) {
            this.wordDAO = demoDAO;
        }

        /**
         * 这个每一条数据解析都会来调用
         *
         * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
         * @param context
         */
        @Override
        public void invoke(WordData data, AnalysisContext context) {
            //log.info("解析到一条数据:{}", JSON.toJSONString(data));
            cachedDataList.add(data);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (cachedDataList.size() >= BATCH_COUNT) {
                try {
                    saveData();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                // 存储完成清理 list
                cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            }
        }

        /**
         * 所有数据解析完成了 都会来调用
         *
         * @param context
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
            try {
                saveData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("所有数据解析完成！");
        }

        /**
         * 加上存储数据库
         */
        private void saveData() throws SQLException {
            System.out.println(cachedDataList.size()+"条数据，开始存储数据库！");
            wordDAO.save(cachedDataList);
            System.out.println("存储数据库成功！");
        }
    }
}