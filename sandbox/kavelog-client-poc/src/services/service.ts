import axios from "axios";

const defaultUrl = "http://127.0.0.1:8080/";

export const apiCall = () => {
  return axios
    .get(defaultUrl)
    .then(function (response) {
      return response.data;
    })
    .catch(function (error) {
      // handle error
      console.log(error);
    })
    .finally(function () {
      // always executed
    });
};
