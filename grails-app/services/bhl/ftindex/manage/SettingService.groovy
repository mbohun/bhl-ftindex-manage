package bhl.ftindex.manage

class SettingService {

    public static final String INDEX_JOB_RUNNING_KEY = "INDEX_JOB_RUNNING"

    def getSettingValue(String key) {
        def setting = Setting.findByKey(key)
        if (setting) {
            return setting.value
        }
        return null
    }

    def setSettingValue(String key, String value) {
        def setting = Setting.findByKey(key)
        if (!setting) {
            setting = new Setting(key: key, value: value)
        } else {
            setting.lock()
            setting.value = value
        }

        setting.save(flush: true, failOnError: true)
    }

}
