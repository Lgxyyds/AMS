getFileCount
===
    select count(1) as count from `reorganize_file`

getFileList
===
    select * from `reorganize_file` order by uploadtime limit #{current},#{pageSize}

getFileById
===
    select * from `reorganize_file` where id = #{id}