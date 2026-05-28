import { useState, useEffect } from 'react';
import { getRepos, runAnalysis, getAnalysisHistory, deleteAnalysisHistory } from '../api';

export default function Analysis() {
    const [repos, setRepos] = useState([]);
    const [history, setHistory] = useState([]);
    const [loading, setLoading] = useState(true);
    const [running, setRunning] = useState(null);
    const [deleting, setDeleting] = useState(null);
    const [repoSearch, setRepoSearch] = useState('');

    useEffect(() => {
        Promise.all([
            getRepos().catch(() => ({ items: [] })),
            getAnalysisHistory().catch(() => ({ items: [] })),
        ]).then(([r, h]) => {
            setRepos(r.items || []);
            setHistory(h.items || []);
            setLoading(false);
        });
    }, []);

    const run = async (id) => {
        setRunning(id);
        try {
            const res = await runAnalysis(id);
            if (res?.content) setHistory(prev => [res.content, ...prev]);
        } catch (e) { /* ignore */ }
        setRunning(null);
    };

    const deleteEntry = async (id) => {
        setDeleting(id);
        try {
            await deleteAnalysisHistory(id);
            setHistory(prev => prev.filter(h => h.id !== id));
        } catch (e) { /* ignore */ }
        setDeleting(null);
    };

    const filteredRepos = repos.filter(r =>
        !repoSearch || (r.name || '').toLowerCase().includes(repoSearch.toLowerCase())
    );

    if (loading) {
        return (
            <>
                <div className="page-header"><h2>Analysis</h2></div>
                <div className="page-body"><div className="loading"><span className="loading-dot" /> loading...</div></div>
            </>
        );
    }

    return (
        <>
            <div className="page-header">
                <h2>Analysis</h2>
                <p className="page-desc">run health analysis on repositories and view history</p>
            </div>
            <div className="page-body">
                <div className="score-info-grid">
                    <div className="score-info-card">
                        <div className="score-info-title">
                            <span className="score-info-icon">~</span>
                            Analytics Score
                        </div>
                        <p className="score-info-text">
                            Measures repository activity and community engagement. Factors in stars, forks,
                            open issues, and recent commit frequency. A higher score means the project is
                            more actively used and developed. Scale: <strong>0–100</strong>.
                        </p>
                        <div className="score-info-factors">
                            <span className="score-factor">★ Stars</span>
                            <span className="score-factor">⑂ Forks</span>
                            <span className="score-factor">⊙ Issues</span>
                            <span className="score-factor">↻ Commits</span>
                        </div>
                    </div>
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }} className="analysis-tables">
                    <div className="card">
                        <div className="card-header">
                            <span>run analysis</span>
                        </div>
                        <div style={{ padding: '10px 16px', borderBottom: '1px solid var(--border)' }}>
                            <input
                                placeholder="search repos..."
                                value={repoSearch}
                                onChange={e => setRepoSearch(e.target.value)}
                                style={{ width: '100%' }}
                            />
                        </div>
                        <table className="data-table">
                            <thead><tr><th>repository</th><th></th></tr></thead>
                            <tbody>
                            {filteredRepos.slice(0, 20).map(r => (
                                <tr key={r.id}>
                                    <td style={{ color: 'var(--text-primary)' }}>{r.name}</td>
                                    <td style={{ textAlign: 'right' }}>
                                        <button
                                            className="btn btn-primary"
                                            onClick={() => run(r.id)}
                                            disabled={running === r.id}
                                        >
                                            {running === r.id ? '...' : 'analyze'}
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>

                    <div className="card">
                        <div className="card-header">
                            <span>history ({history.length})</span>
                        </div>
                        {history.length === 0 ? (
                            <div className="empty-state"><p>no analyses yet</p></div>
                        ) : (
                            <table className="data-table">
                                <thead><tr><th>repo</th><th>score</th><th>time</th><th></th></tr></thead>
                                <tbody>
                                {history.map((h, i) => (
                                    <tr key={`${h.id ?? h.repositoryId}-${h.timestamp}-${i}`}>
                                        <td style={{ color: 'var(--text-primary)' }}>{h.repositoryName || `#${h.repositoryId}`}</td>
                                        <td className="num">{h.calculatedScore?.toFixed(2)}</td>
                                        <td style={{ color: 'var(--text-dim)', fontSize: 11 }}>
                                            {h.timestamp ? new Date(h.timestamp).toLocaleString() : '-'}
                                        </td>
                                        <td style={{ textAlign: 'right' }}>
                                            <button
                                                className="btn btn-danger"
                                                onClick={() => deleteEntry(h.id)}
                                                disabled={deleting === h.id}
                                                style={{ padding: '4px 10px', fontSize: 10 }}
                                            >
                                                {deleting === h.id ? '...' : 'delete'}
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        )}
                    </div>
                </div>
            </div>
        </>
    );
}
