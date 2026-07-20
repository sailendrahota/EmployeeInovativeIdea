import http from 'k6/http';
import { check, sleep } from 'k6';

// Bypasses the Minikube self-signed certificate error
export const options = {
    insecureSkipTLSVerify: true,
};

export default function () {
    // 1. Generate the random ID for this specific virtual user's iteration
    const randomId = Math.floor(Math.random() * 999000) + 1000;

    // 2. FIRST: Hit the POST Registration Endpoint
    const payload = JSON.stringify({
        employeeId: randomId,
        firstName: "Narayan",
        lastName: "Prabhudigni",
        // We also randomize the email to avoid unique database constraints
        email: `Shriman${randomId}@durgasoft.com`,
        doj: "2017-02-19",
        phoneNumber: "8948676760",
        salary: 3200000
    });

    const params = { headers: { 'Content-Type': 'application/json' } };

    const postRes = http.post('https://hr-portal.local/api/registeremployee', payload, params);

    // Verify the POST was successful before moving on
    check(postRes, {
        'POST registered successfully (200/201)': (r) => r.status === 200 || r.status === 201,
    });

    // 3. SECOND: Hit the GET Tax Endpoint using the EXACT same ID we just registered
    const getRes = http.get(`https://hr-portal.local/api/tax?employee_id=${randomId}`);

    // Verify the GET found the user and calculated the tax
    check(getRes, {
        'GET tax calculated successfully (200)': (r) => r.status === 200,
    });

    // Brief pause to mimic human interaction and prevent local network port exhaustion
    sleep(0.1);
}