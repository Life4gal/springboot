package com.springboot.eft.dao.sqlprovider;

import com.springboot.eft.EftApplication;
import com.springboot.eft.additional_utils.Checker;
import com.springboot.eft.modules.constant.ConfigConstants;
import com.springboot.eft.modules.constant.DefaultValues;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class UserSqlProvider {

	public String updateAuthById() {
		return CommonSqlProvider.updateAuthById("user");
	}

	public String getUserBy(@Param("permission") int permission, @Param("condition") String condition, @Param
			("offset") int offset) {
		String sql = new SQL() {{
			SELECT("*");
			FROM("user");
			if (permission == DefaultValues.THREE_INT) {
				WHERE("permission<3");
			} else if (permission == DefaultValues.TWO_INT) {
				WHERE("permission<2");
			} else {
				WHERE("permission<0");
			}
			if (Checker.isNotEmpty(condition)) {
				WHERE("username like '%" + condition + "%' or email like '%" + condition + "%' or real_name like '" +
						condition + "'");
			}
			ORDER_BY(EftApplication.settings.getStringUseEval(ConfigConstants.USER_ORDER_BY_OF_SETTINGS));
		}}.toString();
		int size = EftApplication.settings.getIntegerUseEval(ConfigConstants.USER_PAGE_SIZE_OF_SETTINGS);
		return sql + " limit " + (offset * size) + "," + size;
	}
}
