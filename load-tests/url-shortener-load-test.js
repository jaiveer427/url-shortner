import http from 'k6/http';
import { check } from 'k6';

export const options = {
    scenarios: {
        // Phase 1: 2-minute JVM & HikariCP warm-up (excluded from measurement thresholds)
        warmup: {
            executor: 'constant-vus',
            vus: 50,
            duration: '2m',
            startTime: '0s',
            tags: { phase: 'warmup' },
        },
        // Phase 2: 5-minute steady-state measurement window
        measurement: {
            executor: 'constant-vus',
            vus: 50,
            duration: '5m',
            startTime: '2m', // Starts immediately after 2m warm-up finishes
            tags: { phase: 'measurement' },
        },
    },
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],
    thresholds: {
        // Evaluate latency and errors strictly on the 5-minute measurement phase
        'http_req_duration{phase:measurement}': ['p(95)<25', 'p(99)<50'],
        'http_req_failed{phase:measurement}': ['rate==0'],
    },
};

export default function () {
    const payload = JSON.stringify({
        originalUrl: 'https://google.com'
    });

    const response = http.post(
        'http://localhost:8080/api/urls',
        payload,
        {
            headers: { 'Content-Type': 'application/json' }
        }
    );

    check(response, {
        'status is 201': (r) => r.status === 201,
        'short code exists': (r) => r.status === 201 && r.json('shortCode') !== undefined,
    });
}
