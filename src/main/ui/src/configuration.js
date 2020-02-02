const MODES = {
  local: "local",
  heroku: "heroku",
  sameServer: "sameServer"
};

const mode = MODES.sameServer;
const herokuServiceUrl =
  "https://portfolio-management-app.herokuapp.com/api/portfolio/";
const localServiceUrl = "http://localhost:9090/api/portfolio/";
const sameServerServiceUrl = "/api/portfolio/";

export const serviceEndpoint =
  mode === MODES.local
    ? localServiceUrl
    : mode === MODES.heroku
    ? herokuServiceUrl
    : sameServerServiceUrl;
