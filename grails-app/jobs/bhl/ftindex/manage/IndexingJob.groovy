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
        while (running) {
            def item = Item.first()
            if (item) {
                indexingService.indexItem(item)
                println "Purging item ${item.id}"
                item.delete(flush: true)
            } else {
                running = false
            }
        }
    }

    void interrupt() throws UnableToInterruptJobException {
        running = false
    }

    void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        println "here in execute 2"
    }
}
