package bhl.ftindex.manage

class Setting {

    String key
    String value

    static constraints = {
        key nullable: false
        value nullable: false
    }

}
