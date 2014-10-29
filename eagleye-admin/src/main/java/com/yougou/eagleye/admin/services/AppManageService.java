package com.yougou.eagleye.admin.services;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.dao.EagleyeAppDao;
import com.yougou.eagleye.admin.domain.EagleyeApp;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.reflection.ReflectionUtils;

@Service("appManageService")
@Transactional
public class AppManageService{
	
	private final static Logger logger = LoggerFactory.getLogger(AppManageService.class);

	@Autowired
	private EagleyeAppDao eagleyeAppDao;
	
	@Autowired
	private AlertManageService alertManageService;
	
	
	/**
	 * 根据id获得App
	 * @param id
	 * @return 返回查询到的EagleyeApp对象
	 */
	public EagleyeApp getAppById(String id) throws Exception{
		EagleyeApp app = eagleyeAppDao.getEagleyeAppById(id);
		app.setGroupMap();
		return app;
	}
	
	/**
	 * 更新
	 * @param 页面传入的对象
	 * @return 返回更新后的用户对象
	 */
	public EagleyeApp updateApp(EagleyeApp pageParam) throws Exception{
		EagleyeApp app = null;
		if(pageParam!=null){
			//name需要排重
			if(eagleyeAppDao.verifyRepeat(pageParam)){//有重复数据
				pageParam.setFlash(AppConstants.FLASH_REPEAT);
				app = pageParam;
			}else{//没有重复的
				app = eagleyeAppDao.getEagleyeAppById(pageParam.getId()+"");
				ReflectionUtils.mergeObject(app, pageParam);
				app.setUpdateTime(new Date());
				
				app = eagleyeAppDao.createOrUpdate(app);
			}
		}
//		更新redis预警规则
		this.alertManageService.updateRuleListToCache();
		return app;
	}
	
	/**
	 * 新增
	 * @param eagleyeApp 待更新的对象
	 * @return 返回更新后的对象
	 */
	public EagleyeApp createApp(EagleyeApp eagleyeApp) throws Exception{
		if(eagleyeApp!=null){
			eagleyeApp.setDataStatus(AppConstants.DATASTATUS_VALID);
			eagleyeApp.setCreateTime(new Date());
			eagleyeApp.setUpdateTime(new Date());
			//name需要排重
			if(eagleyeAppDao.verifyRepeat(eagleyeApp)){//有重复数据
				eagleyeApp.setFlash(AppConstants.FLASH_REPEAT);
			}else{//没有重复的
				if(eagleyeApp.getAppName()==null){//用户名不能为空
					eagleyeApp.setFlash(AppConstants.FLASH_ERROR);
				}else{
					eagleyeApp = eagleyeAppDao.createOrUpdate(eagleyeApp);
				}
			}
		}
//		更新redis预警规则
		this.alertManageService.updateRuleListToCache();
		return eagleyeApp;
	}
	
	
	
	
	
	/**
	 * 删除
	 * @param id 待删除的用户对象的id
	 */
	public int delete(String id) throws Exception{
		int resultCount = eagleyeAppDao.delete(id);
//		更新redis预警规则
		this.alertManageService.updateRuleListToCache();
		return resultCount;
	}
	
	
	
	/**
	 * 简单模糊检索
	 * @param DtPageBean pageBean , String sSearch
	 * @return pageBean
	 * @throws Exception
	 */
	public DtPageBean<EagleyeApp> querySimpleAppList(DtPageBean<EagleyeApp> pageBean,String sSearch) throws Exception{
		pageBean = this.eagleyeAppDao.querySimplePage(pageBean, sSearch);
		return pageBean;
	}
	
	/**
	 * 高级检索
	 * @param DtPageBean pageBean , EagleyeApp eagleyeApp
	 * @return pageBean
	 * @throws Exception
	 */
	public DtPageBean<EagleyeApp> queryAppList(DtPageBean<EagleyeApp> pageBean,EagleyeApp eagleyeApp) throws Exception{
		pageBean=this.eagleyeAppDao.queryPage(pageBean, eagleyeApp);
		return pageBean;
	}
	
	
	/**
	 * 获取应用列表
	 * @return
	 * @throws Exception
	 */
	public List<EagleyeApp> getList() throws Exception{
		List<EagleyeApp> appList = this.eagleyeAppDao.queryList(null);
		return appList;
	}
	
	
	
}