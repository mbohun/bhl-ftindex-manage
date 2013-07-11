package bhl.ftindex.manage

class Item {

    String itemId
    String primaryTitleId
    String internetArchiveId
    String title
    String volume

    static mapping = {
        title length: 2000
        volume length: 1000
    }

    static constraints = {
        itemId nullable: false
        volume nullable: true
    }

}
