import { useState, useEffect } from 'react';

function App() {
    // --- STATE FOR POST (Register Employee) ---
    const [formData, setFormData] = useState({
        employeeId: '',
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        doj: '',
        salary: ''
    });
    const [registerStatus, setRegisterStatus] = useState(null);
    const [isRegistering, setIsRegistering] = useState(false);

    // --- STATE FOR GET (Tax Lookup)  ---
    const [searchId, setSearchId] = useState('');
    const [taxData, setTaxData] = useState(null);
    const [taxError, setTaxError] = useState(null);
    const [isSearching, setIsSearching] = useState(false);

    // --- STATE FOR RUNTIME DIAGNOSTICS (/info) ---
    const [envData, setEnvData] = useState(null);
    const [envLoading, setEnvLoading] = useState(true);
    const [envError, setEnvError] = useState(null);

    // --- EFFECT FOR DIAGNOSTICS ---
    useEffect(() => {
        // Fetches your runtime environment data directly from the flat /info endpoint
        fetch('http://localhost:8080/info')
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Failed to reach cluster metadata endpoint');
                }
                return response.json();
            })
            .then((data) => {
                setEnvData(data);
                setEnvLoading(false);
            })
            .catch((err) => {
                console.error(err);
                setEnvError(err.message);
                setEnvLoading(false);
            });
    }, []);

    // --- HANDLERS ---
    const handleRegister = async (e) => {
        e.preventDefault();
        setIsRegistering(true);
        setRegisterStatus(null);

        try {
            const payload = {
                ...formData,
                employeeId: parseInt(formData.employeeId),
                salary: parseFloat(formData.salary),
                doj: formData.doj ? formData.doj.split('T')[0] : null
            };

            const response = await fetch('http://localhost:8080/registeremployee', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                setRegisterStatus({ type: 'success', message: 'Employee successfully registered!' });
                setFormData({ employeeId: '', firstName: '', lastName: '', email: '', phoneNumber: '', doj: '', salary: '' });
            } else {
                try {
                    const errorData = await response.json();
                    setRegisterStatus({
                        type: 'error',
                        message: `Error: ${errorData.message}`
                    });
                } catch (parseError) {
                    setRegisterStatus({ type: 'error', message: 'Failed to register employee (Invalid Data).' });
                }
            }
        } catch (error) {
            setRegisterStatus({ type: 'error', message: 'Server connection error.' });
        } finally {
            setIsRegistering(false);
        }
    };

    const handleTaxSearch = async (e) => {
        e.preventDefault();
        setIsSearching(true);
        setTaxError(null);
        setTaxData(null);

        try {
            const response = await fetch(`http://localhost:8080/tax?employee_id=${searchId}`);
            if (response.ok) {
                const data = await response.json();
                setTaxData(data);
            } else {
                setTaxError('Could not calculate taxes for this ID.');
            }
        } catch (error) {
            setTaxError('Server connection error.');
        } finally {
            setIsSearching(false);
        }
    };

    return (
        <div className="min-h-screen bg-slate-50 p-8 font-sans text-slate-800">
            <div className="max-w-7xl mx-auto">
                <h1 className="text-4xl font-extrabold text-slate-900 mb-2 text-center tracking-tight">Enterprise HR Portal</h1>
                <p className="text-center text-slate-500 mb-8">Manage employee onboarding and financial records</p>

                {/* --- FULL WIDTH SECTION: DIAGNOSTICS WIDGET --- */}
                <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-200 mb-10">
                    <h3 className="text-sm font-bold tracking-wider text-slate-400 uppercase mb-4 flex items-center gap-2">
                        <span className="inline-block w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
                        System Runtime Diagnostics
                    </h3>

                    {envLoading && (
                        <div className="text-slate-400 font-medium text-sm animate-pulse">
                            Quizzing cluster API for container environment parameters...
                        </div>
                    )}

                    {envError && (
                        <div className="text-sm font-medium text-red-600 bg-red-50 p-3 rounded-lg border border-red-100">
                            ⚠️ API Offline or Port-Forward Missing: {envError}
                        </div>
                    )}

                    {envData && (
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                            <div className="bg-slate-50 p-4 rounded-xl border border-slate-100">
                                <span className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-1">Runtime Environment</span>
                                <span className="text-base font-bold text-emerald-600">{envData.runtime_environment}</span>
                            </div>
                            <div className="bg-slate-50 p-4 rounded-xl border border-slate-100">
                                <span className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-1">Java JVM Build</span>
                                <span className="text-base font-bold text-slate-800">{envData.java_version} <span className="text-xs text-slate-500 font-normal">({envData.java_vendor})</span></span>
                            </div>
                            <div className="bg-slate-50 p-4 rounded-xl border border-slate-100">
                                <span className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-1">Container Architecture</span>
                                <span className="text-base font-bold text-slate-800">{envData.os_name} <span className="text-xs text-slate-500 font-normal">({envData.os_architecture})</span></span>
                            </div>
                        </div>
                    )}
                </div>

                {/* --- TWO COLUMN WEB PORTAL MATRIX --- */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">

                    {/* LEFT COLUMN: REGISTRATION FORM */}
                    <div className="bg-white p-8 rounded-2xl shadow-sm border border-slate-200">
                        <h2 className="text-2xl font-bold mb-6 text-slate-800 border-b pb-2">Onboard New Employee</h2>

                        <form onSubmit={handleRegister} className="space-y-5">
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-semibold text-slate-600 mb-1">Employee ID</label>
                                    <input type="number" required className="w-full p-3 bg-slate-50 border border-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
                                           value={formData.employeeId} onChange={e => setFormData({...formData, employeeId: e.target.value})} />
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-slate-600 mb-1">Salary (Annual)</label>
                                    <input type="number" required className="w-full p-3 bg-slate-50 border border-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
                                           value={formData.salary} onChange={e => setFormData({...formData, salary: e.target.value})} />
                                </div>
                            </div>

                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-semibold text-slate-600 mb-1">First Name</label>
                                    <input type="text" required className="w-full p-3 bg-slate-50 border border-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
                                           value={formData.firstName} onChange={e => setFormData({...formData, firstName: e.target.value})} />
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-slate-600 mb-1">Last Name</label>
                                    <input type="text" required className="w-full p-3 bg-slate-50 border border-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
                                           value={formData.lastName} onChange={e => setFormData({...formData, lastName: e.target.value})} />
                                </div>
                            </div>

                            <div>
                                <label className="block text-sm font-semibold text-slate-600 mb-1">Email Address</label>
                                <input type="email" required className="w-full p-3 bg-slate-50 border border-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
                                       value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} />
                            </div>

                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-semibold text-slate-600 mb-1">Phone Number</label>
                                    <input type="tel" required className="w-full p-3 bg-slate-50 border border-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
                                           value={formData.phoneNumber} onChange={e => setFormData({...formData, phoneNumber: e.target.value})} />
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-slate-600 mb-1">Date of Joining</label>
                                    <input type="datetime-local" required className="w-full p-3 bg-slate-50 border border-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
                                           value={formData.doj} onChange={e => setFormData({...formData, doj: e.target.value})} />
                                </div>
                            </div>

                            <button type="submit" disabled={isRegistering} className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-lg transition duration-200 disabled:opacity-50">
                                {isRegistering ? 'Processing...' : 'Register Employee'}
                            </button>

                            {registerStatus && (
                                <div className={`p-4 rounded-lg font-medium ${registerStatus.type === 'success' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                                    {registerStatus.message}
                                </div>
                            )}
                        </form>
                    </div>

                    {/* RIGHT COLUMN: TAX LOOKUP */}
                    <div className="bg-white p-8 rounded-2xl shadow-sm border border-slate-200 h-fit">
                        <h2 className="text-2xl font-bold mb-6 text-slate-800 border-b pb-2">Tax Deduction Calculator</h2>

                        <form onSubmit={handleTaxSearch} className="mb-6 flex gap-3">
                            <input type="number" placeholder="Enter Employee ID..." required className="flex-1 p-3 bg-slate-50 border border-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
                                   value={searchId} onChange={e => setSearchId(e.target.value)} />
                            <button type="submit" disabled={isSearching} className="bg-slate-800 hover:bg-slate-900 text-white font-bold py-3 px-6 rounded-lg transition duration-200 disabled:opacity-50">
                                {isSearching ? 'Loading...' : 'Calculate'}
                            </button>
                        </form>

                        {taxError && <div className="p-4 bg-red-100 text-red-700 rounded-lg font-medium">{taxError}</div>}

                        {taxData && (
                            <div className="bg-slate-900 rounded-lg p-6 overflow-hidden">
                                <div className="flex items-center justify-between mb-4 border-b border-slate-700 pb-2">
                                    <h3 className="text-emerald-400 font-semibold font-mono text-sm">JSON Response</h3>
                                    <span className="text-xs text-slate-400 bg-slate-800 px-2 py-1 rounded">Status: 200 OK</span>
                                </div>
                                <pre className="text-slate-300 font-mono text-sm overflow-x-auto whitespace-pre-wrap">
                  {JSON.stringify(taxData, null, 2)}
                </pre>
                            </div>
                        )}

                        {!taxData && !taxError && (
                            <div className="border-2 border-dashed border-slate-200 rounded-lg p-10 text-center text-slate-400">
                                Enter an ID above to pull real-time tax data from the Spring Boot API.
                            </div>
                        )}
                    </div>

                </div>
            </div>
        </div>
    );
}

export default App;