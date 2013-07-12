package bhl.ftindex.manage

class ItemController {

    def BHLService

    def index() {
        def metaData = this.BHLService.getItemMetaData("7", true, true)
        println metaData
        redirect(uri:'/')
    }

}
