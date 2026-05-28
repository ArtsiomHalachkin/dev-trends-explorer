import keycloak, { getAccessToken, initKeycloak } from './keycloak';

const BASE = '/api';

async function request(path, options = {}) {
  const headers = { 'Content-Type': 'application/json', ...options.headers };

  try {
    await initKeycloak();
  } catch {
  }

  const token = await getAccessToken();
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  } else if (keycloak.authenticated) {
    await keycloak.updateToken(30);
    headers['Authorization'] = `Bearer ${keycloak.token}`;
  }

  const res = await fetch(`${BASE}${path}`, { ...options, headers });
  if (res.status === 204) return null;
  if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
  return res.json();
}

export const getRepos = () => request('/repos');
export const getRepo = (id) => request(`/repos/${id}`);
export const createRepo = (data) => request('/repos', { method: 'POST', body: JSON.stringify(data) });
export const updateRepo = (id, data) => request(`/repos/${id}`, { method: 'PUT', body: JSON.stringify(data) });
export const deleteRepo = (id) => request(`/repos/${id}`, { method: 'DELETE' });

export const getStats = () => request('/repos/stats');
export const getTopStars = () => request('/repos/stats/top-stars');
export const getHealth = () => request('/repos/stats/health');
export const getLanguages = () => request('/repos/stats/languages');

export const getDomains = () => request('/domains');
export const getOwners = () => request('/owners');

export const runAnalysis = (id) => request(`/analysis/run/${id}`, { method: 'POST' });
export const getAnalysisHistory = () => request('/analysis/history');
export const deleteAnalysisHistory = (id) => request(`/analysis/history/${id}`, { method: 'DELETE' });

export const getFavorites = () => request('/favorites');
export const addFavorite = (id) => request(`/favorites/${id}`, { method: 'POST' });
export const removeFavorite = (id) => request(`/favorites/${id}`, { method: 'DELETE' });

export const getSystemInfo = () => request('/system/info');
