package com.tistory.jaimemin.springbatch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CustomTasklet implements Tasklet {

    private long sum;

    private Object lock = new Object();

    /**
     * 공유할 데이터가 있을 경우 동시성 처리 필요
     *
     * @param stepContribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution stepContribution
            , ChunkContext chunkContext) throws Exception {
        synchronized (lock) { // 동기화를 위해
            for (int i = 0; i < (int) Math.pow(10, 8); i++) {
                sum++;
            }

            System.out.println(String.format("%s has been executed on thread %s"
                    , chunkContext.getStepContext().getStepName()
                    , Thread.currentThread().getName()));
            System.out.println(String.format("sum : %d", sum));
        }

        return RepeatStatus.FINISHED;
    }
}
