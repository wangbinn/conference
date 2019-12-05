package com.citic.conference.service;
import com.alibaba.fastjson.JSONArray;
import com.citic.conference.database.BaseService;
import com.citic.conference.database.ProcedureContext;
import com.citic.conference.database.ProcedureParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
/**查询当前用户已经预订的会议室记录
 * 用户取消未过期的预订记录
 */
@Service
public class BookingRecordService {

    @Autowired
    private BaseService baseService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public JSONArray record(String name) {
        List<ProcedureParam> pm = new ArrayList<ProcedureParam>();
        ProcedureParam pp1 = new ProcedureParam(1,name, Types.VARCHAR, "IN");
        //System.out.println("name"+name);
        pm.add(pp1);
        ProcedureContext scheduledRecord =
                baseService.callProcedure("Select_ScheduledRecord", pm);
        JSONArray datas = scheduledRecord.getDatas();
        System.out.println(datas);
        return datas;
    }

    //根据预定记录ID来删除未过期预定记录
    public Boolean deleteRecordById(Integer id){
        String sql="DELETE FROM Book WHERE id=?";
        int register=0 ;
        try {
            register=jdbcTemplate.update(sql, id);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (register==1){
            return true;
        }else
            return false;
    }

}
