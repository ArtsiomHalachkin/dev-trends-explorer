import { useState, useEffect } from 'react';
import { getRepos, addFavorite, removeFavorite, getFavorites } from '../api';

export default function Repos({ authenticated }) {
    const [repos, setRepos] = useState([]);
    const [favIds, setFavIds] = useState(new Set());
    const [search, setSearch] = useState('');
    const [langFilter, setLangFilter] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getRepos().then(data => {
            setRepos(data.items || []);
            setLoading(false);
        }).catch(() => setLoading(false));
    }, []);

    useEffect(() => {
        if (!authenticated) return;
        getFavorites().then(data => {
            setFavIds(new Set((data.items || []).map(f => f.repositoryId)));
        }).catch(() => {});
    }, [authenticated]);

    const languages = [...new Set(repos.map(r => r.languageName).filter(Boolean))].sort();

    const filtered = repos.filter(r => {
        const matchSearch = !search ||
            r.name?.toLowerCase().includes(search.toLowerCase()) ||
            r.fullName?.toLowerCase().includes(search.toLowerCase());
        const matchLang = !langFilter || r.languageName === langFilter;
        return matchSearch && matchLang;
    });

    const toggleFav = async (id) => {
        if (favIds.has(id)) {
            await removeFavorite(id);
            setFavIds(prev => { const n = new Set(prev); n.delete(id); return n; });
        } else {
            await addFavorite(id);
            setFavIds(prev => new Set(prev).add(id));
        }
    };

    return (
        <>
            <div className="page-header">
                <h2>Repositories</h2>
                <p className="page-desc">browse {repos.length} tracked github projects</p>
            </div>
            <div className="page-body">
                <div className="search-bar">
                    <input
                        placeholder="search repos..."
                        value={search}
                        onChange={e => setSearch(e.target.value)}
                    />
                    <select value={langFilter} onChange={e => setLangFilter(e.target.value)}>
                        <option value="">all languages</option>
                        {languages.map(l => <option key={l} value={l}>{l}</option>)}
                    </select>
                </div>

                {loading ? (
                    <div className="loading"><span className="loading-dot" /> loading repos...</div>
                ) : filtered.length === 0 ? (
                    <div className="empty-state">
                        <div className="empty-icon">&gt;_</div>
                        <p>no repositories found</p>
                    </div>
                ) : (
                    <div className="card">
                        <div className="card-header">
                            <span>{filtered.length} result{filtered.length !== 1 ? 's' : ''}</span>
                        </div>
                        <table className="data-table">
                            <thead>
                            <tr>
                                {authenticated && <th></th>}
                                <th>name</th>
                                <th>owner</th>
                                <th>language</th>
                                <th>domain</th>
                                <th>stars</th>
                                <th>forks</th>
                                <th>issues</th>
                            </tr>
                            </thead>
                            <tbody>
                            {filtered.map((r, i) => (
                                <tr key={r.id} style={{ animationDelay: `${i * 20}ms`, animation: 'fadeIn 0.3s ease-out both' }}>
                                    {authenticated && (
                                        <td>
                                            <button
                                                className={`btn-fav${favIds.has(r.id) ? ' active' : ''}`}
                                                onClick={() => toggleFav(r.id)}
                                                title={favIds.has(r.id) ? 'remove from favorites' : 'add to favorites'}
                                            >
                                                {favIds.has(r.id) ? '★' : '☆'}
                                            </button>
                                        </td>
                                    )}
                                    <td style={{ color: 'var(--text-primary)' }}>{r.name}</td>
                                    <td>{r.ownerLogin}</td>
                                    <td>{r.languageName && <span className="tag lang">{r.languageName}</span>}</td>
                                    <td>{r.domainName && <span className="tag domain">{r.domainName}</span>}</td>
                                    <td className="num">{r.stars?.toLocaleString()}</td>
                                    <td className="num">{r.forks?.toLocaleString()}</td>
                                    <td className="num">{r.issues?.toLocaleString()}</td>
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
