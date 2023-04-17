package com.lgx.ams.system.dao;


import com.lgx.ams.system.entity.ArchiveFile;
import com.lgx.ams.system.entity.ReorganizeFile;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.SqlResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@SqlResource("archiveFile")
@Repository
public interface ArchiveFileMapper extends BaseMapper<ArchiveFile> {

    int getFileCount();

    List<ArchiveFile> getFileList(int current, int pageSize);

}
