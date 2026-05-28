import { useState, useEffect, useCallback } from 'react';
import { Routes, Route, useNavigate, useLocation } from 'react-router-dom';
import keycloak, { initKeycloak } from './keycloak';
import Repos from './pages/Repos';
import Stats from './pages/Stats';
import Favorites from './pages/Favorites';
import Analysis from './pages/Analysis';
import SystemInfo from './pages/SystemInfo';
import './App.css';

const NAV = [
  { path: '/', label: 'Repositories', icon: '>' },
  { path: '/stats', label: 'Statistics', icon: '#' },
  { path: '/favorites', label: 'Favorites', icon: '*', auth: true },
  { path: '/analysis', label: 'Analysis', icon: '~' },
  { path: '/system', label: 'System', icon: '$' },
];

export default function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [username, setUsername] = useState('');
  const [kcReady, setKcReady] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    initKeycloak()
        .then((auth) => {
          setAuthenticated(auth);
          if (auth) setUsername(keycloak.tokenParsed?.preferred_username || 'user');
          setKcReady(true);
        })
        .catch(() => setKcReady(true));
  }, []);

  const login = useCallback(() => keycloak.login(), []);
  const logout = useCallback(() => keycloak.logout({ redirectUri: window.location.origin }), []);

  if (!kcReady) {
    return (
        <div className="loading" style={{ height: '100vh' }}>
          <span className="loading-dot" />
          initializing...
        </div>
    );
  }

  return (
      <>
        <div className="scanline-overlay" />
        <div className="app-layout">
          <aside className="sidebar">
            <div className="sidebar-brand">
              <h1>DevTrends</h1>
              <div className="brand-sub">explorer v0.1</div>
            </div>

            <nav className="sidebar-nav">
              {NAV.map((item) => {
                if (item.auth && !authenticated) return null;
                return (
                    <button
                        key={item.path}
                        className={`nav-item${location.pathname === item.path ? ' active' : ''}`}
                        onClick={() => navigate(item.path)}
                    >
                      <span className="nav-icon">{item.icon}</span>
                      {item.label}
                    </button>
                );
              })}
            </nav>

            <div className="sidebar-footer">
              {authenticated ? (
                  <>
                    <div className="user-badge">
                      <span className="user-dot" />
                      {username}
                    </div>
                    <button className="auth-btn logout" onClick={logout}>sign out</button>
                  </>
              ) : (
                  <button className="auth-btn login" onClick={login}>sign in</button>
              )}
            </div>
          </aside>

          <main className="main-content">
            <Routes>
              <Route path="/" element={<Repos authenticated={authenticated} />} />
              <Route path="/stats" element={<Stats />} />
              <Route path="/favorites" element={authenticated ? <Favorites /> : <NeedAuth login={login} />} />
              <Route path="/analysis" element={<Analysis />} />
              <Route path="/system" element={<SystemInfo />} />
            </Routes>
          </main>
        </div>

        {/* Mobile bottom nav */}
        <nav className="mobile-nav">
          {NAV.map((item) => {
            if (item.auth && !authenticated) return null;
            return (
                <button
                    key={item.path}
                    className={`mobile-nav-item${location.pathname === item.path ? ' active' : ''}`}
                    onClick={() => navigate(item.path)}
                >
                  <span className="mobile-nav-icon">{item.icon}</span>
                  <span className="mobile-nav-label">{item.label}</span>
                </button>
            );
          })}
          {!authenticated && (
              <button className="mobile-nav-item" onClick={login}>
                <span className="mobile-nav-icon">@</span>
                <span className="mobile-nav-label">Sign in</span>
              </button>
          )}
        </nav>
      </>
  );
}

function NeedAuth({ login }) {
  return (
      <>
        <div className="page-header">
          <h2>Favorites</h2>
          <p className="page-desc">sign in to access your watchlist</p>
        </div>
        <div className="page-body">
          <div className="empty-state">
            <div className="empty-icon">*</div>
            <p>authentication required</p>
            <button className="btn btn-primary" style={{ marginTop: 16 }} onClick={login}>sign in with keycloak</button>
          </div>
        </div>
      </>
  );
}
