import { useState, useEffect } from 'react';
import { getSystemInfo } from '../api';

export default function SystemInfo() {
    const [info, setInfo] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getSystemInfo().then(data => {
            setInfo(data.content || {});
            setLoading(false);
        }).catch(() => setLoading(false));
    }, []);

    return (
        <>
            <div className="page-header">
                <h2>System</h2>
                <p className="page-desc">application info and diagnostics</p>
            </div>
            <div className="page-body">
                {loading ? (
                    <div className="loading"><span className="loading-dot" /> loading...</div>
                ) : (
                    <div className="card" style={{ maxWidth: 480 }}>
                        <div className="card-header">system info</div>
                        <table className="data-table">
                            <tbody>
                            {Object.entries(info || {}).map(([key, val]) => (
                                <tr key={key}>
                                    <td style={{ color: 'var(--text-dim)', width: '40%' }}>{key}</td>
                                    <td style={{ color: 'var(--accent-green)' }}>{String(val)}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </>
    );
}
