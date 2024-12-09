import axios from "axios";

const url = (uri) => {
  return "https://pet-service-api-d0hxdharh6d3bgd2.eastus2-01.azurewebsites.net/" + uri;
};

const headers = () => {
  const authToken = localStorage.getItem("authToken");
  return {
    headers: {
      Authorization: `Bearer ${authToken}`,
      "Content-Type": "application/json",
    },
  };
};

const apiService = {
  get: async (uri) => {
    try {
      const response = await axios.get(url(uri), headers());
      return response.data;
    } catch (error) {
      console.error("Erro ao fazer GET:", error);
      throw error;
    }
  },

  post: async (uri, data) => {
    try {
      const response = await axios.post(
        url(uri),
        JSON.stringify(data),
        headers(),
      );
      return response.data;
    } catch (error) {
      console.error("Erro ao fazer POST:", error);
      throw error;
    }
  },

  delete: async (uri, id) => {
    try {
      const response = await axios.delete(url(uri + "/" + id), headers());
      return response.data;
    } catch (error) {
      console.error("Erro ao fazer DELETE:", error);
      throw error;
    }
  },


  put: async (uri, data) => {
    try {
      const response = await axios.put(
          url(uri),
          JSON.stringify(data),
          headers(),
      );
      return response.data;
    } catch (error) {
      console.error("Erro ao fazer DELETE:", error);
      throw error;
    }
  },
};

export default apiService;
