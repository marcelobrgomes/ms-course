package com.hr.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hr.entities.Payment;
import com.hr.entities.Worker;

@Service
public class PaymentService {
	
	@Value("${hr-worker.host}")
	private String workerHost;
	
	@Autowired
	private RestTemplate restTemplate;

	public Payment getPayment(long workerId, int days) {
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("id", String.valueOf(workerId));
		
		Worker worker = restTemplate.getForObject(workerHost + "/workers/{id}", Worker.class, variables);
		return new Payment(worker.getName(), worker.getDailyIncome(), days);
	}
}