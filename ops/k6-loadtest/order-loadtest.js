import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '15s', target: 20 }, // Ramp up to 20 users over 15s
        { duration: '30s', target: 50 }, // Hold 50 users for 30s
        { duration: '15s', target: 0 },  // Cool down to 0 users
    ],
    thresholds: {
        // Fail test if more than 1% of HTTP requests fail
        http_req_failed: ['rate<0.01'],
        // 95% of requests must complete in under 500ms (asynchronous API goal)
        http_req_duration: ['p(95)<500'],
    },
};

export default function () {
    // Point directly to your Minikube Ingress host
    const url = 'http://hr-portal.local/api/checkout';

    // Generate unique order payload per virtual user and iteration
    const uniqueId = `${Date.now()}-${__VU}-${__ITER}`;

    const payload = JSON.stringify({
        orderId: `ORD-${uniqueId}`,
        status: 'CREATED',
        amount: parseFloat((Math.random() * 450 + 50).toFixed(2)), // Random amount between 50.00 and 500.00
        customerEmail: `cluster.user.${__VU}.${__ITER}@example.com`,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);

    // Validate that the API accepts the event (200 OK or 202 Accepted)
    check(res, {
        'status is 200 or 202': (r) => r.status === 200 || r.status === 202,
    });

    // Short 100ms pause per VU to simulate realistic high throughput
    sleep(0.1);
}