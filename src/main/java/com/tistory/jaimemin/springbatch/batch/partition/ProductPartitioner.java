package com.tistory.jaimemin.springbatch.batch.partition;

import com.tistory.jaimemin.springbatch.batch.domain.ProductVO;
import com.tistory.jaimemin.springbatch.batch.job.api.QueryGenerator;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ProductPartitioner implements Partitioner {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        ProductVO[] productVOs = QueryGenerator.getProducts(dataSource);
        Map<String, ExecutionContext> result = new HashMap<>();
        int number = 0;

        for (int i = 0; i < productVOs.length; i++) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + number++, value);
            value.put("product", productVOs[i]);
        }

        return result;
    }
}
