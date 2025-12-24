package com.gozon.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "*")
public class GatewayController {

    private final RestTemplate restTemplate;

    @Value("${services.orders.url}")
    private String ordersUrl;

    @Value("${services.payments.url}")
    private String paymentsUrl;

    public GatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<String> createOrder(@RequestBody String body) {
        return forward(ordersUrl + "/orders", HttpMethod.POST, body);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<String> getOrders(@RequestParam String userId) {
        return forward(ordersUrl + "/orders?userId=" + userId, HttpMethod.GET, null);
    }

    @GetMapping("/api/orders/{id}")
    public ResponseEntity<String> getOrder(@PathVariable String id) {
        return forward(ordersUrl + "/orders/" + id, HttpMethod.GET, null);
    }

    @PostMapping("/api/accounts")
    public ResponseEntity<String> createAccount(@RequestBody String body) {
        return forward(paymentsUrl + "/accounts", HttpMethod.POST, body);
    }

    @PostMapping("/api/accounts/topup")
    public ResponseEntity<String> topUp(@RequestBody String body) {
        return forward(paymentsUrl + "/accounts/topup", HttpMethod.POST, body);
    }

    @GetMapping("/api/accounts/{userId}")
    public ResponseEntity<String> getBalance(@PathVariable String userId) {
        return forward(paymentsUrl + "/accounts/" + userId, HttpMethod.GET, null);
    }

    @GetMapping("/api/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API Gateway OK");
    }

    private ResponseEntity<String> forward(String url, HttpMethod method, String body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            return restTemplate.exchange(url, method, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(503).body("{\"error\": \"Service unavailable\"}");
        }
    }
}
