package  com.learning.url_shortener.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learning.url_shortener.dto.CreateUrlRequest;
import com.learning.url_shortener.dto.CreateUrlResponse;
import com.learning.url_shortener.service.UrlService;

@RestController
public class UrlController {
private static final Logger logger = LoggerFactory.getLogger(UrlController.class);
	private final UrlService urlService;

	public UrlController(UrlService urlService) {
		this.urlService = urlService;
	}

	@GetMapping("/api/greet")
	public ResponseEntity<String> greet() {
		return ResponseEntity.status(HttpStatus.OK).body("Hello User");

	}

	@PostMapping("/api/urls")
	public ResponseEntity<CreateUrlResponse> create(@RequestBody CreateUrlRequest request) {

		CreateUrlResponse response = urlService.createShortUrl(request.originalUrl());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{shortCode}")
	public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

		String originalUrl = urlService.getOriginalUrl(shortCode);
		logger.info("Redirecting to: {}", originalUrl);

		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
	}
}