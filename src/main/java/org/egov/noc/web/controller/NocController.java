package org.egov.noc.web.controller;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.noc.model.Errors;
import org.egov.noc.model.RequestData;
import org.egov.noc.service.NocService;
import org.egov.noc.util.CommonConstants;
import org.egov.noc.util.UserUtil;
import org.egov.noc.web.contract.NocResponse;
import org.egov.noc.web.contract.ReponseData;
import org.egov.noc.web.contract.factory.ResponseFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("noc")
public class NocController {

	@Autowired
	private NocService nocService;

	@Autowired
	private UserUtil userUtil;

	@Autowired
	private ResponseFactory responseFactory;

	// Get NOC
	@PostMapping("_get")
	@ResponseBody
	@CrossOrigin
	public ResponseEntity<?> get(@RequestBody RequestData requestData, BindingResult bindingResult) {

		log.debug(String.format("STARTED GET NOC REQUEST : %1s", requestData.toString()));
		Errors response = userUtil.validateUser(requestData);
		NocResponse nocResponse = null;
		if (response.getError().getMessage().equals(CommonConstants.SUCCESS)) {
			if (bindingResult.hasErrors()) {
				return new ResponseEntity<>(
						responseFactory.getErrorResponse(bindingResult, requestData.getRequestInfo()),
						HttpStatus.BAD_REQUEST);
			}
			nocResponse = nocService.searchNoc(requestData);
			log.debug(String.format("ENDED GET NOC RESPONSE : %1s", nocResponse.getNocApplicationDetail()));
			return new ResponseEntity<>(nocResponse, HttpStatus.OK);
		}
		nocResponse = new NocResponse();
		nocResponse.setResposneInfo(
				new ResponseInfo().builder().resMsgId("INVALID USER").status(CommonConstants.FAIL).build());
		log.debug(String.format("ENDED GET NOC RESPONSE : %1s", nocResponse.toString()));
		return new ResponseEntity<>(nocResponse, HttpStatus.OK);
	}

	// Viewing NOC
	@PostMapping("_view")
	@ResponseBody
	@CrossOrigin
	public ResponseEntity<?> view(@RequestBody RequestData requestData, BindingResult bindingResult) {

		log.debug(String.format("STARTED VIEW NOC REQUEST : %1s", requestData.toString()));
		Errors response = userUtil.validateUser(requestData);
		NocResponse nocResponse = null;
		if (response.getError().getMessage().equals(CommonConstants.SUCCESS)) {
			if (bindingResult.hasErrors()) {
				return new ResponseEntity<>(
						responseFactory.getErrorResponse(bindingResult, requestData.getRequestInfo()),
						HttpStatus.BAD_REQUEST);
			}

			if (!requestData.getApplicationId().isEmpty()) {
				nocResponse = nocService.viewNoc(requestData);
			} else {
				nocResponse = new NocResponse();
				nocResponse.setResposneInfo(new ResponseInfo().builder().resMsgId("INVALID APPLICATION ID")
						.status(CommonConstants.FAIL).build());
			}
			log.debug(String.format("ENDED VIEW NOC RESPONSE : %1s", nocResponse.getNocApplicationDetail()));
			return new ResponseEntity<>(nocResponse, HttpStatus.OK);
		}

		nocResponse = new NocResponse();
		nocResponse.setResposneInfo(
				new ResponseInfo().builder().resMsgId("INVALID USER").status(CommonConstants.FAIL).build());
		log.debug(String.format("ENDED VIEW NOC RESPONSE : %1s", nocResponse.toString()));
		return new ResponseEntity<>(nocResponse, HttpStatus.OK);
	}

	// Adding NOC
	@PostMapping("_create")
	@ResponseBody
	@CrossOrigin
	public ResponseEntity<?> createNoc(@RequestBody RequestData requestData, BindingResult bindingResult) {

		log.debug(String.format("STARTED ADD/CREATE NOC REQUEST : %1s", requestData.toString()));
		Errors response = userUtil.validateUser(requestData);
		ReponseData responseDataResponse = null;
		if (response.getError().getMessage().equals(CommonConstants.SUCCESS)) {
			if (bindingResult.hasErrors()) {
				return new ResponseEntity<>(
						responseFactory.getErrorResponse(bindingResult, requestData.getRequestInfo()),
						HttpStatus.BAD_REQUEST);
			}

			if ((requestData.getApplicationStatus() != null && !requestData.getApplicationStatus().isEmpty())
					&& !requestData.getDataPayload().isEmpty() && !requestData.getTenantId().isEmpty()) {
				responseDataResponse = nocService.saveNoc(requestData);
			} else {
				responseDataResponse = new ReponseData();
				responseDataResponse.setResponseInfo(new ResponseInfo().builder().resMsgId("INVALID REQUESTED DATA")
						.status(CommonConstants.FAIL).build());
			}
			log.debug(String.format("ENDED ADD/CREATE NOC RESPONSE : %1s", responseDataResponse.toString()));
			return new ResponseEntity<>(responseDataResponse, HttpStatus.OK);
		}
		responseDataResponse = new ReponseData();
		responseDataResponse.setResponseInfo(
				new ResponseInfo().builder().resMsgId("INVALID USER").status(CommonConstants.FAIL).build());
		log.debug(String.format("ENDED ADD/CREATE NOC RESPONSE : %1s", responseDataResponse.toString()));
		return new ResponseEntity<>(responseDataResponse, HttpStatus.OK);
	}

	// update Noc
	@PostMapping("_update")
	@CrossOrigin
	public ResponseEntity<?> update(@RequestBody RequestData requestData, BindingResult bindingResult) {

		log.debug(String.format("STARTED UPDATE NOC REQUEST : %1s", requestData.toString()));
		Errors response = userUtil.validateUser(requestData);
		ReponseData responseDataResponse = null;
		if (response.getError().getMessage().equals(CommonConstants.SUCCESS)) {
			if (bindingResult.hasErrors()) {
				return new ResponseEntity<>(
						responseFactory.getErrorResponse(bindingResult, requestData.getRequestInfo()),
						HttpStatus.BAD_REQUEST);
			}

			if (!requestData.getApplicationId().isEmpty() && !requestData.getDataPayload().isEmpty()
					&& !requestData.getTenantId().isEmpty()) {
				responseDataResponse = nocService.updateNoc(requestData);
			} else {
				responseDataResponse = new ReponseData();
				responseDataResponse.setResponseInfo(new ResponseInfo().builder().resMsgId("INVALID REQUEST DATA")
						.status(CommonConstants.FAIL).build());
			}
			log.debug(String.format("ENDED UPDATE NOC RESPONSE : %1s", responseDataResponse.toString()));
			return new ResponseEntity<>(responseDataResponse, HttpStatus.OK);
		}
		responseDataResponse = new ReponseData();
		responseDataResponse.setResponseInfo(
				new ResponseInfo().builder().resMsgId("INVALID USER").status(CommonConstants.FAIL).build());
		log.debug(String.format("ENDED UPDATE NOC RESPONSE : %1s", responseDataResponse.toString()));
		return new ResponseEntity<>(responseDataResponse, HttpStatus.OK);
	}

	@PostMapping("_updateappstatus")
	@ResponseBody
	@CrossOrigin
	public ResponseEntity<?> updateApplicationStatus(@RequestBody RequestData requestData,
			BindingResult bindingResult) {

		log.debug(String.format("STARTED APPLICATION STATUS UPDATE NOC REQUEST : %1s", requestData.toString()));
		Errors response = userUtil.validateUser(requestData);
		ReponseData responseDataResponse = null;
		if (response.getError().getMessage().equals(CommonConstants.SUCCESS)) {
			if (bindingResult.hasErrors()) {
				return new ResponseEntity<>(
						responseFactory.getErrorResponse(bindingResult, requestData.getRequestInfo()),
						HttpStatus.BAD_REQUEST);
			}

			if (!requestData.getApplicationStatus().isEmpty() && !requestData.getApplicationType().isEmpty()
					&& !requestData.getApplicationId().isEmpty() && !requestData.getTenantId().isEmpty()) {

				responseDataResponse = nocService.updateNocApplicationStatus(requestData);
				return new ResponseEntity<>(responseDataResponse, HttpStatus.CREATED);
			} else {
				responseDataResponse = new ReponseData();
				responseDataResponse.setResponseInfo(new ResponseInfo().builder().resMsgId("INVALID REQUEST DATA")
						.status(CommonConstants.FAIL).build());
			}

			log.debug(String.format("ENDED APPLICATION STATUS UPDATE NOC RESPONSE : %1s",
					responseDataResponse.toString()));
			return new ResponseEntity<>(responseDataResponse, HttpStatus.OK);
		}

		responseDataResponse = new ReponseData();
		responseDataResponse.setResponseInfo(
				new ResponseInfo().builder().resMsgId("INVALID USER").status(CommonConstants.FAIL).build());
		log.debug(String.format("ENDED APPLICATION STATUS UPDATE NOC RESPONSE : %1s", responseDataResponse.toString()));
		return new ResponseEntity<>(responseDataResponse, HttpStatus.OK);

	}

	@PostMapping("_getcolumnsmodules")
	@ResponseBody
	@CrossOrigin
	public ResponseEntity<?> getcolumnsModules(@RequestBody RequestData requestData, BindingResult bindingResult,
			@RequestParam(value = "noctype") String nocType) {

		log.debug(String.format("STARTED GET COLUMNS MODULES NOC REQUEST : %1s", requestData.toString()));
		Errors response = userUtil.validateUser(requestData);
		JSONArray jsonColumns = new JSONArray();
		JSONObject jsonObject = new JSONObject();

		ReponseData responseDataResponse = null;
		if (response.getError().getMessage().equals(CommonConstants.SUCCESS)) {
			if (bindingResult.hasErrors()) {
				return new ResponseEntity<>(
						responseFactory.getErrorResponse(bindingResult, requestData.getRequestInfo()),
						HttpStatus.BAD_REQUEST);
			}
			if (nocType != null && !nocType.isEmpty()) {
				jsonColumns = nocService.getColumnsForNoc(nocType);
			} else {
				jsonObject = new JSONObject();
				jsonObject.put("ErrorMessage", "INVALID REQUEST DATA");
				jsonColumns.add(jsonObject);
			}
			log.debug(String.format("ENDED GET COLUMNS MODULES NOC RESPONSE : %1s", jsonColumns.toString()));
			return new ResponseEntity<>(jsonColumns, HttpStatus.OK);
		}
		jsonObject.put("ErrorMessage", "INVALID USER");
		jsonColumns.add(jsonObject);

		log.debug(String.format("ENDED GET COLUMNS MODULES NOC RESPONSE : %1s", jsonColumns.toString()));
		return new ResponseEntity<>(jsonColumns, HttpStatus.OK);
	}

	@PostMapping("_getcolumnsremarks")
	@ResponseBody
	@CrossOrigin
	public ResponseEntity<?> getcolumnsRemarks(@RequestBody RequestData requestData, BindingResult bindingResult) {

		log.debug("getcolumns Remarks Request:" + requestData);
		Errors res = null;
		Errors response = userUtil.validateUser(requestData);
		if (response.getError().getMessage().equals("success")) {
			log.debug("Get Remarks :" + requestData.getDataPayload());

			if (bindingResult.hasErrors()) {
				return new ResponseEntity<>(
						responseFactory.getErrorResponse(bindingResult, requestData.getRequestInfo()),
						HttpStatus.BAD_REQUEST);
			}

			final JSONArray jsonColumns = nocService.getColumnsRemarksForNoc(requestData);
			return new ResponseEntity<>(jsonColumns, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping("_getremarkshistory")
	@ResponseBody
	@CrossOrigin
	public ResponseEntity<Object> getRemarksForNoc(@RequestBody RequestData requestData, BindingResult bindingResult)
			throws ParseException {

		log.debug("_getremarkshistory Request:" + requestData);
		ReponseData responseDataResponse = null;

		Errors response = userUtil.validateUser(requestData);
		if (response.getError().getMessage().equals(CommonConstants.SUCCESS)) {
			log.debug("Get Remarks :" + requestData.getDataPayload());

			if (bindingResult.hasErrors()) {
				return new ResponseEntity<>(
						responseFactory.getErrorResponse(bindingResult, requestData.getRequestInfo()),
						HttpStatus.BAD_REQUEST);
			}
			if (requestData.getApplicationId() != null && !requestData.getApplicationId().isEmpty()) {
				return new ResponseEntity<>(nocService.getRemarksForNoc(requestData), HttpStatus.OK);
			} else {
				ResponseInfo responseInfo = new ResponseInfo();
				responseInfo.setStatus(CommonConstants.FAIL);
				responseInfo.setMsgId("Required parameters missing");
				responseDataResponse = new ReponseData();
				responseDataResponse.setResponseInfo(responseInfo);
				return new ResponseEntity<>(responseDataResponse, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

	}

}
