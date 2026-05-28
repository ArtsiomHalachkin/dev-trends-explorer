import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: 'http://localhost:8080',
  realm: 'devtrends',
  clientId: 'devtrends-frontend',
});

let initPromise;

export function initKeycloak() {
  if (!initPromise) {
    initPromise = keycloak.init({
      onLoad: 'check-sso',
      pkceMethod: 'S256',
      checkLoginIframe: false,
    });
  }
  return initPromise;
}

export async function getAccessToken() {
  await initKeycloak();
  if (!keycloak.token) return null;

  try {
    await keycloak.updateToken(30);
  } catch {
  }

  return keycloak.token || null;
}

export default keycloak;
