package com.hr.payroll.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hr.payroll.entities.Payment;
import com.hr.payroll.services.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/payments")
public class PaymentResource {

	@Autowired
	private PaymentService paymentService;
	
	@HystrixCommand(fallbackMethod = "getPaymentAlternative")
	@GetMapping("/{workerId}/days/{days}")
	public ResponseEntity<Payment> getPayment(@PathVariable long workerId, @PathVariable Integer days) {
		return ResponseEntity.ok(paymentService.getPayment(workerId, days));
	}
	
	public ResponseEntity<Payment> getPaymentAlternative(long workerId, Integer days) {
		Payment payment = new Payment("Fulano", 400.00, days);
		return ResponseEntity.ok(payment);
	}
}