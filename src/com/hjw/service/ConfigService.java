package com.hjw.service;

import java.util.List;

import com.hjw.DTO.CenterConfigurationDTO;
import com.hjw.DTO.ExamQueueLog;
import com.hjw.DTO.HisClinicItemPriceDTO;
import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.Bean.ThridReq;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.HisClinicItemPriceListDTO;
import com.hjw.wst.DTO.JobDTO;
import com.hjw.wst.DTO.UserDTO;
import com.hjw.wst.DTO.UserInfoDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.domain.ChargingDetailSingle;
import com.hjw.wst.domain.ChargingSummarySingle;
import com.hjw.wst.domain.HisClinicItem;
import com.hjw.wst.domain.HisPriceList;
import com.synjones.framework.exception.ServiceException;

/**
 * 
 * <菜单、功能、角色操作> <功能详细描述>
 * 
 * @author yangm
 * @version [3.0.0, Nov 4, 2011]
 * @see [相关类/方法]
 * @since [一卡通网站查询系统]
 */
public interface ConfigService {

	/**
	 * 
	     * @Title: getCenterconfigByKey   
	     * @Description: 通过kye获取   CenterConfigurationDTO 配置
	     * @param: @param keys
	     * @param: @return
	     * @param: @throws ServiceException      
	     * @return: CenterConfigurationDTO      
	     * @throws
	 */
	public CenterConfigurationDTO getCenterconfigByKey(String keys)throws ServiceException;
	
	public void insert_log(ThridInterfaceLog til) throws ServiceException;
	
	public void insert_message_log(String til_id, int seq_code, String lmessage) throws ServiceException;
	
	public void update_log(ThridInterfaceLog til) throws ServiceException;
	
	public boolean insert_lis_result(LisResult lr) throws ServiceException;
	
	public boolean insert_lis_result_new(LisResult lr) throws ServiceException;
	
	public boolean insert_pacs_result(PacsResult pr) throws ServiceException;
	
	public ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException;
	
	public ExamInfoUserDTO getExamInfoForBarcode(String sample_barcode) throws ServiceException;
	
	public ExamInfoUserDTO getExamInfoForReqNum(String req_nums) throws ServiceException;
	
	public ExamInfoUserDTO getExamInfoForExam_id(long exam_id) throws ServiceException;
	
	public void setExamInfoChargeItemPacsStatus(String req_nums, String status) throws ServiceException;
	
	public void setExamInfoChargeItemPacsStatus(String req_nums, String pacsItemCode, String status) throws ServiceException;
	
	public void setExamInfoChargeItemLisStatus(List<String> req_nums, String exam_num, String status, String samstatus) throws ServiceException;
	
	public void setExamInfoChargeItemLisStatus(String sample_barcode, String status, String samstatus, UserInfoDTO user, String check_date, String logName)
			throws ServiceException;
	
	public void setExamInfoChargeItemLisStatus(String req_num, String itemCodes, String status, String samstatus) throws ServiceException;
	
	public UserInfoDTO getUser(String user_log_name, String logName);

	public boolean insert_thrid_req(ThridReq tr) throws ServiceException;
	
	public boolean update_thrid_req(int readFlag,String notices, int id, String logname) throws ServiceException;
	
	public boolean insertExamQueueLog(ExamQueueLog eql, String logname) throws ServiceException;
	
	public int insert_zl_req_item(ZlReqItemDTO item,String logname) throws ServiceException;
	
	public int insert_zl_req_pacs_item(ZlReqPacsItemDTO item,String logname) throws ServiceException;
	
	public List<ZlReqItemDTO> select_zl_req_item(long exam_info_id, String lis_req_code,String logname) throws ServiceException;
	
	public List<ZlReqPacsItemDTO> select_zl_req_pacs_item(long exam_info_id, String pacs_req_code,String logname) throws ServiceException;
	
	public ZlReqItemDTO select_zl_req_item(String req_id,String logname) throws ServiceException;
	
	public ZlReqPacsItemDTO select_zl_req_pacs_item(String req_id,String logname) throws ServiceException;
	
	public ZlReqItemDTO select_zl_req_item_by_id(String id,String logname) throws ServiceException;
	
	public ZlReqPacsItemDTO select_zl_req_pacs_item_by_id(String id, String logname) throws ServiceException;
	
	public ChargingItemDTO findChargeItemByHis_num(String his_num) throws ServiceException;
	
	public List<HisClinicItemPriceListDTO> getHisjg(HisClinicItemPriceListDTO dto) throws ServiceException;
	
	public void insertClinicPriceList(String logname, List<HisClinicItemPriceDTO> clinicPriceList);
	
	public void insertClinicPriceList_direct(String logname, List<HisClinicItemPriceDTO> clinicPriceList);
	
	public void insertClinicList(String logname, List<HisClinicItem> clinicList);
	
	public void insertClinicList_direct(String logname, List<HisClinicItem> clinicList);
	
	public void insertPriceList(String logname, List<HisPriceList> priceList);
	
	public void insertPriceList_direct(String logname, List<HisPriceList> priceList);
	
	public void updatePriceList(String logname, String price_code, String price);
	
	public void delete_his_data(String logname);
	
	public List<HisClinicItemPriceListDTO> getHisjg305(HisClinicItemPriceListDTO dto) throws ServiceException;
	
	public void jdbcsaveChargingDetailSingle(ChargingDetailSingle cds);
	
	public void jdbcsaveChargingSummarySingle(ChargingSummarySingle css);

	public ExamInfoUserDTO getExamInfoForBarcode305(String req_no);

	public ExaminfoChargingItemDTO getExaminfoChargingItem(long examinfo_id,long charge_item_id,String logname);
	
	public boolean selectExamQueueLog(String exam_num,String logname);
	
	public boolean updateExamQueueLog(String queue_no, String exam_num,String logname) throws ServiceException;

	public boolean hasItem(long examinfo_id,String logname);
	
	public String paymentApplicationT(String examNum, UserDTO user) throws ServiceException;

	
	public WebserviceConfigurationDTO getWebServiceConfig(String configKey) throws ServiceException;
	
	public List<JobDTO> getDatadis(String data_code)throws ServiceException;
	
	public String getDep_inter_num(String logname, String dep_num);
	
	public void setExamInfoChargeItemPacsStatus305(String pac_nos, String statuss);

	public ExamInfoUserDTO getExamInfoForReqNum305(String req_no);

	public boolean getHisPriceList(String logname, String price_code, String itemClass);

	public boolean getHisClinicItem(String logname, String clinic_code, String clinic_itemclass,
			String clinic_input_code);

	public void updateClinicList(String logname, String clinic_code, String clinic_itemclass, String clinic_input_code);

	public boolean getHisClinicItemVPriceList(String logname, String clinicItemClass, String clinicItemCode,
			String chargeItemclass, String chargeItemCode);

	public void updateHisClinicItemVPriceList(String logname, String clinicItemClass, String clinicItemCode,
			String chargeItemclass, String chargeItemCode, String amount, String units);

	public void updateClinicListTT(String logname, String item_code, String item_class, String input_code,
			String item_status, String price, String item_name);

	public List<HisClinicItemPriceListDTO> getHisjgTT(HisClinicItemPriceListDTO dto);

}
