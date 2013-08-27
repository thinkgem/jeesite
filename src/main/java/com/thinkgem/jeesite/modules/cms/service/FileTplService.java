package com.thinkgem.jeesite.modules.cms.service;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.cms.entity.FileTpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: songlai
 * Date: 13-8-27
 * Time: 下午4:56
 */
@Service
@Transactional(readOnly = true)
public class FileTplService extends BaseService {

    @Autowired
    ServletContext context;

    public List<String> getNameListByPrefix(String path) {
        List<FileTpl> list = getListByPath(path);
        List<String> result = new ArrayList<String>(list.size());
        for (FileTpl tpl : list) {
            result.add(tpl.getName());
        }
        return result;
    }

    public List<FileTpl> getListByPath(String path) {
   		File f = new File(context.getRealPath(path));
   		if (f.exists()) {
   			File[] files = f.listFiles();
   			if (files != null) {
   				List<FileTpl> list = new ArrayList<FileTpl>();
   				for (File file : files) {
                    if(file.isFile())
   					    list.add(new FileTpl(file, context.getRealPath("")));
   				}
   				return list;
   			} else {
   				return new ArrayList<FileTpl>(0);
   			}
   		} else {
   			return new ArrayList<FileTpl>(0);
   		}
   	}
}
