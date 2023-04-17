getFileList
===
    select * from `file` order by uploadtime limit #{current},#{pageSize}

getFileCount
===
    select count(1) as count from `file`

getFileById
===
    select * from `file` where id = #{id}

updateFileById
===
    update `file` set downloadfrequency = #{downloadfrequency} where id = #{id}