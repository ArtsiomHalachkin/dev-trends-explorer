import { useState, useEffect } from 'react';
import { getStats, getTopStars, getHealth, getLanguages } from '../api';

export default function Stats() {
    const [stats, setStats] = useState(null);
    const [topStars, setTopStars] = useState([]);
    const [health, setHealth] = useState({});
    const [langDom, setLangDom] = useState({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        Promise.all([
            getStats().catch(() => null),
            getTopStars().catch(() => ({ items: [] })),
            getHealth().catch(() => ({ content: {} })),
            getLanguages().catch(() => ({ content: {} })),
        ]).then(([s, ts, h, l]) => {
            setStats(s?.content || null);
            setTopStars(ts?.items || []);
            setHealth(h?.content || {});
            setLangDom(l?.content || {});
            setLoading(false);
        });
    }, []);

    const maxHealth = Math.max(...Object.values(health), 1);

    if (loading) {
        return (
            <>
                <div className="page-header"><h2>Statistics</h2></div>
                <div className="page-body"><div className="loading"><span className="loading-dot" /> computing...</div></div>
            </>
        );
    }

    return (
        <>
            <div className="page-header">
                <h2>Statistics</h2>
                <p className="page-desc">computed metrics across all repositories</p>
            </div>
            <div className="page-body">
                {stats && (
                    <div className="stats-grid">
                        {stats.totalRepos != null && (
                            <div className="stat-card">
                                <div className="stat-label">total repos</div>
                                <div className="stat-value amber">{stats.totalRepos}</div>
                            </div>
                        )}
                        {stats.averageStars != null && (
                            <div className="stat-card">
                                <div className="stat-label">avg stars</div>
                                <div className="stat-value green">{Math.round(stats.averageStars).toLocaleString()}</div>
                            </div>
                        )}
                        {stats.averageForks != null && (
                            <div className="stat-card">
                                <div className="stat-label">avg forks</div>
                                <div className="stat-value blue">{Math.round(stats.averageForks).toLocaleString()}</div>
                            </div>
                        )}
                    </div>
                )}

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16, marginBottom: 28 }}>
                    <div className="card">
                        <div className="card-header">top starred</div>
                        <table className="data-table">
                            <thead><tr><th>#</th><th>repository</th></tr></thead>
                            <tbody>
                            {topStars.slice(0, 50).map((name, i) => (
                                <tr key={name} style={{ animation: `slideIn 0.3s ease-out ${i * 50}ms both` }}>
                                    <td className="num">{i + 1}</td>
                                    <td style={{ color: 'var(--text-primary)' }}>{name}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>

                    <div className="card">
                        <div className="card-header">language dominance</div>
                        <table className="data-table">
                            <thead><tr><th>language</th><th>top repo</th></tr></thead>
                            <tbody>
                            {Object.entries(langDom).map(([lang, repo], i) => (
                                <tr key={lang} style={{ animation: `slideIn 0.3s ease-out ${i * 50}ms both` }}>
                                    <td><span className="tag lang">{lang}</span></td>
                                    <td style={{ color: 'var(--text-primary)' }}>{repo}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>

                <div className="score-info-card" style={{ marginBottom: 16 }}>
                    <div className="score-info-title">
                        <span className="score-info-icon health">+</span>
                        Health Score
                    </div>
                    <p className="score-info-text">
                        Evaluates project maintenance quality. Checks for an active license, recent
                        releases, open PR response time, and issue resolution rate. Indicates how
                        well-maintained and sustainable a project is. Scale: <strong>0–100</strong>.
                    </p>
                    <div className="score-info-factors">
                        <span className="score-factor">⚖ License</span>
                        <span className="score-factor">⬆ Releases</span>
                        <span className="score-factor">⏱ PR time</span>
                        <span className="score-factor">✓ Issues closed</span>
                    </div>
                </div>

                <div className="card">
                    <div className="card-header">health scores</div>
                    <table className="data-table">
                        <thead><tr><th>repository</th><th>score</th><th>bar</th></tr></thead>
                        <tbody>
                        {Object.entries(health)
                            .sort(([, a], [, b]) => b - a)
                            .map(([name, score], i) => {
                                const pct = (score / maxHealth) * 100;
                                const color = pct > 60 ? 'var(--accent-green)' : pct > 30 ? 'var(--accent-amber)' : 'var(--accent-red)';
                                return (
                                    <tr key={name} style={{ animation: `fadeIn 0.3s ease-out ${i * 15}ms both` }}>
                                        <td style={{ color: 'var(--text-primary)' }}>{name}</td>
                                        <td className="num">{score.toFixed(1)}</td>
                                        <td>
                                            <div className="health-bar-container">
                                                <div className="health-bar">
                                                    <div className="health-bar-fill" style={{ width: `${pct}%`, background: color }} />
                                                </div>
                                                <span className="tag" style={{ background: `${color}20`, color }}>{pct > 60 ? 'healthy' : pct > 30 ? 'moderate' : 'low'}</span>
                                            </div>
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    );
}
