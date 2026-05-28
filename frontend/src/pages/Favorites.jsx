import { useState, useEffect } from 'react';
import { getFavorites, removeFavorite } from '../api';

export default function Favorites() {
    const [favs, setFavs] = useState([]);
    const [loading, setLoading] = useState(true);

    const load = () => {
        getFavorites().then(data => {
            setFavs(data.items || []);
            setLoading(false);
        }).catch(() => setLoading(false));
    };

    useEffect(load, []);

    const remove = async (id) => {
        await removeFavorite(id);
        setFavs(prev => prev.filter(f => f.repositoryId !== id));
    };

    return (
        <>
            <div className="page-header">
                <h2>Favorites</h2>
                <p className="page-desc">your personal watchlist</p>
            </div>
            <div className="page-body">
                {loading ? (
                    <div className="loading"><span className="loading-dot" /> loading...</div>
                ) : favs.length === 0 ? (
                    <div className="empty-state">
                        <div className="empty-icon">☆</div>
                        <p>no favorites yet — star repos from the repositories page</p>
                    </div>
                ) : (
                    <div className="card">
                        <div className="card-header">
                            <span>{favs.length} favorite{favs.length !== 1 ? 's' : ''}</span>
                        </div>
                        <table className="data-table">
                            <thead>
                            <tr>
                                <th>repository</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            {favs.map((f, i) => (
                                <tr key={f.repositoryId} style={{ animation: `slideIn 0.3s ease-out ${i * 40}ms both` }}>
                                    <td style={{ color: 'var(--text-primary)' }}>{f.repositoryName}</td>
                                    <td style={{ textAlign: 'right' }}>
                                        <button className="btn btn-danger" onClick={() => remove(f.repositoryId)}>remove</button>
                                    </td>
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
