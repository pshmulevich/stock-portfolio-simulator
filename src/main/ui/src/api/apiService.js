import axios from "axios";
import { serviceEndpoint } from "../configuration";

const USER_API_BASE_URL = serviceEndpoint;
const USER_INFO_KEY = "userInfo";

class ApiService {
  signup(registerationData) {
    return axios.post(USER_API_BASE_URL + "signup", registerationData);
  }

  login(credentials) {
    return axios.post(USER_API_BASE_URL + "token", credentials);
  }

  setUserInfo(authToken) {
    sessionStorage.setItem(USER_INFO_KEY, JSON.stringify(authToken));
  }

  getUserInfo() {
    return JSON.parse(sessionStorage.getItem(USER_INFO_KEY));
  }

  getAuthHeader() {
    return { headers: { Authorization: "Bearer " + this.getUserInfo().token } };
  }

  logOut() {
    sessionStorage.removeItem(USER_INFO_KEY);
  }

  getQuote(quoteUrl) {
    return this.axiosGet(quoteUrl);
  }

  getLots(lotsUrl) {
    return this.axiosGet(lotsUrl);
  }

  getPortfolio(portfolioUrl) {
    return this.axiosGet(portfolioUrl);
  }

  buyStock(buyStockRequestData) {
    return this.axiosPost("buyStock", buyStockRequestData);
  }

  sellStock(sellStockRequestData) {
    return this.axiosPost("sellStock", sellStockRequestData);
  }

  axiosGet(url) {
    return axios.get(USER_API_BASE_URL + url, this.getAuthHeader());
  }

  axiosPost(url, data) {
    return axios.post(USER_API_BASE_URL + url, data, this.getAuthHeader());
  }

  setCsrfHeader(response) {
    console.log("CSRF Token:", response.headers._csrf);
    axios.defaults.headers.post["X-XSRF-TOKEN"] =
      response && response.data && response.headers._csrf;
  }
}

export default new ApiService();
