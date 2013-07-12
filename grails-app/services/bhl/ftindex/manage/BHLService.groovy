package bhl.ftindex.manage

import grails.converters.JSON

class BHLService {

    def grailsApplication

    def getItemMetaData(String itemId, boolean pages, boolean ocr) {
        def result = callService("GetItemMetadata", [itemid:itemId,pages: pages ? 't' : 'f', ocr: ocr ? 't' : 'f'])
        return JSON.parse(result)
    }

    def getTitleMetaData(Item item, boolean items) {
        if (!item || !item.primaryTitleId) {
            return null
        }
        def result = callService("GetTitleMetadata", [titleid:item.primaryTitleId,items: items ? 't' : 'f'])
        return JSON.parse(result)
    }

    private String callService(String op, Map params) {
        def urlRoot = grailsApplication.config.bhl.service.url.root
        def apikey = grailsApplication.config.bhl.service.apikey

        def serviceUrl = new StringBuilder()
        serviceUrl << "${urlRoot}?op=${op}&apikey=${apikey}&format=json"
        params?.each { kvp ->
            serviceUrl << "&${URLEncoder.encode(kvp.key as String, "UTF-8")}=${URLEncoder.encode(kvp.value as String, "UTF-8")}"
        }

        def url= new URL(serviceUrl.toString())
        return url.getText()
    }

}
