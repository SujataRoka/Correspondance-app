package com.ashokit.service;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashokit.entity.CitizenAppsEntity;
import com.ashokit.entity.CoTriggersEntity;
import com.ashokit.entity.EligibilityDtlsEntity;
import com.ashokit.repository.AppPlanRepository;
import com.ashokit.repository.CitizenAppsRepository;
import com.ashokit.repository.CoTriggersRepository;
import com.ashokit.repository.EligibilityDtlsRepository;
import com.ashokit.utils.EmailUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class CorrespondanceServiceImpl implements CorrespondanceServcie {

	@Autowired
	private CoTriggersRepository triggerRepo;

	@Autowired
	private EligibilityDtlsRepository eligiRepo;

	@Autowired
	private CitizenAppsRepository appRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public Map<String, Integer> generateNotices() {

		// private static final String PDF_PATH = "D:\\20-JRTP\\workspace-02\\Major
		// Project\\IHIS-Correspondance-Api\\pdf\\";

		Map<String, Integer> statusMap = new HashMap<>();
		Integer success = 0;
		Integer failure = 0;

		
		List<CoTriggersEntity> pendingTrgs = triggerRepo.findByTrgStatus("Pending");
		ExecutorService exService = Executors.newFixedThreadPool(10);
		ExecutorCompletionService<Object> pool = new ExecutorCompletionService<>(exService);

		for (CoTriggersEntity coTrigger : pendingTrgs) {

			pool.submit(new Callable<Object>() {

				@Override
				public Object call() throws Exception {
					// task to do

					try {
						processTrigger(coTrigger);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

			});

		}
		  statusMap.put("TOTAL_TRIGGER",pendingTrgs.size());
		  statusMap.put("SUCCESSS_COUNt",success);
		  statusMap.put("FAILURE_COUNT",failure);

	return statusMap;

	}

	private void processTrigger(CoTriggersEntity coTrigger) throws Exception {
		// get eligi data
		EligibilityDtlsEntity eligiData = eligiRepo.findByCaseNum(coTrigger.getCaseNum());
		Optional<CitizenAppsEntity> findById = appRepo.findById(coTrigger.getCaseNum());

		// generate pdf
		generatePdf(eligiData, findById.get());

		// send pdf to email
		sendPdfAsAttachement(findById.get());

		// store pdf in db & update trigger as completed
		byte[] fileData = new byte[1024];
		FileInputStream fis = new FileInputStream(new File(coTrigger.getCaseNum() + ".pdf"));
		fis.read(fileData);

		coTrigger.setNotice(fileData);
		coTrigger.setTrgStatus("Completed");
		triggerRepo.save(coTrigger);
	}

	private void sendPdfAsAttachement(CitizenAppsEntity app) {

		String subject = "Your Eligibility Notice - IHIS";
		String body = "body";
		emailUtils.sendEmail(app.getEmail(), subject, body, new File(app.getCaseNum() + ".pdf"));
	}

	private void generatePdf(EligibilityDtlsEntity eligiData, CitizenAppsEntity citizenApp) throws Exception {

		Document document = new Document();
		FileOutputStream fos = new FileOutputStream(new File(eligiData.getCaseNum() + ".pdf"));

		PdfWriter writer = PdfWriter.getInstance(document, fos);

		Font font = new Font(Font.HELVETICA, 16, Font.BOLDITALIC, Color.RED);

		document.open();

		Paragraph para = new Paragraph("Eligibility Details", font);

		document.add(para);

		PdfPTable table = new PdfPTable(2);

		table.addCell("Holder Name");
		table.addCell(citizenApp.getFullName());

		table.addCell("Holder ZZN");
		table.addCell(String.valueOf(citizenApp.getZzn()));

		table.addCell("Plan Name");
		table.addCell(eligiData.getPlanName());

		table.addCell("Plan Status");
		table.addCell(eligiData.getPlanStatus());

		table.addCell("Start Date");
		table.addCell(String.valueOf(eligiData.getStartDate()));

		table.addCell("End Date");
		table.addCell(String.valueOf(eligiData.getEndDate()));

		table.addCell("Benefit Amt");
		table.addCell(String.valueOf(eligiData.getBenifitAmt()));

		table.addCell("Denial Reason");
		table.addCell(eligiData.getDenialReason());

		document.add(table);
		document.close();

		writer.close();

	}

}
