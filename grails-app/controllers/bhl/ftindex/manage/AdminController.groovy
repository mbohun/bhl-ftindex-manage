package bhl.ftindex.manage

import org.hibernate.FlushMode
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import java.text.SimpleDateFormat

class AdminController {

    def sessionFactory

    def index() {}

    def items() {}

    def deleteAllItems() {
        Item.executeUpdate("delete Item");
        redirect(action:'items')
    }

    def loadItems() {
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

}
