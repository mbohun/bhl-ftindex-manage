package bhl.ftindex.manage

import org.quartz.InterruptableJob
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.quartz.UnableToInterruptJobException

class IndexingJob implements InterruptableJob {

    def indexingService

    boolean running

    static triggers = {
      simple repeatInterval: 1000 * 60 * 60, startDelay: 1000 * 60 * 60
    }

    def group = "BHLJobs"
    def concurrent = false

    def execute() {
        running = true
        println ("Indexing Job Started")
        while (running) {
            def item = Item.findByStatusIsNullOrStatus(ItemStatus.Pending)
            if (item) {
                try {
                    indexingService.indexItem(item)
                    item.delete(flush: true)
                } catch (Exception ex) {
                    item.status = ItemStatus.Error
                    item.save(flush: true)
                }
            } else {
                item = Item.findByStatusAndRetryCountLessThan(ItemStatus.Error, 5)
                if (item) {
                    try {
                        indexingService.indexItem(item)
                        item.delete(flush: true)
                    } catch (Exception ex) {
                        item.retryCount++
                        item.save(flush: true)
                    }
                } else {
                    running = false
                }
            }
        }
        println ("Indexing Job Finished")
    }

    void interrupt() throws UnableToInterruptJobException {
        running = false
    }

    void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    }

}
