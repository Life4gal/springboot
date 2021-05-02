package com.springboot.eft.service.impl;

import com.springboot.eft.dao.UploadedDAO;
import com.springboot.eft.model.UploadedRecord;
import com.springboot.eft.service.IUploadedService;
import com.springboot.eft.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadedServiceImpl implements IUploadedService {

	private final UploadedDAO uploadedDAO;

	@Autowired
	public UploadedServiceImpl(UploadedDAO uploadedDAO) {
		this.uploadedDAO = uploadedDAO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UploadedRecord> list(String user, String file, String category, int offset) {
		return (List<UploadedRecord>) ServiceUtils.invokeFileFilter(uploadedDAO, "listUploadedBy", user, file,
				category, offset);
	}
}
