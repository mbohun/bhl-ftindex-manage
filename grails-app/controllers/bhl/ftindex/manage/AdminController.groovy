package bhl.ftindex.manage

import org.hibernate.FlushMode
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

class AdminController {

    def sessionFactory
    def settingService
    def jobManagerService
    def BHLService

    def index() {}

    def items() {
        def errorItems = Item.findAllByStatus(ItemStatus.Error, [max: params.max ?: 10, offset: params.offset ?: 0])
        def totalCount = Item.countByStatus(ItemStatus.Error)
        [errorItems: errorItems, totalCount: totalCount]
    }

    def deleteAllItems() {
        Item.executeUpdate("delete Item");
        redirect(action:'items')
    }

    def deleteItem() {
        def item = Item.get(params.int("id"))
        if (item) {
            item.delete()
        }
        redirect(action:'items')
    }

    def loadItems() {
    }

    def loadSingleItem() {
    }

    def uploadSingleItem() {
        def itemId = params.itemId
        if (!itemId) {
            flash.errorMessage = "You must specify an item id!"
            redirect(action:'loadSingleItem')
            return
        }

        def existing = Item.findByItemId(itemId)
        if (existing) {
            flash.errorMessage = "This item already exists in the items table"
            redirect(action:'loadSingleItem')
            return
        }

        def itemMetaData = this.BHLService.getItemMetaData(itemId, false, false)
        if (!itemMetaData) {
            flash.errorMessage = "This item already exists in the items table"
            redirect(action:'loadSingleItem')
            return
        }

        def md = itemMetaData.Result

        def item = new Item(itemId: itemId, internetArchiveId: md?.SourceIdentifier, primaryTitleId: md?.PrimaryTitleID, volume: md?.Volume, title: md?.Title ?: '')
        item.save(failOnError: true)

        redirect(action:'items')
    }

    def uploadItems() {
        if(request instanceof MultipartHttpServletRequest) {
            MultipartFile f = ((MultipartHttpServletRequest) request).getFile('filename')
            if (f != null) {
                def allowedMimeTypes = ['text/csv', 'application/octet-stream']
                if (!allowedMimeTypes.contains(f.getContentType())) {
                    flash.errorMessage = "The file must be one of: ${allowedMimeTypes}"
                    redirect(action:'samplingUnits')
                    return;
                }
                int lineCount = 0
                int importCount = 0

                sessionFactory.currentSession.setFlushMode(FlushMode.MANUAL)

                try {
                    f.inputStream.toCsvReader([skipLines: 1]).eachLine { String[] tokens ->
                        def item = new Item(itemId: tokens[0], primaryTitleId: tokens[1], internetArchiveId: tokens[2], title:tokens[3], volume: tokens[4])
                        item.save(failOnError: true)
                        lineCount++
                        if (lineCount % 2000 == 0) {
                            // Doing this significantly speeds up imports...
                            sessionFactory.currentSession.flush()
                            sessionFactory.currentSession.clear()
                            println lineCount
                        }

                    }
                } catch (Exception ex) {
                    println ex.getNextException()
                } finally {
                    sessionFactory.currentSession.setFlushMode(FlushMode.AUTO)
                }

                flash.message ="${lineCount} line(s) processed, ${importCount} new items created."
            } else {
                flash.errorMessage ="No file selected!"
            }
        }

        redirect(action: 'items')

    }

    def schedule() {
        def isRunning = settingService.getSettingValue(SettingService.INDEX_JOB_RUNNING_KEY) == "true"
        def jobs = jobManagerService.getJobs("BHLJobs")

        def runningJobs = jobManagerService.quartzScheduler.currentlyExecutingJobs


        [isRunning: isRunning, jobs: jobs, runningJobs: runningJobs]
    }

    def triggerIndexingJob() {
        IndexingJob.triggerNow([:])
        redirect(action: 'schedule')
    }

    def interruptIndexingJob() {
        def jobs = jobManagerService.getJobs("BHLJobs")
        jobs.each {
            jobManagerService.interruptJob("BHLJobs", it.name)
        }
        redirect(action: 'schedule')
    }

}
